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
    public List<Recipes> getAll(){
      return recipesService.findAllRecipes();
    }
    @GetMapping("/{id}")
    public Recipes getRecipe(@PathVariable String id){
        return recipesService.findRecipesById(id);
    }

    @PostMapping()
    public Recipes addRecipe(@RequestBody RecipesModelDto recipe){
        Recipes newRecipe=new Recipes(null,recipe.name(),recipe.description(),recipe.time(),
                recipe.imageUrl(),recipe.preparation(),recipe.status(),recipe.ingredients());
        return recipesService.addRecipes(newRecipe);
    }
    @PutMapping("/update/{id}")
    public Recipes updateRecipe(@PathVariable String id, @RequestBody RecipesModelDto recipe){
        return recipesService.editRecipe(id,recipe);
    }
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable String id){
        recipesService.deleteRecipe(id);
    }
}
