package cookme.repository;

import cookme.recipesmodel.BaseIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IngredientsRepo extends MongoRepository<BaseIngredient, String> {
 List< BaseIngredient> findByName(String name);
}
