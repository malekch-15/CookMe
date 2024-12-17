package cookme.api;

import cookme.recipesmodel.*;
import cookme.repository.AppUserRepo;
import cookme.repository.IngredientsRepo;
import cookme.repository.RecipesRepo;
import cookme.services.AppUserService;

import cookme.user.AppUser;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class ApiServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    AppUserRepo appUserRepo;
    @Autowired
    IngredientsRepo ingredientsRepo;
    private final RecipesRepo mockrecipesRepo = mock(RecipesRepo.class);

    @Test
    void getMealByIngredient() throws Exception {
        // Mock user ingredients
       appUserRepo.save(new AppUser("1", "test", "test",List.of(),null));
        BaseIngredient recipeBaseIngredient=new BaseIngredient("2", "Cheese");
        ingredientsRepo.save(recipeBaseIngredient);
       appUserService.addIngredientToUser("1",recipeBaseIngredient,"1");

        mockRestServiceServer.expect(requestTo("https://www.themealdb.com/api/json/v1/1/filter.php?i=Cheese"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                            "meals": [
                                {
                                    "strMeal": "15-minute chicken & halloumi burgers",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/vdwloy1713225718.jpg",
                                    "idMeal": "53085"
                                },
                                {
                                    "strMeal": "Big Mac",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/urzj1d1587670726.jpg",
                                    "idMeal": "53013"
                                },
                                {
                                    "strMeal": "Cream Cheese Tart",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/wurrux1468416624.jpg",
                                    "idMeal": "52779"
                                },
                                {
                                    "strMeal": "Kumpir",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/mlchx21564916997.jpg",
                                    "idMeal": "52978"
                                }
                            ]
                        }
                       """, MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cookMe/user/1/mealPlan"))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json("""
 
                            [
                                {
                                    "strMeal": "15-minute chicken & halloumi burgers",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/vdwloy1713225718.jpg",
                                    "idMeal": "53085"
                                },
                                {
                                    "strMeal": "Big Mac",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/urzj1d1587670726.jpg",
                                    "idMeal": "53013"
                                },
                                {
                                    "strMeal": "Cream Cheese Tart",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/wurrux1468416624.jpg",
                                    "idMeal": "52779"
                                },
                                {
                                    "strMeal": "Kumpir",
                                    "strMealThumb": "https://www.themealdb.com/images/media/meals/mlchx21564916997.jpg",
                                    "idMeal": "52978"
                                }
                            ]
                      
                """ ));
    }

    @Test
    void getMealByName() throws Exception {


        mockRestServiceServer.expect(requestTo("https://www.themealdb.com/api/json/v1/1/search.php?s=Strawberries%20Romanoff"))
                .andExpect(method(HttpMethod.GET)).andRespond(withSuccess("""
                        {
                             "meals": [
                                 {
                                     "idMeal": "53082",
                                     "strMeal": "Strawberries Romanoff",
                                     "strDrinkAlternate": null,
                                     "strCategory": "Dessert",
                                     "strArea": "Russian",
                                     "strInstructions": "In a medium bowl, combine hulled and quartered strawberries, 4 Tbsp sugar and 4 Tbsp liqueur, stir to combine then cover and refrigerate at least 1 hour and up to 2 hours, stirring once or twice.\\r\\n\\r\\nTwo photos of cut strawberries in a bowl with one having sugar being added to the bowl Two photos of cut up strawberries for Strawberry Romanoff \\r\\n\\r\\nJust before serving, in a large mixing bowl, combine 1 cup cold heavy cream and 1/4 cup powdered sugar, and beat with an electric mixer until stiff peaks form. Using a spatula, fold in 1/4 cup sour cream just until well blended.\\r\\n\\r\\nTo serve, stir strawberries then divide between 6 serving glasses or bowls. You can spoon a little syrup over the berries if you like. You can also use this syrup to soak a cake. Spoon cream over strawberries, dividing evenly. You can also use an ice cream scoop with trigger release for a nice rounded puff of cream. Serve right away or chill and enjoy within 2 hours of assembly.\\r\\n\\r\\n",
                                     "strMealThumb": "https://www.themealdb.com/images/media/meals/oe8rg51699014028.jpg",
                                     "strTags": "fruity",
                                     "strYoutube": "https://www.youtube.com/watch?v=ybWHc4Vi-xU",
                                     "strIngredient1": "Strawberries",
                                     "strIngredient2": "Sugar",
                                     "strIngredient3": "Grand Marnier",
                                     "strIngredient4": "Cream",
                                     "strIngredient5": "Sour Cream",
                                     "strIngredient6": "",
                                     "strIngredient7": "",
                                     "strIngredient8": "",
                                     "strIngredient9": "",
                                     "strIngredient10": "",
                                     "strIngredient11": "",
                                     "strIngredient12": "",
                                     "strIngredient13": "",
                                     "strIngredient14": "",
                                     "strIngredient15": "",
                                     "strIngredient16": "",
                                     "strIngredient17": "",
                                     "strIngredient18": "",
                                     "strIngredient19": "",
                                     "strIngredient20": "",
                                     "strMeasure1": "2 pint ",
                                     "strMeasure2": "4 tbs",
                                     "strMeasure3": "4 tbs",
                                     "strMeasure4": "1 cup ",
                                     "strMeasure5": "1/4 cup",
                                     "strMeasure6": " ",
                                     "strMeasure7": " ",
                                     "strMeasure8": " ",
                                     "strMeasure9": " ",
                                     "strMeasure10": " ",
                                     "strMeasure11": " ",
                                     "strMeasure12": " ",
                                     "strMeasure13": " ",
                                     "strMeasure14": " ",
                                     "strMeasure15": " ",
                                     "strMeasure16": " ",
                                     "strMeasure17": " ",
                                     "strMeasure18": " ",
                                     "strMeasure19": " ",
                                     "strMeasure20": " ",
                                     "strSource": "https://natashaskitchen.com/strawberries-romanoff-recipe/",
                                     "strImageSource": null,
                                     "strCreativeCommonsConfirmed": null,
                                     "dateModified": null
                                 }
                             ]
                         }
                        """,MediaType.APPLICATION_JSON));

        BaseIngredient baseIngredient1=new BaseIngredient("c97b7162-5087-4ebe-acc2-ed40aff6cb5f","Strawberries");
        BaseIngredient baseIngredient2=new BaseIngredient("43208dea-d7a1-4f1f-8429-7f6332ba3b7d","Sugar");
        BaseIngredient baseIngredient3=new BaseIngredient("2e4aec2a-d7b9-45ea-922d-5154b8c32ab0","Grand Marnier");
        BaseIngredient baseIngredient4=new BaseIngredient("be291e36-c490-4416-9c01-2e797c83f8e1","Cream");
        BaseIngredient baseIngredient5=new BaseIngredient("f6722b77-c3b8-426e-8dd2-347785fbe597","Sour Cream");
        ingredientsRepo.save(baseIngredient1);
        ingredientsRepo.save(baseIngredient2);
        ingredientsRepo.save(baseIngredient3);
        ingredientsRepo.save(baseIngredient4);
        ingredientsRepo.save(baseIngredient5);
        RecipeIngredient recipeIngredient=new RecipeIngredient("2 pint ",baseIngredient1);
        RecipeIngredient recipeIngredient1=new RecipeIngredient("4 tbs ",baseIngredient2);
        RecipeIngredient recipeIngredient2=new RecipeIngredient("4 tbs ",baseIngredient3);
        RecipeIngredient recipeIngredient3=new RecipeIngredient("1 cup ",baseIngredient4);
        RecipeIngredient recipeIngredient4=new RecipeIngredient("1/4 cup ",baseIngredient5);
        List<RecipeIngredient> recipeIngredients = List.of(
                recipeIngredient,
                recipeIngredient1,
                recipeIngredient2,
                recipeIngredient3,
                recipeIngredient4
        );
        RecipeDto recipeDto=new RecipeDto("Strawberries Romanoff","fruity", 30.0,"https://www.themealdb.com/images/media/meals/oe8rg51699014028.jpg",
                 "In a medium bowl, combine hulled and quartered strawberries, 4 Tbsp sugar and 4 Tbsp liqueur, stir to combine then cover and refrigerate at least 1 hour and up to 2 hours, stirring once or twice." +
                         "\\r\\n\\r\\nTwo photos of cut strawberries in a bowl with one having sugar being added to the bowl Two photos of cut up strawberries for " +
                         "Strawberry Romanoff \\r\\n\\r\\nJust before serving, in a large mixing bowl, combine 1 cup cold heavy cream and 1/4 cup powdered sugar, " +
                         "and beat with an electric mixer until stiff peaks form. Using a spatula, fold in 1/4 cup sour cream just until well blended.\\r\\n\\r\\nTo " +
                         "serve, stir strawberries then divide between 6 serving glasses or bowls. You can spoon a little syrup over the berries if you like. " +
                         "You can also use this syrup to soak a cake. Spoon cream over strawberries, dividing evenly. You can also use an ice cream scoop with trigger" +
                         " release for a nice rounded puff of cream. Serve right away or chill and enjoy within 2 hours of assembly.\\r\\n\\r\\n",Status.NOT_FAVORITE,
                recipeIngredients);
        Recipe recipe=new Recipe("53082",recipeDto.name(),recipeDto.description(),recipeDto.time(),recipeDto.imageUrl(),recipeDto.preparation(),recipeDto.status(),recipeDto.ingredients());
       when(mockrecipesRepo.save(recipe)).thenReturn(recipe);

       mockMvc.perform(MockMvcRequestBuilders.get("/api/cookMe/meal/Strawberries Romanoff"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json("""
                       
                        {
                            "id": "53082",
                            "name": "Strawberries Romanoff",
                            "description": "fruity",
                            "time": 30.0,
                            "imageUrl": "https://www.themealdb.com/images/media/meals/oe8rg51699014028.jpg",
                            "preparation": "In a medium bowl, combine hulled and quartered strawberries, 4 Tbsp sugar and 4 Tbsp liqueur, stir to combine then cover and refrigerate at least 1 hour and up to 2 hours, stirring once or twice.\\r\\n\\r\\nTwo photos of cut strawberries in a bowl with one having sugar being added to the bowl Two photos of cut up strawberries for Strawberry Romanoff \\r\\n\\r\\nJust before serving, in a large mixing bowl, combine 1 cup cold heavy cream and 1/4 cup powdered sugar, and beat with an electric mixer until stiff peaks form. Using a spatula, fold in 1/4 cup sour cream just until well blended.\\r\\n\\r\\nTo serve, stir strawberries then divide between 6 serving glasses or bowls. You can spoon a little syrup over the berries if you like. You can also use this syrup to soak a cake. Spoon cream over strawberries, dividing evenly. You can also use an ice cream scoop with trigger release for a nice rounded puff of cream. Serve right away or chill and enjoy within 2 hours of assembly.\\r\\n\\r\\n",
                            "status": "NOT_FAVORITE",
                            "ingredients": [
                                {
                                    "quantity": "2 pint ",
                                    "ingredient": {
                                        "id": "c97b7162-5087-4ebe-acc2-ed40aff6cb5f",
                                        "name": "Strawberries"
                                    }
                                },
                                {
                                    "quantity": "4 tbs",
                                    "ingredient": {
                                        "id": "43208dea-d7a1-4f1f-8429-7f6332ba3b7d",
                                        "name": "Sugar"
                                    }
                                },
                                {
                                    "quantity": "4 tbs",
                                    "ingredient": {
                                        "id": "2e4aec2a-d7b9-45ea-922d-5154b8c32ab0",
                                        "name": "Grand Marnier"
                                    }
                                },
                                {
                                    "quantity": "1 cup ",
                                    "ingredient": {
                                        "id": "be291e36-c490-4416-9c01-2e797c83f8e1",
                                        "name": "Cream"
                                    }
                                },
                                {
                                    "quantity": "1/4 cup",
                                    "ingredient": {
                                        "id": "f6722b77-c3b8-426e-8dd2-347785fbe597",
                                        "name": "Sour Cream"
                                    }
                                }
                            ]
                        }
                       """));

    }
}