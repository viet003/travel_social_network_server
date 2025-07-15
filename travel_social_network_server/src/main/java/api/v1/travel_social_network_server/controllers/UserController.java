package api.v1.travel_social_network_server.controllers;

import api.v1.travel_social_network_server.dto.user.UpdateUserImgDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.base-url}/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    public ResponseEntity<Response<?>> updateUserAvatar(
            @AuthenticationPrincipal User user,
            @ModelAttribute UpdateUserImgDto updateUserImgDto
    ) throws IOException {
        String imgUrl = userService.updateUserImg(updateUserImgDto, user);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(imgUrl)
                        .message("Update successfully")
                        .build()
        );
    }
}
