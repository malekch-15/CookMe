package cookme.controller;

import cookme.recipesmodel.Recipes;
import cookme.recipesmodel.RecipesModelDto;
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
    public List<Recipes> getAll() {
        return recipesService.findAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipes getRecipeWithId(@PathVariable String id) {
        return recipesService.findRecipesById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Recipes addRecipe(@RequestBody Recipes recipe) {
        return recipesService.addRecipes(recipe);
    }

    @PutMapping("/update/{id}")
    public Recipes updateRecipe(@PathVariable String id, @RequestBody RecipesModelDto recipe) {
        return recipesService.editRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipeWithId(@PathVariable String id) {
    recipesService.findRecipesById(id);
    recipesService.deleteRecipe(id);
    }




}
