package cookme.services;

import cookme.recipesmodel.RecipesModelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cookme.recipesmodel.Recipes;
import cookme.repository.RecipesRepo;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipesService {
    private final RecipesRepo recipesRepo;
    public List<Recipes> findAllRecipes(){
        return recipesRepo.findAll();
    }
    public Recipes findRecipesById(String id){
        return recipesRepo.findById(id).orElseThrow(()->new NoSuchElementException("No Recipes found with this id"));
    }
    public Recipes addRecipes(Recipes recipe){
     String id = UUID.randomUUID().toString();
     Recipes savedRecipe = new Recipes(id, recipe.name(),recipe.description(),recipe.time()
                                      ,recipe.imageUrl(),recipe.preparation(),recipe.status(),recipe.ingredients());
        return recipesRepo.save(savedRecipe);
    }
    public Recipes editRecipe(String id, RecipesModelDto recipe){
        Optional<Recipes> find= recipesRepo.findById(id);
        if(find.isPresent()){
            Recipes updatedRecipe= new Recipes(id,
                    recipe.name(),
                    recipe.description(),
                    recipe.time(),
                    recipe.imageUrl(),
                    recipe.preparation(),
                    recipe.status(),
                    recipe.ingredients());
            return recipesRepo.save(updatedRecipe);
        }else throw new NoSuchElementException("No Recipes found with this id");
    }
    public void deleteRecipe(String id){
       recipesRepo.deleteById(id);
    }

}
