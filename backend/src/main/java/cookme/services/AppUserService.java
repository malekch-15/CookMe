package cookme.services;


import cookme.exception.UserNotFoundException;
import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.repository.AppUserRepo;
import cookme.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class AppUserService {

        private final AppUserRepo appUserRepo;

        public AppUser getUserById(String userId) {
            return appUserRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }
        //favorite

        public List<Recipe> getUserFavorites(String userId) {
            AppUser user = getUserById(userId);
            return user.favorites();
        }

        public void addRecipeToFavorites(String userId, Recipe recipe) {
            AppUser user = getUserById(userId);

            if (!user.favorites().contains(recipe)) {
                user.favorites().add(recipe);
                appUserRepo.save(user);
            }
        }

        public void removeRecipeFromFavorites(String userId, Recipe recipe ){
            AppUser user = getUserById(userId);

            if (user.favorites().contains(recipe)) {
                user.favorites().remove(recipe);
                appUserRepo.save(user);
            }
        }
        //Ingredient
        public List<RecipeIngredient> getUserIngredient(String userId) {
            AppUser user = getUserById(userId);
            return user.ingredient();
        }
        @Transactional
        public void addIngredientToUser(String userId, BaseIngredient ingredient, String quantity) {
            AppUser user = getUserById(userId);

            // Check if the ingredient already exists in the user's ingredient list
            RecipeIngredient existingIngredient = user.ingredient().stream()
                    .filter(recipeIngredient -> recipeIngredient.ingredient().id().equals(ingredient.id()))
                    .findFirst()
                    .orElse(null);

            if (existingIngredient != null) {
                // update the quantity by adding the new quantity
                user.ingredient().remove(existingIngredient);
                user.ingredient().add(new RecipeIngredient(existingIngredient.quantity() + quantity, ingredient));
            } else {
                //  add ingredient with the given quantity
                user.ingredient().add(new RecipeIngredient(quantity, ingredient));
            }

            // Save the updated user object
            appUserRepo.save(user);
        }

        public void removeIngredientFromUser(String userId, BaseIngredient ingredient) {
            AppUser user = getUserById(userId);
            user.ingredient().stream()
                    .filter(recipeIngredient -> recipeIngredient.ingredient().id().equals(ingredient.id()))
                    .findFirst()
                    .ifPresent(existingIngredient -> user.ingredient().remove(existingIngredient));
            appUserRepo.save(user);
        }


    }

