package cookme.services;

import cookme.recipesmodel.BaseIngredient;
import cookme.repository.IngredientsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientsService {

   private final IngredientsRepo ingredientsRepo;
   private final IdService idService;

   public List<BaseIngredient> findAll() {
       return ingredientsRepo.findAll();
   }
   public BaseIngredient findById(String id) {
       return ingredientsRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Ingredient not found"));
   }
   public List<BaseIngredient> findByName(String name) {
       List<BaseIngredient> baseIngredient = ingredientsRepo.findByName(name);
               if(baseIngredient == null){
                   throw new NoSuchElementException("Ingredient not found");
               }else return baseIngredient;

   }
   public BaseIngredient save(BaseIngredient ingredient) {
       String generatedId = idService.generateId();
       BaseIngredient ingredientWithId = ingredient.withId(generatedId);
       return ingredientsRepo.save(ingredientWithId);
   }

}
