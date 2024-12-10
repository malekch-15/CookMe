package cookme.recipesmodel;

import java.util.List;

public record RecipeDto(String name,
                        String description,
                        double time,
                        String imageUrl,
                        String preparation,
                        Status status,
                        List<RecipeIngredient> ingredients) {
}
