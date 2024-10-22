package com.malvin.assetregister.service.user;

import com.malvin.assetregister.entity.User;
import com.malvin.assetregister.enums.Role;
import com.malvin.assetregister.exception.AlreadyExistsException;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.UserRepository;
import com.malvin.assetregister.request.CreateUserReq;
import com.malvin.assetregister.request.LoginRequest;
import com.malvin.assetregister.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signUp(CreateUserReq request) {
        return Optional.of(request).filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req ->{
                        User user = new User();
                        user.setFirstName(request.getFirstName());
                        user.setLastName(request.getLastName());
                        user.setEmail(request.getEmail());
                        user.setPassword(passwordEncoder.encode(request.getPassword()));
                        user.setRole(Role.valueOf(request.getRole()));
                        return userRepository.save(user);
                })
                .orElseThrow(()-> new AlreadyExistsException("User with " + request.getEmail() + "already Exists"));
    }

    @Override
    public User login(LoginRequest request) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("user with " + userId + " not found")
        );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                ()-> {throw new ResourceNotFoundException("User not Found");
                });
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        return userRepository.findById(userId).map(user->{
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            return userRepository.save(user);
        }).orElseThrow(
                        ()->  new ResourceNotFoundException("user not found"));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
