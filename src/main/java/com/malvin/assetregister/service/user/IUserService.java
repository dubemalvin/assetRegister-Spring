package com.malvin.assetregister.service.user;

import com.malvin.assetregister.entity.User;
import com.malvin.assetregister.request.CreateUserReq;
import com.malvin.assetregister.request.LoginRequest;
import com.malvin.assetregister.request.UpdateUserRequest;

import java.util.List;

public interface IUserService {
    User signUp(CreateUserReq request);
    User login(LoginRequest request);
    List<User> getAllUsers();
    User getUserById(Long userId);
    void deleteUser(Long userId);
    User updateUser(Long userId, UpdateUserRequest request);

    User getAuthenticatedUser();
}
