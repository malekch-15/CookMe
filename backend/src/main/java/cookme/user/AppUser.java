package cookme.user;

import cookme.recipesmodel.BaseIngredient;

import java.util.List;

public record AppUser(String id,
                      String username,
                      String avatarUrl,
                      List<BaseIngredient> ingredient,
                      List<String> favorites) {
}
