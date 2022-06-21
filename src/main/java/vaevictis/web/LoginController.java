package vaevictis.web;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;





@Controller
@SessionAttributes
public class LoginController {

    @GetMapping(value= "/login")
    public String login(Map<String, Object> model, Principal principal) {
        if(principal == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() == "anonymousUser") {
            return "login";
        } else {
            return "redirect: ";
        }
    }

    @GetMapping("/login-error")
    public String loginError() {
        System.out.println("Error");
        return "exception";
    }

    
}
