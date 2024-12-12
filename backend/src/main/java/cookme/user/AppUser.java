package cookme.user;


import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;

import java.util.List;

public record AppUser(String id,
                      String username,
                      String avatarUrl,
                      List<RecipeIngredient> ingredient,
                      List<Recipe> favorites) {
}
