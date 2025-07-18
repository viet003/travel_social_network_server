package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dto.user.UpdateUserDto;
import api.v1.travel_social_network_server.dto.user.UpdateUserImgDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.entities.UserProfile;
import api.v1.travel_social_network_server.exceptions.ResourceNotFoundException;
import api.v1.travel_social_network_server.reponses.user.UpdateUserResponse;
import api.v1.travel_social_network_server.reponses.user.UserResponse;
import api.v1.travel_social_network_server.responsitories.UserRepository;

import api.v1.travel_social_network_server.utilities.GenderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final static String AVATAR_FOLDER = "avatars";
    private final static String COVER_FOLDER = "covers";

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        return UserResponse.builder()
                .userName(user.getUsername())
                .userProfile(user.getUserProfile())
                .avatarImg(user.getAvatarImg())
                .coverImg(user.getCoverImg())
                .build();
    }


    @Transactional
    public String updateUserImg(UpdateUserImgDto updateUserImgDto, User user) throws IOException {
        User ex_user = userRepository.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String imgUrl = "";

        if (updateUserImgDto.getAvatarImg() != null) {
            imgUrl = cloudinaryService.uploadFile(updateUserImgDto.getAvatarImg().getBytes(), AVATAR_FOLDER, "image");
            ex_user.setAvatarImg(imgUrl);
            log.info("Avatar url: {}", imgUrl);
            userRepository.save(ex_user);
        }

        if (updateUserImgDto.getCoverImg() != null) {
            imgUrl = cloudinaryService.uploadFile(updateUserImgDto.getCoverImg().getBytes(), COVER_FOLDER, "image");
            ex_user.setCoverImg(imgUrl);
            log.info("Cover url: {}", imgUrl);
            userRepository.save(ex_user);
        }

        return imgUrl;
    }

    @Transactional
    public UpdateUserResponse updateUserProfile(UpdateUserDto updateUserDto, User user) throws IOException {
        User ex_user = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ex_user.setUserName(updateUserDto.getUserName());

        System.out.println(updateUserDto);

        UserProfile profile = ex_user.getUserProfile();
        profile.setFirstName(updateUserDto.getFirstName());
        profile.setLastName(updateUserDto.getLastName());
        profile.setDateOfBirth(updateUserDto.getDateOfBirth());
        profile.setLocation(updateUserDto.getLocation());
        profile.setAbout(updateUserDto.getAbout());

        String genderInput = updateUserDto.getGender().toLowerCase().trim();
        switch (genderInput) {
            case "male" -> profile.setGender(GenderEnum.MALE);
            case "female" -> profile.setGender(GenderEnum.FEMALE);
            default -> profile.setGender(GenderEnum.OTHER);
        }

        ex_user.setUserProfile(profile);
        userRepository.save(ex_user);

        return UpdateUserResponse.builder()
                .userName(ex_user.getUsername())
                .userProfile(profile)
                .build();
    }


}
