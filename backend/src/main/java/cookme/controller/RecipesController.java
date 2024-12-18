package cookme.controller;

import cookme.api.ApiService;
import cookme.api.dto.*;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeDto;
import cookme.recipesmodel.RecipeIngredient;
import cookme.services.AppUserService;
import cookme.services.RecipesService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;



@RestController
@RequestMapping("/api/cookMe")
@RequiredArgsConstructor
public class RecipesController {
    private final RecipesService recipesService;
    private final AppUserService appUserService;
    private final ApiService apiService;

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
        return recipesService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipeWithId(@PathVariable String id) {
        recipesService.findRecipeById(id);
        recipesService.deleteRecipe(id);
    }
    // User Favorites Endpoints

    @PostMapping("/user/favorites/{recipeId}")
    public void addRecipeToFavorites(@RequestBody String userId, @PathVariable String recipeId) {
       Recipe recipe=recipesService.findRecipeById(recipeId);
        appUserService.addRecipeToFavorites(userId, recipe);
    }

    @DeleteMapping("/users/{recipeId}/favorite")
    public void removeRecipeFromFavorites(@RequestBody String userId, @PathVariable String recipeId) {
        Recipe recipe=recipesService.findRecipeById(recipeId);
        appUserService.removeRecipeFromFavorites(userId, recipe);
    }

    // User Ingredient Endpoints
    @GetMapping("/user/{userId}/ingredients")
    public List<RecipeIngredient> getUserIngredients(@PathVariable String userId) {
        return appUserService.getUserIngredient(userId);
    }

    @PostMapping("/user/{userId}/ingredients")
    public void addIngredientToUser(@PathVariable String userId, @RequestBody BaseIngredient ingredient, @RequestParam String quantity) {
        appUserService.addIngredientToUser(userId, ingredient, quantity);
    }

    @DeleteMapping("/user/{userId}/ingredients")
    public ResponseEntity<String> removeIngredientFromUser(@PathVariable String userId, @RequestBody String ingredient) {
        try {
            appUserService.removeIngredientFromUser(userId, ingredient);
            return ResponseEntity.ok("Ingredient removed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not found");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    //MealPlan
    @GetMapping("/user/{userId}/mealPlan")
    public List<MealBasic> getMealPlan(@PathVariable String userId) {
        List<RecipeIngredient> userIngredient = appUserService.getUserIngredient(userId);
        List<String> ingredientNames = userIngredient.stream()
                .map(ingredient -> ingredient.ingredient().name())
                .toList();
        return ingredientNames.stream()
                .flatMap(ingredient -> {
                    // For each ingredient, call the API and return a stream of MealBasicResponse
                    List<MealBasic> mealsForIngredient = apiService.getMealByIngredient(ingredient);
                    return mealsForIngredient.stream(); // Convert the list to a stream to use flatMap
                })
                .distinct()
                .limit(6)
                .toList();
    }

    @GetMapping("/meal/{mealName}")
    public RecipeDto getRecipe(@PathVariable String mealName) {

        Meal meal = apiService.getMealByName(mealName);

        // Convert the MealResponse into your Recipe domain model

        return apiService.convertMealToRecipe(meal);
    }
    @PostMapping("/mealApi")
    public Recipe postRecipeApi(@RequestBody RecipeDto recipeApi) {
        return recipesService.saveRecipes(recipeApi);
    }
}
