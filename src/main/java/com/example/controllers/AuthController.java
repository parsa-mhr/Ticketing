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
import org.springframework.web.bind.annotation.*;

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
     ShahkarResponse response = new ShahkarResponse();//shahkarService.verify(authDto.getPhone() , authDto.getNational_id());
        response.setData(true);
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
               return ResponseEntity.ok().body(Map.of("token" , jwtUtil.generateToken(username) ,
                    "username" , username   ));
           else
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (Exception e){
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping ("/homescreen")
    public ResponseEntity<?> mainpage(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = null;
        if (jwtUtil.validateToken(token)){
//            User user = userRepository.findByEmail(jwtUtil.extractEmail(token)).orElseThrow();
//            int coins = userService.logincoinhandling(user);
//            long remain =  user.getRemainsSub();

            return ResponseEntity.ok(jwtUtil.extractUsername(token));
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
