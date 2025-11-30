package com.cafepos.events;

public record OrderPaid(long orderId) implements OrderEvent { }
