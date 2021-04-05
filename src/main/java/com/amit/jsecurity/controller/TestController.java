package com.amit.jsecurity.controller;

import com.amit.jsecurity.JwtUtil;
import com.amit.jsecurity.UserdetailsService;
import com.amit.jsecurity.model.AuthenticationRequest;
import com.amit.jsecurity.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserdetailsService userdetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
        }catch (AuthenticationException ex){
            if(ex instanceof BadCredentialsException){
                throw new RuntimeException("UserName or Password is incorrect");
            }
        }
        final UserDetails userDetails = userdetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return new AuthenticationResponse(jwtToken);
    }

}
