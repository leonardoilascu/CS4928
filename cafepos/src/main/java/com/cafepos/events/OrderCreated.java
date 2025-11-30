package com.cafepos.events;

public record OrderCreated(long orderId) implements OrderEvent { }
