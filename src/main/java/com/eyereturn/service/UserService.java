package com.eyereturn.service;

import com.eyereturn.exceptions.DuplicateEmailException;
import com.eyereturn.model.UserModel;

import java.util.List;

/**
 * Created by Chemcee. M. C on 02-02-2017.
 */
public interface UserService {

    List<UserModel> getAllUsers();
    UserModel addNewUser(UserModel userModel);
    boolean getUserByEmail(String email) throws DuplicateEmailException;
    boolean deleteUser(Long id);
}
