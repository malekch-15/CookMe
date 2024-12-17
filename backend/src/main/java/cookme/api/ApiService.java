package cookme.api;
import cookme.api.dto.*;
import cookme.exception.MealNotFoundException;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Status;
import cookme.services.IngredientsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


@Service
public class ApiService {

    private final IngredientsService ingredientsService;
    private final RestClient restClient;
public ApiService(IngredientsService ingredientsService, RestClient.Builder clientBuilder) {
    this.ingredientsService = ingredientsService;
    restClient = clientBuilder
            .baseUrl("https://www.themealdb.com/api/json/v1/1")
            .build();
}
    public List<MealBasic> getMealByIngredient(String ingredient) {

        MealBasicResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/filter.php")
                        .queryParam("i", ingredient)
                        .build())
                .retrieve()
                .body(MealBasicResponse.class);


        List<MealBasic> mealsForIngredient = (response != null && response.meals() != null)
                ? response.meals()
                : List.of();


        mealsForIngredient.forEach(System.out::println);

        return mealsForIngredient;
    }

    public Meal getMealByName(String name) {
        MealResponse mealResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.php")
                        .queryParam("s", name)
                        .build())
                .retrieve()
                .body(MealResponse.class);

        if (mealResponse == null || mealResponse.meals() == null ) {
            throw new MealNotFoundException("Meal not found");
        }

        return mealResponse.meals().getFirst();
    }

    public Recipe convertMealToRecipe(Meal meal) {
        List<RecipeIngredient> ingredients = IntStream.range(1, 21)
                .mapToObj(i -> {
                    String ingredientName = getIngredientByIndex(meal, i);
                    String measure = getMeasureByIndex(meal, i);

                    if (ingredientName != null && !ingredientName.isEmpty() && measure != null && !measure.isEmpty()) {
                        // Find or save the ingredient
                        List<BaseIngredient> baseIngredients = ingredientsService.findByName(ingredientName);
                        BaseIngredient baseIngredient = baseIngredients.stream()
                                .findFirst()
                                .orElseGet(() -> ingredientsService.save(new BaseIngredient("", ingredientName)));  // Save if not found

                        // Return the RecipeIngredient
                        String quantity = parseMeasure(measure);
                        return new RecipeIngredient(quantity, baseIngredient);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();  // Collect to a list

        return new Recipe(
                meal.getIdMeal(),
                meal.getStrMeal(),
                meal.getStrTags() != null ? meal.getStrTags() : "No tags",
                30.0,
                meal.getStrMealThumb(),
                meal.getStrInstructions(),
                Status.NOT_FAVORITE,
                ingredients
        );
        }

    private String parseMeasure(String measure) {
        try {
            return measure;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    //Get ingredient and measure with index
    private String getIngredientByIndex(Meal meal, int index) {
        try {
            return (String) Meal.class.getMethod("getStrIngredient" + index).invoke(meal);
        } catch (Exception e) {
            return null;
        }
    }

    private String getMeasureByIndex(Meal meal, int index) {
        try {

            return (String) Meal.class.getMethod("getStrMeasure" + index).invoke(meal);
        } catch (Exception e) {
            return null;
        }
    }
    }


