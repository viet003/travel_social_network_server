package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.dto.user.UpdateUserImgDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.responsitories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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
        return userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    @Transactional
    public String updateUserImg(UpdateUserImgDto updateUserImgDto, User user) throws IOException {
        User ex_user = userRepository.findById(user.getId()).orElseThrow(() -> new BadCredentialsException("User not found"));
        String imgUrl = "";

        if (updateUserImgDto.getAvatarImg() != null) {
            imgUrl = cloudinaryService.upload(updateUserImgDto.getAvatarImg().getBytes(), AVATAR_FOLDER);
            ex_user.setAvatarImg(imgUrl);
            log.info("Avatar url: {}", imgUrl);
            userRepository.save(user);
        }

        if (updateUserImgDto.getCoverImg() != null) {
            imgUrl = cloudinaryService.upload(updateUserImgDto.getCoverImg().getBytes(), COVER_FOLDER);
            ex_user.setAvatarImg(imgUrl);
            log.info("Cover url: {}", imgUrl);
            userRepository.save(user);
        }

        return imgUrl;
    }


}
