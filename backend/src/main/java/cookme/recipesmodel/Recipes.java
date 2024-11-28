package cookme.recipesmodel;

import lombok.With;

import java.util.List;

public record Recipes(@With String id,
                      String name,
                      String description,
                      double time,
                      String imageUrl,
                      String preparation,
                      Status status,
                      List<String> ingredients) {
}
