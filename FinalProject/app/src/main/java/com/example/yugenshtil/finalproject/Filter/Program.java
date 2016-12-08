package com.example.yugenshtil.finalproject.Filter;

/**
 * Created by Oleg Mytryniuk on 22/11/16.
 */

public class Program {

    String name;
    boolean selected = false;

    public Program(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
