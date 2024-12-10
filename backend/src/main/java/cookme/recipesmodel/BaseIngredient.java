package cookme.recipesmodel;

import lombok.With;

@With
public record BaseIngredient(@With String id, String name) {
}
