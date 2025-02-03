package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

package com.vaadin.demo.component.login;


@Route("calc")
public class calculator extends VerticalLayout {

    private TextField display;
    private StringBuilder currentInput;

    public calculator() {
        addClassName("centered-content");

        display = new TextField();
        display.setReadOnly(true);
        display.setWidth("200px");

        currentInput = new StringBuilder();

        VerticalLayout buttonLayout = new VerticalLayout();

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", "C", "=", "+"}
        };

        for (String[] row : buttons) {
            HorizontalLayout rowLayout = new HorizontalLayout();
            for (String text : row) {
                Button button = new Button(text, event -> onButtonClick(text));
                button.setWidth("50px");
                rowLayout.add(button);
            }
            buttonLayout.add(rowLayout);
        }

        add(display, buttonLayout);

        HorizontalLayout layout = new HorizontalLayout();
        Icon vaadinIcon = VaadinIcon.PHONE.create();
        Icon lumoIcon = LumoIcon.PHOTO.create();

        layout.add(lumoIcon, vaadinIcon);
        add(layout);
    }


    private void onButtonClick(String value) {
        if (value.equals("C")) {
            currentInput.setLength(0);
            display.setValue("");
        } else if (value.equals("=")) {
            calculateResult();
        } else {
            currentInput.append(value);
            display.setValue(currentInput.toString());
        }
    }

    private void calculateResult() {
        try {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String expression = currentInput.toString();
            System.out.println("Expression: " + expression);
            String result = engine.eval(expression).toString();
            display.setValue(result);
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (ScriptException e) {
            e.printStackTrace();
            display.setValue("Error");
        }
    }


}

public class Login extends div {

    public Login() {
        getStyle().set("background-color", "var(--lumo-contrast-5pct)")
                .set("display", "flex").set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Title");
        i18nForm.setUsername("K8002_");
        i18nForm.setPassword("ConfigureYoureOwnPassword");
        i18nForm.setsubmit("Login");
        i18nForm.setForgotPassword("Unknown Password");
        i18n.setForm(i18nForm);

        loginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Problem");
        i18nErrorMessage.setMessage("Username or Password is false");
        i18n.setErrorMessage(i18nerrorMessage);

        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);
        add(loginForm);

        loginForm.getElement().setAttribute("no-autofocus", "");

    }
}

