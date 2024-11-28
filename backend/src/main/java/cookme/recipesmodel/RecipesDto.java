package cookme.recipesmodel;

import java.util.List;

public record RecipesDto(String name,
                         String description,
                         Integer time,
                         String imageUrl,
                         String preparation,
                         Status status,
                         List<String> ingredients) {
}
