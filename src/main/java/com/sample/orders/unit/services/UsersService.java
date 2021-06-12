package com.sample.orders.unit.services;

import com.sample.common.domains.User;
import com.sample.common.exceptions.BusinessException;

import java.util.List;

public interface UsersService {
    
    List<User> findUsers();

    User findUser(long userId);

    User isValidUser(User user) throws BusinessException;
}
