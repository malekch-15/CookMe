package cookme.services;

import cookme.recipesmodel.BaseIngredient;
import cookme.repository.IngredientsRepo;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class IngredientsService {

   private final IngredientsRepo ingredientsRepo;
   private final IdService idService;

    @Autowired
    private MongoTemplate mongoTemplate;

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
    // Method to remove duplicate ingredients by name
    public void removeDuplicateIngredients() {
        // Step 1: Find all the duplicate names using aggregation
        List<Document> duplicates = mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        Aggregation.group("name") // Group by 'name' field
                                .count().as("count") // Count the occurrences of each name
                                .push("_id").as("ids"), // Collect document ids for each name
                        Aggregation.match(Criteria.where("count").gt(1)) // Filter duplicates (count > 1)
                ),
                BaseIngredient.class,  // The document class for the aggregation
                Document.class         // Return result as Document (MongoDB generic object)
        ).getMappedResults();

        // Step 2: Iterate over each duplicate and delete the excess documents
        for (Document duplicate : duplicates) {
            List<Object> ids = (List<Object>) duplicate.get("ids");
            ids.remove(0); // Keep the first document, remove the others

            // Step 3: Delete all but the first duplicate document
            if (!ids.isEmpty()) {
                // Correct way to instantiate a Query object
                Query query = new Query(Criteria.where("_id").in(ids)); // Instantiate using the constructor
                mongoTemplate.remove(query, BaseIngredient.class); // Remove duplicates by _id
            }
        }
    }
}
