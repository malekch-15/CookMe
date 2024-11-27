package cookme.services;

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
        return recipesRepo.findById(id).orElseThrow(() -> new NoSuchElementException("No Recipes found with this "+id));
    }

    public void deleteRecipe(String id){
       recipesRepo.deleteById(id);
    }

}
