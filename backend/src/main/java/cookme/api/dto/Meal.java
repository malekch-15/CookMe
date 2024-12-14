package cookme.api.dto;

import java.util.List;

public record Meal(
        List<MealResponse> mealResponseRespons
) {
}
