package com.cafepos.events;

import java.util.*;
import java.util.function.Consumer;

public final class EventBus {

    private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void on(Class<T> type, Consumer<T> handler) {
        handlers.computeIfAbsent(type, k -> new ArrayList<>())
                .add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(T event) {
        Class<?> type = event.getClass();
        List<Consumer<?>> list = handlers.getOrDefault(type, List.of());
        for (Consumer<?> rawHandler : list) {
            Consumer<T> handler = (Consumer<T>) rawHandler;
            handler.accept(event);
        }
    }
}
