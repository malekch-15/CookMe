package cookme.recipesmodel;

import org.springframework.data.mongodb.core.mapping.DBRef;

public record RecipeIngredient(String quantity, @DBRef BaseIngredient ingredient) {
}
