package com.sample.orders.unit.services;

import com.sample.common.domains.User;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @MockBean
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Test
    public void isValidUser_WithNullUser_ThrowsException() throws Exception {

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> usersService.isValidUser(null),
                "Expected isValidUser() to throw BusinessException but it didn't"
        );

        assertEquals("missing-user", thrown.getCode());
    }

    @Test
    public void isValidUser_WithUserIdNotInDatabase_ThrowsException() throws Exception {

        Long dummyId = 5L;
        User expectedUser = new User();
        expectedUser.setId(dummyId);

        BDDMockito.given(usersRepository.findById(dummyId)).willReturn(Optional.empty());

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> usersService.isValidUser(new User(dummyId)),
                "Expected checkoutOrder() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-user-id", thrown.getCode());
    }

    @Test
    public void isValidUser_WithUserIdInDatabase_ReturnsUser() throws Exception {

        Long dummyId = 50L;

        User expectedUser = new User();
        expectedUser.setId(dummyId);

        BDDMockito.given(usersRepository.findById(dummyId)).willReturn(Optional.of(expectedUser));

        User retrievedUser = usersService.isValidUser(new User(dummyId));

        assertEquals(expectedUser, retrievedUser);
    }
}
