package cookme.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    @GetMapping()
    public String getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
         return "anonymousUser";
        }else {
        System.out.println(user);
        return Objects.requireNonNull(user.getAttribute("login")).toString();}
    }
    @GetMapping("/me/details")
    public Map<String, Object> getUserDetails(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return Map.of("message", "User not authenticated");
        }
        return user.getAttributes();
    }
}
