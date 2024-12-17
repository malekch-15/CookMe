package cookme.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.RecipeIngredient;
import cookme.repository.AppUserRepo;
import cookme.repository.IngredientsRepo;
import cookme.services.AppUserService;

import cookme.user.AppUser;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

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

}