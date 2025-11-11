package com.cafepos.state;

final class DeliveredState implements State {
    @Override public void pay(OrderFSM ctx) { System.out.println("[State] Completed"); }
    @Override public void prepare(OrderFSM ctx) { System.out.println("[State] Completed"); }
    @Override public void markReady(OrderFSM ctx) { System.out.println("[State] Completed"); }
    @Override public void deliver(OrderFSM ctx) { System.out.println("[State] Already delivered"); }
    @Override public void cancel(OrderFSM ctx) { System.out.println("[State] Completed"); }
    @Override public String name() { return "DELIVERED"; }
}