package com.sample.orders.unit.services.impl;

import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;
import com.sample.orders.exceptions.ResourceNotFoundException;
import com.sample.orders.repositories.PartnersRepository;
import com.sample.orders.unit.services.PartnersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartnersServiceImpl implements PartnersService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PartnersRepository partnersRepository;

    @Autowired
    public PartnersServiceImpl(PartnersRepository partnersRepository) {
        this.partnersRepository = partnersRepository;
    }

    @Override
    public List<Partner> findPartners() {
        return this.partnersRepository.findAll();
    }

    @Override
    public Partner findPartner(long partnerId) {
        Optional<Partner> exitingPartner = this.partnersRepository.findById(partnerId);

        if (exitingPartner.isPresent()) {
            return exitingPartner.get();
        } else {
            throw new ResourceNotFoundException("Record not found with id: " + partnerId);
        }
    }

    @Override
    public Partner isValidPartner(Partner partner) throws BusinessException {

        if (partner == null)
            throw new BusinessException("missing-partner", "Invalid Partner");

        try {
            Partner retrievedPartner = findPartner(partner.getId());

            // check for partner validity (active, etc..)

            return retrievedPartner;
        } catch (ResourceNotFoundException ex) {
            log.info("no partner found with id {}", partner.getId());
            throw new BusinessException("invalid-partner-id", "Invalid Partner");
        }
    }
}
