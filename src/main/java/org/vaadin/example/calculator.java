package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoIcon;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Route("website")
public class calculator extends VerticalLayout {

    private TextField display;
    private StringBuilder currentInput;

    public calculator() {
        if (VaadinSession.getCurrent().getAttribute("user") == null) {
            UI.getCurrent().navigate("login");
            return;
        }
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

        Button logoutButton = new Button("Logout", event -> {
            VaadinSession.getCurrent().setAttribute("user", null);
            UI.getCurrent().navigate("login");
        });
        add(logoutButton);
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

@Route("login")
class login extends VerticalLayout {

    public login() {
        addClassName("centered-content");
        getStyle().set("background-color", "var(--lumo-contrast-5pct)")
                .set("display", "flex").set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Login");
        i18nForm.setUsername("Username");
        i18nForm.setPassword("Password");
        i18nForm.setSubmit("Submit");
        i18nForm.setForgotPassword("Unknown Password");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Problem");
        i18nErrorMessage.setMessage("Username or Password is false");
        i18n.setErrorMessage(i18nErrorMessage);

        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);

        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            if (authenticate(username, password)) {
                VaadinSession.getCurrent().setAttribute("user", username);
                UI.getCurrent().navigate("website");
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.getElement().setAttribute("no-autofocus", "");
        add(loginForm);
    }

    private boolean authenticate(String username, String password) {
        return "KarliBobMarli".equals(username) && "1234".equals(password);
    }
}




