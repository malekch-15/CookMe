package cookme.controller;

import cookme.recipesmodel.BaseIngredient;
import cookme.recipesmodel.Recipe;
import cookme.recipesmodel.RecipeIngredient;
import cookme.recipesmodel.Status;
import cookme.services.AppUserService;
import cookme.user.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;


@SpringBootTest
@AutoConfigureMockMvc
 class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    AppUserService appUserService;

    @Test

    void testGetUser_withLoggedInUser_getUserDetails() throws Exception {
        // Arrange
        AppUser appUser = new AppUser(
                "user",
                "johndoe123",
                "https://example.com/avatar.jpg",
                List.of(new RecipeIngredient(2,new BaseIngredient("1","2"))),
                List.of(new Recipe("1", "a", "a", 12, "a", "a", Status.FAVORITE, List.of()))
        );

        when(appUserService.getUserById("user")).thenReturn(appUser);
        when(appUserService.getUserFavorites("user")).thenReturn(appUser.favorites());
        when(appUserService.getUserIngredient("user")).thenReturn(appUser.ingredient());


        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .with(oidcLogin().userInfoToken(token -> {
                            token.claim("login", "johndoe123");
                            token.claim("name", "user");
                            token.claim("avatar_url", "https://example.com/avatar.jpg");
                        })))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
               {
                      "id": "user",
                      "username": "johndoe123",
                      "avatarUrl": "https://example.com/avatar.jpg",
                     "ingredient":[{"quantity":2.0,"ingredient":{"id":"1","name":"2"}}],
                     "favorites":[{"id":"1","name":"a","description":"a","time":12.0,"imageUrl":"a","preparation":"a","status":"FAVORITE",
                     "ingredients":[]}]}
            """));
    }

}