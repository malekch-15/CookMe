package cookme.controller;

import cookme.recipesmodel.Favorite;
import cookme.recipesmodel.Recipes;
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

        Recipes recipe1 = new Recipes("1", "a", "a", 12, "a",
                "a", Favorite.FAVORITE, List.of("a", "b", "c"));

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
                        "ingredients": ["a", "b", "c"]
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
                        "ingredients": ["a", "b", "c"]
                         }
                        
                        """));
    }


    @Test
    void addRecipe_returnTheGivenRecipe_whenRecipeSaved() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/cookMe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                         {
                                "id": "1",
                                "name": "a",
                                "description": "a",
                                "time": 12,
                                "imageUrl": "a",
                                "preparation": "a",
                                "status": "FAVORITE",
                                "ingredients": ["a", "b", "c"]
                                 }
                                
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                        
                        "name": "a",
                        "description": "a",
                        "time": 12,
                        "imageUrl": "a",
                        "preparation": "a",
                        "status": "FAVORITE",
                        "ingredients": ["a", "b", "c"]
                        }
                        """
                ));

    }

    @Test
    void updateRecipe_returnUpdatedRecipe_whenRecipeSaved() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/cookMe/update/1")
                        .contentType(MediaType.APPLICATION_JSON).content("""           
                                {
                                 "id": "1",
                                "name": "a",
                                "description": "aa",
                                "time": 12,
                                "imageUrl": "a",
                                "preparation": "a",
                                "status": "FAVORITE",
                                "ingredients": ["a", "b", "c"]
                                }
                                """

                        )).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """           
                                {
                                "id": "1",
                                "name": "a",
                                "description": "aa",
                                "time": 12,
                                "imageUrl": "a",
                                "preparation": "a",
                                "status": "FAVORITE",
                                "ingredients": ["a", "b", "c"]
                                 }
                                """
                ));
    }

    @Test
    void deleteRecipeWithId_shouldDeleteRecipeWithValidId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/cookMe/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}