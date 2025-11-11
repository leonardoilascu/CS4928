package com.cafepos.menu;
import java.util.*;
import java.util.stream.Collectors;

public final class Menu extends MenuComponent {
    private final String name;
    private final List<MenuComponent> children = new ArrayList<>();
    public Menu(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        this.name = name;
    }
    @Override public void add(MenuComponent c) { children.add(c); }
    @Override public void remove(MenuComponent c) { children.remove(c); }
    @Override public MenuComponent getChild(int i) { return children.get(i); }
    @Override public String name() { return name; }
    // Expose child iterator for CompositeIterator
    public Iterator<MenuComponent> childrenIterator() { return children.iterator(); }
    @Override public Iterator<MenuComponent> iterator() {
        return new CompositeIterator(childrenIterator());
    }
    @Override public void print() {
        System.out.println(name);
        for (MenuComponent c : children) c.print();
    }
    public List<MenuComponent> allItems() {
        List<MenuComponent> out = new ArrayList<>();
        var it = iterator();
        while (it.hasNext()) out.add(it.next());
        return out;
    }
    public List<MenuItem> vegetarianItems() {
        return allItems().stream()
        .filter(mc -> mc instanceof MenuItem mi && mi.vegetarian())
        .map(mc -> (MenuItem) mc)
        .collect(Collectors.toList());
    }
}