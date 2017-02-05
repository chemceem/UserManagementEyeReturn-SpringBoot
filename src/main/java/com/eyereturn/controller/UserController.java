package com.eyereturn.controller;

import com.eyereturn.exceptions.DuplicateEmailException;
import com.eyereturn.model.UserModel;
import com.eyereturn.service.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chemcee. M. C on 02-02-2017.
 */
@RestController
public class UserController {

    static final String USER_EXISTS = "User with the email already exists.";
    static final String USER_CREATED = "User created.";
    static final String USER_CREATION_ERROR = "Error while creating user. Try again later.";
    static final String USER_INPUT_ERROR = "Unexpected input";
    static final String USERS_NOT_FOUND = "No users found in database";
    static final String USERS_FOUND = "Users found in database";
    static final String SERVER_ERROR = "Internal Error. Try again later.";

    @Autowired
    UserService userService;

    List<UserModel> userModels;

    /*
        This method fetches the list of all users present in the system.

     */
    @RequestMapping(value="/users/all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Map<String, Object>> findAllUsers()
    {
        Map<String, Object> usersMap = new HashMap<String, Object>();

        userModels = new ArrayList<UserModel>();
        HttpHeaders headers = new HttpHeaders();
        try {
            userModels = userService.getAllUsers();

            headers.add("Content-Type", "application/json; charset=UTF-8");
            if(userModels != null && !userModels.isEmpty())
            {
                usersMap.put("message", userModels.size()+ " "+USERS_FOUND);
                usersMap.put("users", userModels);
                return new ResponseEntity<>(usersMap,headers, HttpStatus.OK);
            } else {
                usersMap.put("message",USERS_NOT_FOUND);
                usersMap.put("users", null);
                return new ResponseEntity<>(usersMap, headers, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e)
        {
            usersMap.put("message ", SERVER_ERROR);
            usersMap.put("users", userModels);
            return new ResponseEntity<>(usersMap,headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String, Object>> CreateUser(@RequestBody UserModel userModel)
    {
        Map<String, Object> userMap = new HashMap<String, Object>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/userMap; charset=UTF-8");

        if(userModel != null)
        {
            try {
                if(!userService.getUserByEmail(userModel.getEmail()))
                {
                    userModel = userService.addNewUser(userModel);
                    if(userModel != null)
                    {
                        userMap.put("message",USER_CREATED);
                        userMap.put("user", userModel);
                        return new ResponseEntity<>(userMap, headers,HttpStatus.CREATED);
                    }
                    else
                    {
                        userMap.put("message",USER_CREATION_ERROR);
                        userMap.put("user", userModel);
                        return new ResponseEntity<>(userMap, headers,HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                else
                {
                    userMap.put("user", userModel);
                    userMap.put("message", USER_EXISTS);
                    return new ResponseEntity<Map<String, Object>>(userMap, headers,HttpStatus.BAD_REQUEST);
                }
            }  catch (Exception e) {
                userMap.put("message",USER_CREATION_ERROR);
                userMap.put("user", userModel);
                return new ResponseEntity<Map<String, Object>>(userMap, headers,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            userMap.put("message",USER_INPUT_ERROR );
            userMap.put("user", userModel);
            return new ResponseEntity<Map<String, Object>>(userMap, headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@RequestMapping(value = "/users/remove/{userId}", method = RequestMethod.DELETE)


}
