package cookme.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MealResponse {

    @JsonProperty("idMeal")
    private String idMeal;

    @JsonProperty("strMeal")
    private String strMeal;

    @JsonProperty("strInstructions")
    private String strInstructions;

    @JsonProperty("strMealThumb")
    private String strMealThumb;

    @JsonProperty("strTags")
    private String strTags;

    @JsonProperty("strIngredient1")
    private String strIngredient1;
    @JsonProperty("strIngredient2")
    private String strIngredient2;
    @JsonProperty("strIngredient3")
    private String strIngredient3;
    @JsonProperty("strIngredient4")
    private String strIngredient4;
    @JsonProperty("strIngredient5")
    private String strIngredient5;
    @JsonProperty("strIngredient6")
    private String strIngredient6;
    @JsonProperty("strIngredient7")
    private String strIngredient7;
    @JsonProperty("strIngredient8")
    private String strIngredient8;
    @JsonProperty("strIngredient9")
    private String strIngredient9;
    @JsonProperty("strIngredient10")
    private String strIngredient10;
    @JsonProperty("strIngredient11")
    private String strIngredient11;
    @JsonProperty("strIngredient12")
    private String strIngredient12;
    @JsonProperty("strIngredient13")
    private String strIngredient13;
    @JsonProperty("strIngredient14")
    private String strIngredient14;
    @JsonProperty("strIngredient15")
    private String strIngredient15;

    @JsonProperty("strMeasure1")
    private String strMeasure1;
    @JsonProperty("strMeasure2")
    private String strMeasure2;
    @JsonProperty("strMeasure3")
    private String strMeasure3;
    @JsonProperty("strMeasure4")
    private String strMeasure4;
    @JsonProperty("strMeasure5")
    private String strMeasure5;
    @JsonProperty("strMeasure6")
    private String strMeasure6;
    @JsonProperty("strMeasure7")
    private String strMeasure7;
    @JsonProperty("strMeasure8")
    private String strMeasure8;
    @JsonProperty("strMeasure9")
    private String strMeasure9;
    @JsonProperty("strMeasure10")
    private String strMeasure10;
    @JsonProperty("strMeasure11")
    private String strMeasure11;
    @JsonProperty("strMeasure12")
    private String strMeasure12;
    @JsonProperty("strMeasure13")
    private String strMeasure13;
    @JsonProperty("strMeasure14")
    private String strMeasure14;
    @JsonProperty("strMeasure15")
    private String strMeasure15;

    // Derived lists for ingredients and measures
    private List<String> ingredients;
    private List<String> measures;

    // Constructor logic to generate the lists of ingredients and measures
    public void generateIngredientsAndMeasures() {
        this.ingredients = Stream.of(
                        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
                )
                .filter(ingredient -> ingredient != null && !ingredient.isEmpty())
                .collect(Collectors.toList());

        this.measures = Stream.of(
                        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
                        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
                )
                .filter(measure -> measure != null && !measure.isEmpty())
                .collect(Collectors.toList());
    }
}
