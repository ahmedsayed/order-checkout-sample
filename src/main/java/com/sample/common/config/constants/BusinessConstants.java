package com.sample.common.config.constants;

import java.math.BigDecimal;

public class BusinessConstants {

    public static final int MIN_ITEM_QUANTITY_IN_ORDER = 1;
    public static final int MAX_ITEM_QUANTITY_IN_ORDER = 100; //assumption

    public static final BigDecimal MIN_ORDER_TOTAL_AMOUNT = BigDecimal.valueOf(100);

    public static final BigDecimal ORDER_TOTAL_AMOUNT_FRAUD_THRESHOLD = BigDecimal.valueOf(1500);
}
