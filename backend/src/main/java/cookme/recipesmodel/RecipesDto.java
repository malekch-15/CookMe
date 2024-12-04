package cookme.recipesmodel;

import java.util.List;

public record RecipesDto(String name,
                         String description,
                         double time,
                         String imageUrl,
                         String preparation,
                         Status status,
                         List<Ingredient> ingredients) {
}
