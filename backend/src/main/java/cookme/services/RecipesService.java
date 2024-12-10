package cookme.services;

import cookme.recipesmodel.RecipeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cookme.recipesmodel.Recipe;
import cookme.repository.RecipesRepo;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipesService {
    private final RecipesRepo recipesRepo;
    // ingServ

    public List<Recipe> findAllRecipes() {
        return recipesRepo.findAll();
    }

    public Recipe findRecipeById(String id) {
        //recipe ingredient every ingred base ing laden um the name to saved Dto
        return recipesRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No Recipes found with this " + id));
    }

    public Recipe saveRecipes(RecipeDto recipe) {
        String id = UUID.randomUUID().toString();
        Recipe newRecipe = new Recipe(id, recipe.name(),
                recipe.description(),
                recipe.time(),
                recipe.imageUrl(),
                recipe.preparation(),
                recipe.status(),
                recipe.ingredients());
        return recipesRepo.save(newRecipe);
    }
    public Recipe updateRecipe(String id, RecipeDto recipe) {
        Recipe updatedRecipe = findRecipeById(id);
        if (updatedRecipe != null) {
            Recipe newRecipe = new Recipe( id,recipe.name(),
                    recipe.description(),
                    recipe.time(),
                    recipe.imageUrl(),
                    recipe.preparation(),
                    recipe.status(),
                    recipe.ingredients());
            return recipesRepo.save(newRecipe);
        }else throw new NoSuchElementException("No Recipes found with this " + id);
    }

    public void deleteRecipe(String id) {
        recipesRepo.deleteById(id);
    }

}
