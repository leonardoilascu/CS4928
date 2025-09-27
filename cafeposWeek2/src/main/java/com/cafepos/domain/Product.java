package com.cafepos.domain;

import com.cafepos.common.Money;


public interface Product {
    String id();
    String name();
    Money basePrice();
}