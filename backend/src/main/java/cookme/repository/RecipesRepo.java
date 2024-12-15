package cookme.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import cookme.recipesmodel.Recipe;

import java.util.Optional;

@Repository
public interface RecipesRepo extends MongoRepository<Recipe, String> {
   Optional <Recipe> findByName(String name);
}
