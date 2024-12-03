package cookme.services;

import cookme.recipesmodel.RecipesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cookme.recipesmodel.Recipes;
import cookme.repository.RecipesRepo;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipesService {
    private final RecipesRepo recipesRepo;

    public List<Recipes> findAllRecipes() {
        return recipesRepo.findAll();
    }

    public Recipes findRecipesById(String id) {
        return recipesRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No Recipes found with this " + id));
    }

    public Recipes saveRecipes(RecipesDto recipe) {
        String id = UUID.randomUUID().toString();
        Recipes newRecipe = new Recipes(id, recipe.name(),
                recipe.description(),
                recipe.time(),
                recipe.imageUrl(),
                recipe.preparation(),
                recipe.status(),
                recipe.ingredients());
        return recipesRepo.save(newRecipe);
    }

    public void deleteRecipe(String id) {
        recipesRepo.deleteById(id);
    }

}
