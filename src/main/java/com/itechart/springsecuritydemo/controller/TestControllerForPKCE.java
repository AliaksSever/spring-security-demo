package com.itechart.springsecuritydemo.controller;


import com.itechart.springsecuritydemo.service.CodeGeneration;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/pkce")
public class TestControllerForPKCE {
    private final CodeGeneration verifyCodeGeneration;

    @GetMapping("/generate")
    public Map<String, String> generatePkce() throws Exception {
        String verifier = verifyCodeGeneration.generateCodeVerifier();
        String challenge = verifyCodeGeneration.generateCodeChallange(verifier);

        return Map.of(
                "code_verifier", verifier,
                "code_challenge", challenge,
                "code_challenge_method", "S256"
        );
    }

}

