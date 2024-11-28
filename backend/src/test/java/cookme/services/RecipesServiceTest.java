package cookme.services;

import cookme.recipesmodel.Favorite;
import cookme.recipesmodel.Recipes;
import cookme.repository.RecipesRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RecipesServiceTest {
    private final RecipesRepo mockrecipesRepo = mock(RecipesRepo.class);
    @InjectMocks
    private RecipesService recipesService = new RecipesService(mockrecipesRepo);

    Recipes recipe1 = new Recipes("1", "a", "a", 12, "a",
            "a", Favorite.FAVORITE, List.of("a", "b", "c"));
    Recipes recipe2 = new Recipes("2", "a", "a", 12, "a",
            "a", Favorite.FAVORITE, List.of("a", "b", "c"));
    List<Recipes> recipes = List.of(recipe1, recipe2);

    @Test
    void getAllRecipes_return2Recipe_when2RecipeSaved() {

        when(mockrecipesRepo.findAll()).thenReturn(recipes);

        List<Recipes> actual = recipesService.findAllRecipes();
        //ASSERT
        assertEquals(recipes, actual);
        verify(mockrecipesRepo, times(1)).findAll();
    }

    @Test
    void getRecipeById_returnRecipeWithId2_whenRecipeWithId2Saved() {
        when(mockrecipesRepo.findById("2")).thenReturn(Optional.of(recipe2));

        Recipes actual = recipesService.findRecipesById("2");
        //ASSERT
        assertEquals(recipe2, actual);

    }

    @Test
    void deleteRecipe_shouldDeleteRecipeWithValidId() {
        String id = "2";
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.of(recipe2));
       recipesService.deleteRecipe(id);

        verify(mockrecipesRepo, times(1)).deleteById(id);
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> recipesService.findRecipesById(id));
    }

}