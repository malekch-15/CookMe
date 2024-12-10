package cookme.controller;

import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.Status;
import cookme.repository.IngredientsRepo;
import cookme.repository.RecipesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RecipesRepo recipesRepo;
    @Autowired
    private IngredientsRepo ingredientsRepo;

    @BeforeEach
    void setup() {
        recipesRepo.deleteAll();
        BaseIngredient ingredient1 = new BaseIngredient("1", "Eggs");
        BaseIngredient ingredient2 = new BaseIngredient("2", "Potatoes");
        ingredientsRepo.save(ingredient1);
        ingredientsRepo.save(ingredient2);

        RecipeIngredient ingredients = new RecipeIngredient(2, ingredient1);
        RecipeIngredient ingredients2 = new RecipeIngredient(2, ingredient2);
        List<RecipeIngredient> ingredientsList = List.of(ingredients, ingredients2);

        Recipe recipe1 = new Recipe("1", "a", "a", 12, "a", "a", Status.FAVORITE, ingredientsList);
        recipesRepo.save(recipe1);
    }


    @Test
    void getAllRecipe_expectListWithOneRecipe_whenOneRecipeSaved() throws Exception {
        //WHEN
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/cookMe")
                )
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""  
                        [
                                 {
                        "id": "1",
                        "name": "a",
                        "description": "a",
                        "time": 12,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients":[ {
                                             "quantity": 2,
                                                  "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                           {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "2",
                                                 "name": "Potatoes"
                                              }
                                           }
                                         ]
                        }
                        ]
                        """));
    }

    @Test
    void getRecipeWithId_returnRecipeWithId1_whenRecipeWithId1Saved() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/cookMe/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                        "id": "1",
                        "name": "a",
                        "description": "a",
                        "time": 12,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": [ {
                                             "quantity": 2,
                                              "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                          {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "2",
                                                 "name": "Potatoes"
                                              }
                                           }
                                         ]
                         }
                        
                        """));
    }
    @Test
    void postRecipeWithId_returnRecipeWithId2_whenRecipeWithId2Saved() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/cookMe/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                 {
                        "name": "a",
                        "description": "a",
                        "time": 12,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients":[ {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                           {
                                             "quantity": 2,
                                            "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           }
                                         ]
                         }
                        
                        """)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                                 {
                      
                        "name": "a",
                        "description": "a",
                        "time": 12,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": [ {
                                             "quantity": 2,
                                            "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                           {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           }
                                         ]
                         }
                        
                        """)
                ).andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void deleteRecipeWithId_shouldDeleteRecipeWithValidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/cookMe/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void putRecipeWithId_shouldUpdateRecipeWithValidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/cookMe/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                        "name": "a",
                        "description": "a",
                        "time": 20,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": [ {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                           {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           }
                                         ]
                         }
                      """)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {"name": "a",
                        "description": "a",
                        "time": 20,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": [ {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           },
                                           {
                                             "quantity": 2,
                                             "ingredient": {
                                                "id": "1",
                                                 "name": "Eggs"
                                              }
                                           }
                                         ]
                        }
""")).andExpect(jsonPath("$.id").isNotEmpty());
    }
}