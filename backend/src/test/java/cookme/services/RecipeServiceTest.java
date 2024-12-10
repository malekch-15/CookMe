package cookme.services;

import cookme.recipesmodel.*;
import cookme.repository.RecipesRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RecipeServiceTest {
    private final RecipesRepo mockrecipesRepo = mock(RecipesRepo.class);
    @InjectMocks
    private RecipesService recipesService = new RecipesService(mockrecipesRepo);
    BaseIngredient ingredient1=new BaseIngredient("1","Eggs");
    RecipeIngredient recipeIngredient=new RecipeIngredient(2,ingredient1);
    List<RecipeIngredient> ingredients = List.of(recipeIngredient);
    Recipe recipe1 = new Recipe("1", "a", "a", 12, "a",
            "a", Status.FAVORITE, ingredients);
    Recipe recipe2 = new Recipe("2", "a", "a", 12, "a",
            "a", Status.FAVORITE, ingredients);

    List<Recipe> recipes = List.of(recipe1, recipe2);

    @Test
    void getAllRecipes_return2Recipe_when2RecipeSaved() {

        when(mockrecipesRepo.findAll()).thenReturn(recipes);

        List<Recipe> actual = recipesService.findAllRecipes();
        //ASSERT
        assertEquals(recipes, actual);
        verify(mockrecipesRepo, times(1)).findAll();
    }

    @Test
    void getRecipeById_returnRecipeWithId2_whenRecipeWithId2Saved() {
        when(mockrecipesRepo.findById("2")).thenReturn(Optional.of(recipe2));

        Recipe actual = recipesService.findRecipeById("2");
        //ASSERT
        assertEquals(recipe2, actual);

    }
    @Test
    void postRecipe_returnRecipeWithId2_whenRecipeWithId2Saved() {
        RecipeDto newRecipe=new RecipeDto( "a", "a", 12, "a",
                "a", Status.FAVORITE, ingredients);
        when(mockrecipesRepo.save(any(Recipe.class))).thenReturn(recipe2);
        Recipe actual =recipesService.saveRecipes(newRecipe);
        assertEquals(recipe2, actual);
    }

    @Test
    void deleteRecipe_shouldDeleteRecipeWithValidId() {
        String id = "2";
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.of(recipe2));
       recipesService.deleteRecipe(id);

        verify(mockrecipesRepo, times(1)).deleteById(id);
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> recipesService.findRecipeById(id));
    }

    @Test
    void updateRecipe_shouldUpdateRecipeWithValidId() {
        String id = "2";
        RecipeDto updatedRecipe=new RecipeDto( "a", "a", 20, "a",
                "a", Status.FAVORITE, ingredients);
        Recipe newRecipe=new Recipe( id,"a", "a", 20, "a",
                "a", Status.FAVORITE, ingredients);
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.of(recipe2));
        when(mockrecipesRepo.save(any(Recipe.class))).thenReturn(newRecipe);
       Recipe expected = recipesService.updateRecipe(id, updatedRecipe);
       assertEquals(newRecipe, expected);
    }
}