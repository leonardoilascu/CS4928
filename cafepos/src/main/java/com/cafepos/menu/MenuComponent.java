package com.cafepos.menu;
import com.cafepos.common.Money;
import java.util.Iterator;
public abstract class MenuComponent {

    public void add(MenuComponent c) { throw new UnsupportedOperationException(); }
    public void remove(MenuComponent c) { throw new UnsupportedOperationException(); }
    public MenuComponent getChild(int i) { throw new UnsupportedOperationException(); }

    public String name() { throw new UnsupportedOperationException(); }
    public Money price() { throw new UnsupportedOperationException(); }
    public boolean vegetarian() { return false; }

    public Iterator<MenuComponent> iterator() { throw new UnsupportedOperationException(); }
    public void print() { throw new UnsupportedOperationException(); }
}