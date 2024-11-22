package cookme.controller;

import cookme.recipesmodel.Recipes;
import cookme.recipesmodel.RecipesModelDto;
import cookme.services.RecipesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cookme")
@RequiredArgsConstructor
public class RecipesController {
    private final RecipesService recipesService;

    @GetMapping()
    public List<Recipes> getAll() {
        return recipesService.findAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipes getRecipe(@PathVariable String id) {
        return recipesService.findRecipesById(id);
    }

    @PostMapping()
    public Recipes addRecipe(@RequestBody Recipes recipe) {
        return recipesService.addRecipes(recipe);
    }

    @PutMapping("/update/{id}")
    public Recipes updateRecipe(@PathVariable String id, @RequestBody RecipesModelDto recipe) {
        return recipesService.editRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable String id) {
        recipesService.deleteRecipe(id);
    }
}
