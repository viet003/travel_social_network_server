package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dto.auth.LoginDto;
import api.v1.travel_social_network_server.dto.auth.RegisterDto;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.auth.LoginResponse;
import api.v1.travel_social_network_server.reponses.auth.RegisterResponse;
import api.v1.travel_social_network_server.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-url}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponse>> register(@RequestBody RegisterDto registerDto) {

        System.out.println(registerDto);
        try {
            return ResponseEntity.ok(Response.<RegisterResponse>builder()
                    .status(200)
                    .data(authService.registerService(registerDto))
                    .message("Register successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<RegisterResponse>builder()
                    .status(400)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(Response.<LoginResponse>builder()
                    .status(200)
                    .data(authService.loginService(loginDto))
                    .message("Login successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.<LoginResponse>builder()
                    .status(400)
                    .message(e.getMessage())
                    .build());
        }
    }
}
