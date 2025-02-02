package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/")
public class Calculator extends VerticalLayout {

    public Calculator() {


        addClassName("centered-content");

        add(new TextField("Ergebnis"));

        add(new HorizontalLayout(new Button("1"), new Button("2"), new Button("3"), new Button("*")));
        add(new HorizontalLayout(new Button("4"), new Button("5"), new Button("6"), new Button("-")));
        add(new HorizontalLayout(new Button("7"), new Button("8"), new Button("9"), new Button("+")));
        add(new HorizontalLayout(new Button("0"), new Button("=")));


    }
}
