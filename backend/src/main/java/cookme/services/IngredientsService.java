package cookme.services;

import cookme.recipesmodel.BaseIngredient;
import cookme.repository.IngredientsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
   public BaseIngredient save(BaseIngredient ingredient) {
       idService.generateId();
       return ingredientsRepo.save(ingredient.withId(idService.generateId()));
   }

}
