package cookme.recipesmodel;

import org.springframework.data.mongodb.core.mapping.DBRef;

public record RecipeIngredient(double quantity, @DBRef BaseIngredient ingredient) {
}
