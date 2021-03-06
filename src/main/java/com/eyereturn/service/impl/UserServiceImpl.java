package com.eyereturn.service.impl;

import com.eyereturn.dao.UserDao;
import com.eyereturn.domain.AverageAge;
import com.eyereturn.domain.User;
import com.eyereturn.exceptions.DuplicateEmailException;
import com.eyereturn.model.UserModel;
import com.eyereturn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chemcee. M. C on 02-02-2017.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    List<UserModel> userModels;

    @Override
    public List<UserModel> getAllUsers()
    {
        userModels = new ArrayList<UserModel>();
        List<User> users;
        try
        {
            users = userDao.findAll();  //fetch all the users present in the system.
            if(users != null && !users.isEmpty())
            {
                for(User user : users)
                {
                    UserModel userModel = new UserModel();

                    userModel.setId(user.getId());
                    userModel.setFirstName(user.getFirstName());
                    userModel.setLastName(user.getLastName());
                    userModel.setPhoneNumber(user.getPhoneNumber());
                    userModel.setEmail(user.getEmailAddress());
                    userModel.setDateOfBirth(user.getDateOfBirth().toString());

                    userModels.add(userModel);
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return userModels;
    }

    @Override
    @Transactional
    public UserModel addNewUser(UserModel userModel)
    {
        try {
            if(userModel != null) {

                Date birthDate = convertToDate(userModel.getDateOfBirth());
                User user = new User();

                user.setFirstName(userModel.getFirstName());
                user.setLastName(userModel.getLastName());
                user.setPhoneNumber(userModel.getPhoneNumber());
                user.setEmailAddress(userModel.getEmail());
                user.setDateOfBirth(birthDate);

                user = userDao.save(user);
                userModel.setId(user.getId());

                return userModel;
            }
            else
            {
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /*
        Method to check if a user with an email already exist in the system.
        Returns true if present.
     */
    @Override
    public boolean getUserByEmail(String email)
    {
        try
        {
            if(userDao.findByEmailAddress(email) == null)
            {
                //return false if no user is present with the email address
                return false;
            }
            else
            {
                throw new DuplicateEmailException("An account exists with the email provided");
            }
        }catch (Exception e)
        {
            return true;
        }
    }

    @Override
    public boolean deleteUser(Long id)
    {
        try
        {
            userDao.delete(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    /*
        service level method.
        Check if a user with the 'id' is present in the system.
     */
    @Override
    public boolean isUserExisting(Long id)
    {
        try {
            return userDao.exists(id);
        }catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public int findAverageAge(AverageAge averageAge)
    {
        List<User> allUsers;
        int ageAverage;
        int ageSum = 0;
        try {

            Date fromDate = convertToDate(averageAge.getFromDate());
            Date toDate = convertToDate(averageAge.getToDate());

            allUsers = userDao.findByDateOfBirthBetween(fromDate, toDate);
            if(allUsers != null && !allUsers.isEmpty())
            {
                int userCount = allUsers.size();
                for(User user: allUsers)
                {
                    int age = CalculateAge(user.getDateOfBirth());
                    ageSum+= age;
                }

                ageAverage = ageSum/userCount;
                //ageAverage = Math.round(ageAverage * 100d) / 100d;
                return ageAverage;
            } else
            {
                return -1;
            }
        } catch (Exception e){
            return 0;
        }
    }

    public static Date convertToDate(String date) throws ParseException
    {
        //regex to check the date is in dd/MM/yyyy format
        date = date.replaceAll("[./']","-");
        String datePattern = "(0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](19|20)\\d\\d";
        Matcher match = Pattern.compile(datePattern).matcher(date);
        if(match.matches())
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date javaDate = dateFormat.parse(date);
            Date finalDate = new Date(javaDate.getTime());

            return finalDate;
        } else {
            return null;
        }
    }

    public int CalculateAge(Date dateOfBirth)
    {
        return Period.between(dateOfBirth.toLocalDate(), LocalDate.now()).getYears();
    }
}