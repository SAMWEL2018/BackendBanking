package com.example.transcsystem.controller;

import com.example.transcsystem.models.Transactions;
import com.example.transcsystem.security.JwtTokenService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Samwel Wafula
 * Created on July 09, 2023.
 * Time 11:20 PM
 */

@RestController
public class AuthApi {
    @Autowired
    private JwtTokenService tokenService;

    @RequestMapping("/structure")
    @Hidden
    public Object getStructure() {
        return new Transactions();
    }

    @RequestMapping(value = "/get-token", method = RequestMethod.POST)
    public Object Login() {
        return tokenService.genToken();
    }

}
