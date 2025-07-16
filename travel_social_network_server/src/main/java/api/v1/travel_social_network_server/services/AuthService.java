package api.v1.travel_social_network_server.services;

import api.v1.travel_social_network_server.components.JwtGenerator;
import api.v1.travel_social_network_server.dto.auth.LoginDto;
import api.v1.travel_social_network_server.dto.auth.RegisterDto;
import api.v1.travel_social_network_server.entities.User;
import api.v1.travel_social_network_server.entities.UserProfile;
import api.v1.travel_social_network_server.exceptions.ResourceAlreadyExisted;
import api.v1.travel_social_network_server.reponses.auth.LoginResponse;
import api.v1.travel_social_network_server.reponses.auth.RegisterResponse;
import api.v1.travel_social_network_server.responsitories.UserRepository;
import api.v1.travel_social_network_server.utilities.GenderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    @Transactional
    public RegisterResponse registerService(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResourceAlreadyExisted("User already existed with this email");
        }

        UserProfile userProfile = UserProfile.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .dateOfBirth(registerDto.getDateOfBirth())
                .gender(registerDto.getGender().toLowerCase().trim().equals("male") ? registerDto.getGender().toLowerCase().trim().equals("female") ? GenderEnum.FEMALE : GenderEnum.MALE : GenderEnum.MALE)
                .build();

        User user = User.builder()
                .userName(registerDto.getUserName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .userProfile(userProfile)
                .build();

        userProfile.setUser(user);  // Quan trọng: ánh xạ ngược

        System.out.println(user);

        userRepository.save(user);

        return RegisterResponse.builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public LoginResponse loginService(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not existed"));


        String jwtToken = authenticateUser(loginDto, user);

        return LoginResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .avatarImg(user.getAvatarImg())
                .coverImg(user.getCoverImg())
                .email(user.getEmail())
                .token(jwtToken)
                .role(user.getRole().name())
                .userProfile(user.getUserProfile())
                .build();
    }


    private String authenticateUser(LoginDto loginDto, User user) {
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword(),
                user.getAuthorities()
        );

        System.out.println(authenticationToken);

        authenticationManager.authenticate(authenticationToken);

        String jwtToken = jwtGenerator.generateToken(user);
        System.out.println(jwtToken);
        return jwtToken;
    }
}
