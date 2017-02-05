package com.eyereturn.dao;

import com.eyereturn.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Chemcee. M. C on 02-02-2017.
 */
public interface UserDao extends CrudRepository<User, Long> {

    User findByEmailAddress(String email);
    User findOne(Long id);
    List<User> findAll();
    User save(User user);
}
