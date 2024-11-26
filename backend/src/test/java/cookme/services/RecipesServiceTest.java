package cookme.services;

import cookme.recipesmodel.Favorite;
import cookme.recipesmodel.Recipes;
import cookme.recipesmodel.RecipesModelDto;
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
    void addRecipe_returnTheGivenRecipe_whenRecipeSaved() {
        Recipes addRecipe = new Recipes(null, "a", "a", 12, "a",
                "a", Favorite.FAVORITE, List.of("a", "b", "c"));
        Recipes expectedRecipe = addRecipe.withId("3");
        when(mockrecipesRepo.save(any(Recipes.class))).thenReturn(expectedRecipe);
        Recipes actual = recipesService.addRecipes(addRecipe);
        //ASSERT
        assertEquals(expectedRecipe, actual);
        verify(mockrecipesRepo, times(1)).save(any(Recipes.class));
    }

    @Test
    void updateRecipe_returnUpdatedRecipe_whenRecipeSaved() {
        //GIVEN
        String id = "3";
        Recipes existRecipe = new Recipes("3", "a", "a", 12, "a", "a"
                , Favorite.FAVORITE, List.of("a", "b", "c"));

        RecipesModelDto givenRecipe = new RecipesModelDto("aa", "a", 12, "a",
                "a", Favorite.FAVORITE, List.of("a", "b", "c"));
        Recipes updatedRecipe = new Recipes("3", "aa", "a", 12, "a",
                "a", Favorite.FAVORITE, List.of("a", "b", "c"));
        //WHEN
        when(mockrecipesRepo.findById(id)).thenReturn(Optional.of(existRecipe));
        when(mockrecipesRepo.save(any(Recipes.class))).thenReturn(updatedRecipe);
        //THEN
        Recipes expected = recipesService.editRecipe(id, givenRecipe);

        // Assert
        assertEquals(expected, updatedRecipe);
        assertNotNull(expected);
        assertEquals(id, expected.id());
        assertEquals(givenRecipe.name(), expected.name());
        assertEquals(givenRecipe.description(), expected.description());
        verify(mockrecipesRepo, times(1)).findById(id);
        verify(mockrecipesRepo, times(1)).save(any(Recipes.class));

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