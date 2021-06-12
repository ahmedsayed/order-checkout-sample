package com.sample.orders.unit.services.impl;

import com.sample.common.domains.User;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.exceptions.ResourceNotFoundException;
import com.sample.orders.repositories.UsersRepository;
import com.sample.orders.unit.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<User> findUsers() {
        return this.usersRepository.findAll();
    }

    @Override
    public User findUser(long userId) {
        Optional<User> exitingUser = this.usersRepository.findById(userId);

        if (exitingUser.isPresent()) {
            return exitingUser.get();
        } else {
            throw new ResourceNotFoundException("Record not found with id: " + userId);
        }
    }

    @Override
    public User isValidUser(User user) throws BusinessException {

        if (user == null)
            throw new BusinessException("missing-user", "Invalid User");

        try {
            User retrievedUser = findUser(user.getId());

            // check for user validity (active, etc..)

            return retrievedUser;
        } catch (ResourceNotFoundException ex) {
            log.info("no user found with id {}", user.getId());
            throw new BusinessException("invalid-user-id", "Invalid User");
        }
    }
}
