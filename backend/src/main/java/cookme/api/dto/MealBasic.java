package cookme.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MealBasic(@JsonProperty("idMeal")
                        String idMeal,

                        @JsonProperty("strMeal")
                        String strMeal,

                        @JsonProperty("strMealThumb")
                        String strMealThumb) {
}
