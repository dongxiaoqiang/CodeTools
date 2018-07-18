package com.citydo.tools.component;

import java.awt.Component;

public interface MyComponent {
    int labelH = 19;
    int buttonH = 35;
    int cellH = 3;

    Component create();

    void resized();
}
