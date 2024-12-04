package cookme.controller;

import cookme.recipesmodel.Ingredient;
import cookme.recipesmodel.Recipes;
import cookme.recipesmodel.Status;
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
class RecipesControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RecipesRepo recipesRepo;

    @BeforeEach
    void setup() {
        recipesRepo.deleteAll();
        Ingredient ingredient1=new Ingredient("1","Eggs",3);
        Ingredient ingredient2=new Ingredient("2","Potatoes",4);
        List<Ingredient> ingredients = List.of(ingredient1,ingredient2);
        Recipes recipe1 = new Recipes("1", "a", "a", 12, "a",
                "a", Status.FAVORITE, ingredients);

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
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }
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
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }]
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
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }]
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
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }]
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
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }]
                         }
                      """)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {"name": "a",
                        "description": "a",
                        "time": 20,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": [{ "id": "1", "name": "Eggs", "quantity": 3 },
                                        { "id": "2", "name": "Potatoes", "quantity": 4 }]
                        }
""")).andExpect(jsonPath("$.id").isNotEmpty());
    }
}