package cookme.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import cookme.recipesmodel.Recipe;
@Repository
public interface RecipesRepo extends MongoRepository<Recipe, String> {
}
