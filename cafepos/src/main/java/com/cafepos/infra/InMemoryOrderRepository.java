package com.cafepos.infra;

import com.cafepos.domain.Order;
import com.cafepos.domain.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }
}
