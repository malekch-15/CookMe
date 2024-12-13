package cookme.recipesmodel;

import java.util.List;

public record Meal(String idMeal,
                   String strMeal,
                   String strInstructions,
                   String strMealThumb,
                   String strTags,
                   List<String> ingredients,
                   List<String> measures){


}
