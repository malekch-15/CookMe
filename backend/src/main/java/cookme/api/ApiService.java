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

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ApiService {

    private final IngredientsService ingredientsService;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1")
            .build();

    public List<MealBasic> getMealByIngredient(String ingredient) {
        // Dynamically construct the URI with the ingredient
        MealBasicResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/filter.php")
                        .queryParam("i", ingredient)
                        .build())
                .retrieve()
                .bodyToMono(MealBasicResponse.class)
                .block(); // Use .block() to get the result synchronously for now

        // Ensure that response.getMeals() is not null
        List<MealBasic> mealsForIngredient = (response != null && response.meals() != null)
                ? response.meals()
                : List.of(); // Return an empty list if null

        // Now it's safe to use the stream() method
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
                .block(); // Make sure to use Mono if the call is synchronous.

        if (mealResponse == null || mealResponse.meals() == null ) {
            throw new RuntimeException("Meal not found");
        }

        // Return the first meal from the list.
        return mealResponse.meals().get(0);
    }

    public Recipe convertMealToRecipe(Meal meal) {
            List<RecipeIngredient> ingredients = new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                String ingredientName = getIngredientByIndex(meal, i);
                String measure = getMeasureByIndex(meal, i);

                if (ingredientName != null && !ingredientName.isEmpty() && measure != null && !measure.isEmpty()) {
                    // Use existing or create a new BaseIngredient
                    List<BaseIngredient> baseIngredients = ingredientsService.findByName(ingredientName);

                    BaseIngredient baseIngredient;
                    if (!baseIngredients.isEmpty()) {
                        // Use the first match
                        baseIngredient = baseIngredients.get(0);
                    } else {
                        // Save a new ingredient and use it
                        baseIngredient = ingredientsService.save(new BaseIngredient("", ingredientName));
                    }

                    // Parse the measure and add the RecipeIngredient
                   String quantity = parseMeasure(measure);
                    ingredients.add(new RecipeIngredient(quantity, baseIngredient));
                }
            }

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


