package com.sample.orders.unit.services;

import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.repositories.PartnersRepository;
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
public class PartnersServiceTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @MockBean
    private PartnersRepository partnersRepository;

    @Autowired
    private PartnersService partnersService;

    @Test
    public void isValidPartner_WithNullPartner_ThrowsException() throws Exception {

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> partnersService.isValidPartner(null),
                "Expected isValidPartner() to throw BusinessException but it didn't"
        );

        assertEquals("missing-partner", thrown.getCode());
    }

    @Test
    public void isValidPartner_WithPartnerIdNotInDatabase_ThrowsException() throws Exception {

        Long dummyId = 5L;
        Partner expectedPartner = new Partner();
        expectedPartner.setId(dummyId);

        BDDMockito.given(partnersRepository.findById(dummyId)).willReturn(Optional.empty());

        BusinessException thrown = assertThrows(
                BusinessException.class,
                () -> partnersService.isValidPartner(new Partner(dummyId)),
                "Expected isValidPartner() to throw BusinessException but it didn't"
        );

        assertEquals("invalid-partner-id", thrown.getCode());
    }

    @Test
    public void isValidPartner_WithPartnerIdInDatabase_ReturnsPartner() throws Exception {

        Long dummyId = 50L;

        Partner expectedPartner = new Partner();
        expectedPartner.setId(dummyId);

        BDDMockito.given(partnersRepository.findById(dummyId)).willReturn(Optional.of(expectedPartner));

        Partner retrievedPartner = partnersService.isValidPartner(new Partner(dummyId));

        assertEquals(expectedPartner, retrievedPartner);
    }
}
