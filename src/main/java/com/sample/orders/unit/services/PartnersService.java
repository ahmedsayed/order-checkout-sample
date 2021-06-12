package com.sample.orders.unit.services;


import com.sample.common.domains.Partner;
import com.sample.common.exceptions.BusinessException;

import java.util.List;

public interface PartnersService {
    
    List<Partner> findPartners();

    Partner findPartner(long partnerId);

    Partner isValidPartner(Partner partner) throws BusinessException;
}
