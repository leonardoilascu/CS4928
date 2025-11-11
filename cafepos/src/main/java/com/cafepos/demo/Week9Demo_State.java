package com.cafepos.demo;

import com.cafepos.state.OrderFSM;

public final class Week9Demo_State {
        public static void main(String[] args) {
        OrderFSM fsm = new OrderFSM();
        System.out.println("Status = " + fsm.status());
        fsm.prepare(); // invalid before pay
        fsm.pay(); // NEW -> PREPARING
        fsm.prepare(); // still preparing
        fsm.markReady(); // PREPARING -> READY
        fsm.deliver(); // READY -> DELIVERED
        System.out.println("Status = " + fsm.status());
    }
}