package com.robobo.rbb_springboot.service;

import com.robobo.rbb_springboot.dto.SignUpRequestDto;
import com.robobo.rbb_springboot.model.User;
import com.robobo.rbb_springboot.model.UserRole;
import com.robobo.rbb_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "rbbbbb//oooo//12AVKKJEJ//SJCJEyidfkwwdxaas";

    public void registerUser(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());

        String email = requestDto.getEmail();
        String tel = requestDto.getTel();
        // 사용자 ROLE 확인
        UserRole role = UserRole.ROLE_USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ROLE_ADMIN;
        }

        User user = new User(username, password, email, tel, role);
        userRepository.save(user);
    }

}
