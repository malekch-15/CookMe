package cookme.recipesmodel;

import java.util.List;

public record Recipes(String id,
                      String name,
                      String description,
                      Integer time,
                      String imageUrl,
                      String preparation,
                      Favorite status,
                      List<String> ingredients) {
}
