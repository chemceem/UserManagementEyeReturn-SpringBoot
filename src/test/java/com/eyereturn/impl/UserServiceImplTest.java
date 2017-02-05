package com.eyereturn.impl;

import com.eyereturn.service.UserService;
import com.eyereturn.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Chemcee. M. C on 03-02-2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    public void getAllUsers() throws Exception {

    }

    @Test
    public void getUserByEmail() throws Exception {
        boolean status = userService.getUserByEmail("chemceem@gmail.com");
        assertThat(status).isEqualTo(true);

        status = userService.getUserByEmail("chemcee@gmail.com");
        assertThat(status).isEqualTo(false);
    }

    @Test
    public void deleteUser() throws Exception {

    }

    @Test
    public void checkDateFormat() throws Exception
    {
        Date date = UserServiceImpl.convertToDate("25-13-2016");
        //assertThat(date).isEqualTo(java.sql.Date(new Date()));
    }


}