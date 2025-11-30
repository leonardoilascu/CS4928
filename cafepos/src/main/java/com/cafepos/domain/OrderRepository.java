// com/cafepos/domain/OrderRepository.java
package com.cafepos.domain;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(long id);
}
