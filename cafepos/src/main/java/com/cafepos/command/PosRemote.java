package com.cafepos.command;

import java.util.ArrayDeque;
import java.util.Deque;

public final class PosRemote {
        private final Command[] slots;
        private final Deque<Command> history = new ArrayDeque<>();
        public PosRemote(int n) { this.slots = new Command[n]; }
        public void setSlot(int i, Command c) { slots[i] = c; }
        public void press(int i) {
            Command c = slots[i];
            if (c != null) {
                c.execute();
                history.push(c);
            } else {
                System.out.println("[Remote] No command in slot " + i);
            }
        }
        public void undo() {
        if (history.isEmpty()) { 
            System.out.println("[Remote] Nothing to undo"); 
            return; 
        }
        history.pop().undo();
    }
}
