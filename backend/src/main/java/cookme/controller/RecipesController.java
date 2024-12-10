package cookme.controller;

import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeDto;
import cookme.services.RecipesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/cookMe")
@RequiredArgsConstructor
public class RecipesController {
    private final RecipesService recipesService;

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
}
