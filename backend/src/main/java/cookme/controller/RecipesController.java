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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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
    public void addIngredientToUser(@PathVariable String userId, @RequestBody BaseIngredient ingredient, @RequestParam String quantity) {
        appUserService.addIngredientToUser(userId, ingredient, quantity);
    }

    @DeleteMapping("/user/{userId}/ingredients")
    public void removeIngredientFromUser(@PathVariable String userId, @RequestBody BaseIngredient ingredient) {
        appUserService.removeIngredientFromUser(userId, ingredient);
    }
    //MealPlan
    @GetMapping("/user/{userId}/mealPlan")
    public  List<MealBasic> getMealPlan(@PathVariable String userId) {
        List<RecipeIngredient>  userIngredient= appUserService.getUserIngredient(userId);
        List<String> ingredientNames = userIngredient.stream()
                .map(ingredient -> ingredient.ingredient().name())
                .toList();
       return  ingredientNames.stream()
                .flatMap(ingredient -> {
                    // Step 3: For each ingredient, call the API and return a stream of MealBasicResponse
                    List<MealBasic> mealsForIngredient = apiService.getMealByIngredient(ingredient);
                    return mealsForIngredient.stream(); // Convert the list to a stream to use flatMap
                })
                .distinct()  // Optional: if you want to avoid duplicate recipes
                .collect(Collectors.toList());


    }
    @GetMapping("/meal/{mealName}")
    public Recipe getRecipe(@PathVariable String mealName) {

        // Fetch the MealResponse from the API by meal name
        Meal meal= apiService.getMealByName(mealName);
        // Convert the MealResponse into your Recipe domain model
       Recipe newRecipe= apiService.convertMealToRecipe(meal);
       // add the MealResponse into dateBase
       RecipeDto recipeToSave=new RecipeDto(newRecipe.name(),newRecipe.description(),newRecipe.time(),newRecipe.imageUrl(),
               newRecipe.preparation(),newRecipe.status(),newRecipe.ingredients());

       recipesService.saveRecipes(recipeToSave);

        return newRecipe;
    }
}
