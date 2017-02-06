package com.eyereturn.controller;


import com.eyereturn.domain.AverageAge;
import com.eyereturn.model.UserModel;
import com.eyereturn.service.UserService;

import org.apache.catalina.connector.Response;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chemcee. M. C on 02-02-2017.
 */
@RestController
public class UserController {

    /**
     *  static declarations
     */
    static final Logger logger = Logger.getLogger(UserController.class);

    static final String USER_EXISTS = "User with the email already exists.";
    static final String USER_CREATED = "User created.";
    static final String USER_CREATION_ERROR = "Error while creating user. Try again later.";
    static final String USER_INPUT_ERROR = "Unexpected input";
    static final String USERS_NOT_FOUND = "No users found in database";
    static final String USERS_FOUND = "Users found in database";
    static final String USER_REMOVED = "User successfully removed";
    static final String USER_NOT_FOUND = "User not found";
    static final String SERVER_ERROR = "Internal Error. Try again later.";
    static final String INVALID_EMAIL = "Invalid email";

    @Autowired
    UserService userService;

    List<UserModel> userModels;

    /**
     * This method fetches the list of all users present in the system.
     * @return
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

    /**
     *  Method to create a new user.
     *  Upon successful creation, the user details are passed back to the client.
     *  For any exceptions, the client entered details and exception message is sent back to the client
     * @param userModel
     * @return
     */
    @RequestMapping(value = "/users/new", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> CreateUser(@RequestBody UserModel userModel)
    {
        Map<String, Object> userMap = new HashMap<String, Object>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        if(userModel != null)
        {
            try {
                if(EmailValidator.getInstance().isValid(userModel.getEmail()))  //check if an email is valid or not. Uses apache commons validator
                {
                    if(!userService.getUserByEmail(userModel.getEmail()))       //check if the email is already taken in the system
                    {
                        userModel = userService.addNewUser(userModel);          //call to the service layer to add the user to the database
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
                        return new ResponseEntity<>(userMap, headers,HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    userMap.put("user", userModel);
                    userMap.put("message", INVALID_EMAIL);
                    return new ResponseEntity<>(userMap, headers,HttpStatus.BAD_REQUEST);
                }
            }  catch (Exception e) {
                userMap.put("message",USER_CREATION_ERROR);
                userMap.put("user", userModel);
                return new ResponseEntity<>(userMap, headers,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            userMap.put("message",USER_INPUT_ERROR );
            userMap.put("user", userModel);
            return new ResponseEntity<>(userMap, headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***
     * Method to delete a user based on the userid (Long)
     * @param userId
     * @return
     */
    @RequestMapping(value = "/users/remove/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable long userId)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        Map<String, String>  userRemoval = new HashMap<>();
        try
        {
            if(userService.isUserExisting(userId))
            {
                if(userService.deleteUser(userId))
                {
                    userRemoval.put("message", USER_REMOVED);
                    return new ResponseEntity<>(userRemoval, headers, HttpStatus.NO_CONTENT);
                }else
                {
                    userRemoval.put("message", SERVER_ERROR);
                    return new ResponseEntity<>(userRemoval, headers, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else
            {
                userRemoval.put("message", USER_NOT_FOUND);
                return new ResponseEntity<>(userRemoval, headers,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            userRemoval.put("message", SERVER_ERROR);
            return new ResponseEntity<>(userRemoval, headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/users/age",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<AverageAge> getAverageAge(@RequestBody AverageAge averageAge)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        try{
            int age = userService.findAverageAge(averageAge);
            if(age > 0)
            {
                averageAge.setAge(age);
                return new ResponseEntity<>(averageAge, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(averageAge, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e)
        {
            return new ResponseEntity<>(averageAge, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
