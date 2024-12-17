package cookme.services;

import cookme.recipesmodel.BaseIngredient;
import cookme.repository.IngredientsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest

class IngredientsServiceTest {
    private final IngredientsRepo mockIngredientsRepo=mock(IngredientsRepo.class);
    private final IdService mockIdService=mock(IdService.class);
    private final IngredientsService ingredientsService = new IngredientsService(mockIngredientsRepo, mockIdService);
    BaseIngredient ingredient1= new BaseIngredient("1", "Tomato");
    BaseIngredient ingredient2= new BaseIngredient("2", "Salt");
    @Test
    void findAll() {
        when(mockIngredientsRepo.findAll()).thenReturn(List.of(ingredient1));
        List<BaseIngredient> ingredientsExpected = ingredientsService.findAll();
        assertEquals(List.of(ingredient1), ingredientsExpected);
    }

    @Test
    void findById() {
        when(mockIngredientsRepo.findById("2")).thenReturn(Optional.of(ingredient2));
        BaseIngredient actual=ingredientsService.findById("2");
        assertEquals(ingredient2, actual);
    }

    @Test
    void findByName() {
        when(mockIngredientsRepo.findByName("Tomato")).thenReturn(List.of(ingredient1));
       List<BaseIngredient> actual=ingredientsService.findByName("Tomato");
        assertEquals(List.of(ingredient1), actual);
    }
    @Test
    void findByName_WhenIngredientNotFound_ShouldThrowException() {
        // Arrange
        String name = "NonExistingIngredient";

        when(mockIngredientsRepo.findByName(name)).thenReturn(null);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            ingredientsService.findByName(name);
        });

        assertEquals("Ingredient not found", exception.getMessage());

        verify(mockIngredientsRepo, times(1)).findByName(name); // Verify repository call
    }
    @Test
    void save() {
        BaseIngredient ingredient3 = new BaseIngredient("3", "water");

        when(mockIngredientsRepo.save(any(BaseIngredient.class))).thenReturn(ingredient3);

        BaseIngredient actual = ingredientsService.save(ingredient3);

        assertEquals(ingredient3, actual);
    }
}