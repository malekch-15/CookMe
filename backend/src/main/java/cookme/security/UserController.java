package cookme.security;

import cookme.recipesmodel.Recipe;
import cookme.services.AppUserService;
import cookme.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {
public final AppUserService appUserService;
   @GetMapping()
   public AppUser getCurrentUserDetails(@AuthenticationPrincipal OAuth2User user) {
       String name = user.getName()!= null ? user.getName() : "anonymousUser";
       String login = user.getAttribute("login");
       String avatarUrl = user.getAttribute("avatar_url");
       AppUser currentUser = appUserService.getUserById(name);
       List<Recipe> favorites = appUserService.getUserFavorites(currentUser.id());
       System.out.println(user.getAttributes());
       return new AppUser(
               name,
               login,
               avatarUrl,
               currentUser.ingredient(),
               favorites
       );
   }
}
