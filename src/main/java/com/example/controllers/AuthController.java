package com.example.controllers;

import com.example.dto.AuthDto;
import com.example.dto.ShahkarResponse;
import com.example.security.JwtUtil;
import com.example.services.CaptchaService;
import com.example.services.ShahkarService ;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
//@CrossOrigin(origins = "*") // Allow all origins
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private ShahkarService shahkarService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody AuthDto authDto) {
     ShahkarResponse response =  shahkarService.verify(authDto.getPhone() , authDto.getNational_id());
     if (response.getData()){
         return ResponseEntity.ok().build();
     }else {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String , String> Body) {
        String username = Body.get("username");
        String password = Body.get("password");
        try {
           if (userService.adminlogin(username , password))
               return ResponseEntity.ok().body(jwtUtil.generateToken(username));
           else
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception e){
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
