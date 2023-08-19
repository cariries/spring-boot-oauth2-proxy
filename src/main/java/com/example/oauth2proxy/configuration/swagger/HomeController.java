package com.example.oauth2proxy.configuration.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {
    
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/"
    )
    public String index() {
        return "redirect:swagger-ui.html";
    }

}