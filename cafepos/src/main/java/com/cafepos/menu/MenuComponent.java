package com.cafepos.menu;
import com.cafepos.common.Money;
import java.util.Iterator;
public abstract class MenuComponent {
    // Composite ops (unsupported by default → safe)
    public void add(MenuComponent c) { throw new UnsupportedOperationException(); }
    public void remove(MenuComponent c) { throw new UnsupportedOperationException(); }
    public MenuComponent getChild(int i) { throw new UnsupportedOperationException(); }
    // Leaf data (unsupported by default → safe)
    public String name() { throw new UnsupportedOperationException(); }
    public Money price() { throw new UnsupportedOperationException(); }
    public boolean vegetarian() { return false; }
    // Iteration / printing hooks
    public Iterator<MenuComponent> iterator() { throw new UnsupportedOperationException(); }
    public void print() { throw new UnsupportedOperationException(); }
}