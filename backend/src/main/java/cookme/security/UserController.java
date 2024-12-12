package cookme.security;

import cookme.user.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

//    @GetMapping()
//    public String getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
//        if (user == null) {
//         return "anonymousUser";
//        }else {
//        System.out.println(user);
//        return Objects.requireNonNull(user.getAttribute("login")).toString();}
//    }
   @GetMapping()
   public AppUser getCurrentUserDetails(@AuthenticationPrincipal OAuth2User user) {
       String name = user.getName()!= null ? user.getName() : "anonymousUser";
       String login = user.getAttribute("login");
       String avatarUrl = user.getAttribute("avatar_url");
       System.out.println(user.getAttributes());
       return new AppUser(
               name,
               login,
               avatarUrl,
               List.of(),
               List.of()
       );
   }
}
