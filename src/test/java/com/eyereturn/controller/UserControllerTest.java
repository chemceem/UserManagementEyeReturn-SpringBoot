package com.eyereturn.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Chemcee. M. C on 03-02-2017.
 */
public class UserControllerTest {
    @Test
    public void createUser() throws Exception {

    }

    @Test
    public void emailValidator() throws Exception {
        EmailValidator emailValidator = EmailValidator.getInstance();
        boolean status = emailValidator.isValid("chemceem@myca.ca");
        assertThat(status).isEqualTo(true);
    }

    @Test
    public void findAllUsers() throws Exception {

    }
}