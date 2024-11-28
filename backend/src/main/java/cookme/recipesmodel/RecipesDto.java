package cookme.recipesmodel;

import java.util.List;

public record RecipesDto(String name,
                         String description,
                         Integer time,
                         String imageUrl,
                         String preparation,
                         Favorite status,
                         List<String> ingredients) {
}
