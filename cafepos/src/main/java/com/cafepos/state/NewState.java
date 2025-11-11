package com.cafepos.state;

final class NewState implements State {
    @Override public void pay(OrderFSM ctx) { System.out.println("[State] Paid → Preparing"); ctx.set(new PreparingState()); }
    @Override public void prepare(OrderFSM ctx) { System.out.println("[State] Cannot prepare before pay"); }
    @Override public void markReady(OrderFSM ctx) { System.out.println("[State] Not ready yet"); }
    @Override public void deliver(OrderFSM ctx) { System.out.println("[State] Cannot deliver yet"); }
    @Override public void cancel(OrderFSM ctx) { System.out.println("[State] Cancelled"); ctx.set(new CancelledState()); }
    @Override public String name() { return "NEW"; }
}