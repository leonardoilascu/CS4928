package com.cafepos.state;

final class ReadyState implements State {
    @Override public void pay(OrderFSM ctx) { System.out.println("[State] Already paid"); }
    @Override public void prepare(OrderFSM ctx) { System.out.println("[State] Already prepared"); }
    @Override public void markReady(OrderFSM ctx) { System.out.println("[State] Already ready"); }
    @Override public void deliver(OrderFSM ctx) { System.out.println("[State] Delivered"); ctx.set(new DeliveredState()); }
    @Override public void cancel(OrderFSM ctx) { System.out.println("[State] Cannot cancel after ready"); }
    @Override public String name() { return "READY"; }
}
