package cookme.api;
import cookme.api.dto.*;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Status;
import cookme.services.IngredientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class ApiService {

    private final IngredientsService ingredientsService;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1")
            .build();

    public List<MealBasic> getMealByIngredient(String ingredient) {

        MealBasicResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/filter.php")
                        .queryParam("i", ingredient)
                        .build())
                .retrieve()
                .bodyToMono(MealBasicResponse.class)
                .block();


        List<MealBasic> mealsForIngredient = (response != null && response.meals() != null)
                ? response.meals()
                : List.of();


        mealsForIngredient.forEach(System.out::println);

        return mealsForIngredient;
    }

    public Meal getMealByName(String name) {
        MealResponse mealResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search.php")
                        .queryParam("s", name)
                        .build())
                .retrieve()
                .bodyToMono(MealResponse.class)
                .block();

        if (mealResponse == null || mealResponse.meals() == null ) {
            throw new RuntimeException("Meal not found");
        }

        return mealResponse.meals().get(0);
    }

    public Recipe convertMealToRecipe(Meal meal) {
        List<RecipeIngredient> ingredients = IntStream.range(1, 21)  // Creates a stream of integers from 1 to 20
                .mapToObj(i -> {
                    String ingredientName = getIngredientByIndex(meal, i);
                    String measure = getMeasureByIndex(meal, i);

                    // Check if both ingredient name and measure are not null or empty
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
                        return null;  // Skip invalid ingredients
                    }
                })
                .filter(Objects::nonNull)  // Remove null values (invalid ingredients)
                .collect(Collectors.toList());  // Collect to a list

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
            return null; // Default to 1.0 if parsing fails
        }
    }
    // Helper methods to get ingredient and measure by index
    private String getIngredientByIndex(Meal meal, int index) {
        try {
            // Dynamically access the ingredient field based on the index
            return (String) Meal.class.getMethod("getStrIngredient" + index).invoke(meal);
        } catch (Exception e) {
            return null;
        }
    }

    private String getMeasureByIndex(Meal meal, int index) {
        try {
            // Dynamically access the measure field based on the index
            return (String) Meal.class.getMethod("getStrMeasure" + index).invoke(meal);
        } catch (Exception e) {
            return null;
        }
    }
    }


