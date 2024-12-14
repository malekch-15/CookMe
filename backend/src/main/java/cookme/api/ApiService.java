package cookme.api;


import cookme.api.dto.MealBasic;
import cookme.api.dto.MealBasicResponse;
import cookme.api.dto.MealResponse;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Status;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public MealResponse getMealByName(String name) {
        // Dynamically construct the URI with the ingredient
        MealResponse body = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("search.php")
                        .queryParam("i", name)
                        .build())
                .retrieve()
                .bodyToMono(MealResponse.class)
                .block(); // Use .block() to get the result synchronously for now

        System.out.println(body);
        return body;
    }
    public Recipe convertMealToRecipe(MealResponse mealResponse) {
        // For now, you can set a dummy value for description and preparation time
        String description = mealResponse.getStrTags(); // Placeholder
        double time = 30.0; // Placeholder for preparation time


        // Creating RecipeIngredient objects from the ingredients and measures
        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (int i = 0; i < mealResponse.getIngredients().size(); i++) {
            // Creating BaseIngredient for each ingredient (assuming you have the id and name)
            BaseIngredient ingredient = new BaseIngredient(String.valueOf(i + 1), mealResponse.getIngredients().get(i));

            // Creating RecipeIngredient with a quantity and BaseIngredient
            ingredients.add(new RecipeIngredient(
                    Double.parseDouble(mealResponse.getMeasures().get(i)), // Convert measure (e.g., "1 cup") to a numeric value
                    ingredient // Pass the BaseIngredient
            ));
        }

        // Returning a new Recipe object
        return new Recipe(
                mealResponse.getIdMeal(), // id
                mealResponse.getStrMeal(), // name
                description, // description
                time, // time (can be updated if time is available in response)
                mealResponse.getStrMealThumb(), // image URL
                mealResponse.getStrInstructions(), // preparation instructions
                Status.NOT_FAVORITE, // Status (You can adjust this as needed)
                ingredients // list of RecipeIngredients
        );
    }
    }


