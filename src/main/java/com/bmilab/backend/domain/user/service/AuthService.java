package com.bmilab.backend.domain.user.service;

import com.bmilab.backend.domain.leave.entity.UserLeave;
import com.bmilab.backend.domain.leave.repository.UserLeaveRepository;
import com.bmilab.backend.domain.user.dto.request.LoginRequest;
import com.bmilab.backend.domain.user.dto.request.RegisterUserRequest;
import com.bmilab.backend.domain.user.dto.response.LoginResponse;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.domain.user.exception.UserErrorCode;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.jwt.TokenProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserLeaveRepository userLeaveRepository;

    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException(UserErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = tokenProvider.generateToken(user, Duration.ofDays(30));
        Role role = user.getRole();

        return LoginResponse.of(accessToken, role);
    }

    @Transactional
    public void registerNewUser(RegisterUserRequest request) {
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .department(request.department())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        UserLeave userLeave = UserLeave.builder()
                .user(user)
                .annualLeaveCount(request.annualLeaveCount())
                .usedLeaveCount(request.usedLeaveCount())
                .build();

        userLeaveRepository.save(userLeave);
    }
}
