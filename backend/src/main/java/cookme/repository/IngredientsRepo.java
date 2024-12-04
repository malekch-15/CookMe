package cookme.repository;

import cookme.recipesmodel.BaseIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepo extends MongoRepository<BaseIngredient, String> {
}
