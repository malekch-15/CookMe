package cookme.controller;

import cookme.recipesmodel.BaseIngredient;
import cookme.services.IngredientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
@RequiredArgsConstructor
public class IngredientController {
   private final IngredientsService ingredientsService;
   @GetMapping
   public List<BaseIngredient> getIngredients() {
       return ingredientsService.findAll();
   }

   @GetMapping("/{id}")
    public BaseIngredient getIngredient(@PathVariable String id) {
       return ingredientsService.findById(id);
   }

   @PostMapping
    public BaseIngredient addIngredient(@RequestBody BaseIngredient ingredient) {
       return ingredientsService.save(ingredient);
   }
    @PostMapping("/removeDuplicates")
    public ResponseEntity<String> removeDuplicates() {
        ingredientsService.removeDuplicateIngredients();
        return ResponseEntity.ok("Duplicate ingredients removed successfully!");
    }
}
