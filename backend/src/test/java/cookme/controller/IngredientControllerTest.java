package cookme.controller;

import cookme.repository.IngredientsRepo;
import org.junit.jupiter.api.Test;
import cookme.recipesmodel.BaseIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class IngredientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

   @Autowired
   private IngredientsRepo ingredientsRepo;



    @BeforeEach
    void setUp() {
        BaseIngredient ingredient1;
        BaseIngredient ingredient2;
        ingredient1 = new BaseIngredient("1", "Tomato");
        ingredient2 = new BaseIngredient("2", "Salt");
       ingredientsRepo.save(ingredient1);
      ingredientsRepo.save(ingredient2);
    }

    @Test
    void GetIngredients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ingredient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {"id": "1", "name": "Tomato"},
                            {"id": "2", "name": "Salt"}
                        ]
                        """));
    }
@Test
    void GetIngredientsById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ingredient/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                               {"id": "1", "name": "Tomato"}
                               """));
}
@Test
    void PostIngredient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/ingredient")
                        .contentType(MediaType.APPLICATION_JSON).content("""
                      {"name": "Tomatoo"}
                      """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        { "name": "Tomatoo"}""")).andExpect(jsonPath("$.id").isNotEmpty());
}

}
