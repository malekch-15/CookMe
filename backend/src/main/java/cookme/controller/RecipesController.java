package cookme.controller;

import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeDto;
import cookme.recipesmodel.RecipeIngredient;
import cookme.services.AppUserService;
import cookme.services.RecipesService;
import cookme.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cookMe")
@RequiredArgsConstructor
public class RecipesController {
    private final RecipesService recipesService;
   private final AppUserService appUserService;
    @GetMapping()
    public List<Recipe> getAll() {
        return recipesService.findAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeWithId(@PathVariable String id) {

        return recipesService.findRecipeById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public Recipe postRecipe(@RequestBody RecipeDto recipe) {
        return recipesService.saveRecipes(recipe);
    }

    @PutMapping("/update/{id}")
    public Recipe putRecipe(@RequestBody RecipeDto recipe, @PathVariable String id) {
        return recipesService.updateRecipe(id,recipe);
    }
    @DeleteMapping("/{id}")
    public void deleteRecipeWithId(@PathVariable String id) {
    recipesService.findRecipeById(id);
    recipesService.deleteRecipe(id);
    }
    // User Favorites Endpoints

    @PostMapping("/user/{userId}/favorites/{recipeId}")
    public void addRecipeToFavorites(@PathVariable String userId, @PathVariable String recipeId) {
        Recipe recipe = recipesService.findRecipeById(recipeId);
        appUserService.addRecipeToFavorites(userId, recipe);
    }

    @DeleteMapping("/user/{userId}/favorites/{recipe}")
    public void removeRecipeFromFavorites(@PathVariable String userId, @PathVariable Recipe recipe) {
        appUserService.removeRecipeFromFavorites(userId, recipe);
    }

    // User Ingredient Endpoints
    @GetMapping("/user/{userId}/ingredients")
    public List<RecipeIngredient> getUserIngredients(@PathVariable String userId) {
        return appUserService.getUserIngredient(userId);
    }

    @PostMapping("/user/{userId}/ingredients")
    public void addIngredientToUser(@PathVariable String userId, @RequestBody BaseIngredient ingredient, @RequestParam double quantity) {
        appUserService.addIngredientToUser(userId, ingredient, quantity);
    }

    @DeleteMapping("/user/{userId}/ingredients")
    public void removeIngredientFromUser(@PathVariable String userId, @RequestBody BaseIngredient ingredient) {
        appUserService.removeIngredientFromUser(userId, ingredient);
    }
    //MealPlan
    @GetMapping("/user/{userId}/mealPlan")
    public List<Recipe> getMealPlan(@PathVariable String userId) {
        List<RecipeIngredient>  userIngredient= appUserService.getUserIngredient(userId);
        List<Recipe> recipes= recipesService.findAllRecipes();
        return recipes.stream()
                .filter(recipe ->  !recipe.ingredients().isEmpty() && recipe.ingredients().stream()
                        .allMatch(recipeIngredient ->
                                userIngredient.stream()
                                        .anyMatch(user ->
                                                user.ingredient().name().equals(recipeIngredient.ingredient().name())
                                        )
                        )
                )
                .toList();
    }
}
