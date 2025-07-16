package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dto.user.UpdateUserDto;
import api.v1.travel_social_network_server.dto.user.UpdateUserImgDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.entities.UserProfile;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.reponses.user.UpdateUserResponse;
import api.v1.travel_social_network_server.reponses.user.UserResponse;
import api.v1.travel_social_network_server.services.UserService;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-url}/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<Response<?>> getUserProfile(@PathVariable UUID userId) throws IOException {

        UserResponse userResponse = userService.getUserProfile(userId);

        return ResponseEntity.ok(
                Response.<UserResponse>builder()
                        .data(userResponse)
                        .status(StatusRequestEnum.SUCCESS)
                        .message("Get user Profile Successfully")
                        .build()
        );
    }

    @PostMapping(value = "/img", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> updateUserAvatar(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdateUserImgDto updateUserImgDto
    ) throws IOException {
        String imgUrl = userService.updateUserImg(updateUserImgDto, user);

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .data(imgUrl)
                        .message("Update successfully")
                        .build()
        );
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<Response<?>> updateUserProfile(@RequestBody UpdateUserDto updateUserDto, @AuthenticationPrincipal User user) throws IOException {

        UpdateUserResponse updateUserResponse = userService.updateUserProfile(updateUserDto, user);

        return ResponseEntity.ok(
                Response.builder()
                        .status(StatusRequestEnum.SUCCESS)
                        .data(updateUserResponse)
                        .message("Update successfully")
                        .build()
        );
    }
}
