package cookme.controller;

import cookme.user.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static javax.management.Query.attr;
import static javax.management.Query.value;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    void testGetUser_withLoggedInUser_getUserName() throws Exception {
        AppUser appUser=new AppUser("user", "johndoe123", "https://example.com/avatar.jpg", List.of(),List.of());
        Map<String, Object> userInfoMap = Map.of(
                "name", appUser.id(),
                "login", appUser.username(),
                "avatar_url", appUser.avatarUrl()
        );
        OidcUserInfo userInfo = new OidcUserInfo(userInfoMap);

        mvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .with(oidcLogin().userInfoToken(token -> {
                    // Add claims to the token
                    token.claim("login", userInfo.getClaims().get("login"));
                    token.claim("name", userInfo.getClaims().get("name"));
                    token.claim("avatar_url", userInfo.getClaims().get("avatar_url"));
                })))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                                               {
                                               id:"user",
                                               username:"johndoe123",
                                               avatarUrl:"https://example.com/avatar.jpg"}
                                               
"""

                ));


    }

    @Test
    void testGetMe_withoutLogin_expectAnonymousUsername() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users/me"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("anonymousUser"));
    }
}
