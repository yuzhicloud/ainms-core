package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.service.AccessControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login/oauth2/code/cas")
public class OAuth2CallbackController {
    private final Logger log = LoggerFactory.getLogger(OAuth2CallbackController.class);

    @GetMapping
    public String redirectToFrontend() {
        log.debug("Redirecting to frontend");
        // return "redirect:http://localhost:8000/apList";
        return "redirect:http://localhost:8081/#/dashboard/workbench";
    }
}

