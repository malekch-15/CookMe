package cookme.recipesmodel;

import java.util.List;

public record RecipeApi(
        List<Meal> meals
) {
}
