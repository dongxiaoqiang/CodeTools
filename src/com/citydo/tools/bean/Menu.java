package com.citydo.tools.bean;

public class Menu {
    private String menuId;
    private String menuName;
    private String menuClass;

    public Menu() {
    }

    public String getMenuClass() {
        return this.menuClass;
    }

    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String toString() {
        return this.menuName;
    }
}
