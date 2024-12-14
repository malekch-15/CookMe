package cookme.api;


import cookme.api.dto.*;
import cookme.exception.MealNotFoundException;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Status;
import cookme.services.IngredientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.ArrayList;
import java.util.List;


@Service

public class ApiService {

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
        // Extract meal details from the provided MealDetails object
        String description = meal.getStrTags() != null ? meal.getStrTags() : "No tags available"; // Placeholder
        double time = 30.0; // Placeholder for preparation time (you could add logic to parse this from meal data)

        // Create the list of RecipeIngredient objects from the ingredients and measures
        List<RecipeIngredient> ingredients = new ArrayList<>();

        // Loop through the ingredients and measures
        for (int i = 1; i <= 20; i++) {
            String ingredient = getIngredientByIndex(meal, i);
            String measure = getMeasureByIndex(meal, i);

            // Ensure ingredient and measure are not null or empty
            if (ingredient != null && !ingredient.isEmpty() && measure != null && !measure.isEmpty()) {
                // Create BaseIngredient from the ingredient name
                BaseIngredient baseIngredient = new BaseIngredient(String.valueOf(i), ingredient);

                // Try parsing the measure as a numeric value. If it fails, default to 1.0
                double measureValue;
                try {
                    measureValue = Double.parseDouble(measure);
                } catch (NumberFormatException e) {
                    measureValue = 1.0; // Default to 1.0 if parsing fails
                }

                // Add the ingredient with its quantity to the list
                ingredients.add(new RecipeIngredient(measureValue, baseIngredient));
            }
        }

        // Create and return a Recipe object
        return new Recipe(
                meal.getIdMeal(),
                meal.getStrMeal(),
                description,
                time,
                meal.getStrMealThumb(), // Thumbnail image for the meal
                meal.getStrInstructions(), // Instructions for preparing the meal
                Status.NOT_FAVORITE, // Status (you can modify this as needed)
                ingredients
        );
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


