package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;


@Route("website")

public class Website extends VerticalLayout {

    private TextField display;
    private StringBuilder currentInput;

    public Website() {
        if (VaadinSession.getCurrent().getAttribute("user") == null) {
            UI.getCurrent().navigate("login");
            return;
        }
        addClassName("centered-content");

        display = new TextField();
        display.setReadOnly(true);
        display.setWidth("200px");

        Button logoutButton = new Button("Logout", event -> {
            String currentUser = (String) VaadinSession.getCurrent().getAttribute("user");
            if ("Selbsthilfegruppe".equals(currentUser)) {
                GroupManager.setSelfhilfegruppeLoggedIn(false);
                GroupManager.clearGroup();
            }
            VaadinSession.getCurrent().setAttribute("user", null);
            UI.getCurrent().navigate("login");
        });
        logoutButton.getStyle()
                .set("position", "absolute")
                .set("bottom", "10px")
                .set("left", "10px");
        add(logoutButton);

        String currentUser = (String) VaadinSession.getCurrent().getAttribute("user");
        if ("Selbsthilfegruppe".equals(currentUser)) {
            Button createGroupButton = new Button("Create Group", event -> {
                String code = GroupManager.createGroup();
                Notification.show("Gruppe erstellt. Code: " + code);
            });
            add(createGroupButton);

            Button showMembers = new Button("Show Group Members", event -> {
                Notification.show("Gruppenmitglieder: " + GroupManager.getGroupMembers().toString());
            });
            add(showMembers);
        } else {
            Button joinGroupButton = new Button("Join Group", event -> {
                Dialog dialog = new Dialog();
                TextField codeField = new TextField("Gruppen-Code");
                Button submitButton = new Button("Beitreten", e -> {
                    String enteredCode = codeField.getValue();
                    if (GroupManager.joinGroup(enteredCode, currentUser)) {
                        Notification.show("Erfolgreich der Gruppe beigetreten.");
                        dialog.close();
                    } else {
                        Notification.show("Ungültiger Gruppen-Code.");
                    }
                });
                VerticalLayout dialogLayout = new VerticalLayout(codeField, submitButton);
                dialog.add(dialogLayout);
                dialog.open();
            });
            add(joinGroupButton);
        }
    }
}


@Route("login")
class login extends VerticalLayout {

    public login() {
        addClassName("centered-content");
        getStyle().set("background-color", "var(--lumo-contrast-5pct)")
                .set("display", "flex")
                .set("justify-content", "center")
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

            if ("Selbsthilfegruppe".equals(username) && GroupManager.isSelfhilfegruppeLoggedIn()) {
                loginForm.setError(true);
                com.vaadin.flow.component.notification.Notification.show("Der Account 'Selbsthilfegruppe' ist bereits angemeldet.");
                return;
            }

            if (authenticate(username, password)) {
                VaadinSession.getCurrent().setAttribute("user", username);
                if ("Selbsthilfegruppe".equals(username)) {
                    GroupManager.setSelfhilfegruppeLoggedIn(true);
                }
                UI.getCurrent().navigate("website");
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.getElement().setAttribute("no-autofocus", "");
        add(loginForm);

        Button registerButton = new Button("Register", event -> UI.getCurrent().navigate("register"));
        add(registerButton);
    }

    private boolean authenticate(String username, String password) {
        return ("Selbsthilfegruppe".equals(username) && "lol".equals(password)) ||
                ("User".equals(username) && "12345".equals(password));
    }
}


@Route("register")
class RegisterView extends VerticalLayout {

    private static HashMap<String, String> userDatabase = new HashMap<>();

    public RegisterView() {
        addClassName("centered-content");
        getStyle().set("background-color", "var(--lumo-contrast-5pct)")
                .set("display", "flex")
                .set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");

        TextField usernameField = new TextField("Username");
        TextField passwordField = new TextField("Password");
        Button registerButton = new Button("Register", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                usernameField.setErrorMessage("Fields cannot be empty");
                usernameField.setInvalid(true);
                return;
            }

            if ("Selbsthilfegruppe".equals(username)) {
                usernameField.setErrorMessage("Dieser Username ist reserviert.");
                usernameField.setInvalid(true);
                return;
            }

            if (userDatabase.containsKey(username)) {
                usernameField.setErrorMessage("Username already exists");
                usernameField.setInvalid(true);
            } else {
                userDatabase.put(username, password);
                UI.getCurrent().navigate("login");
            }
        });
        add(usernameField, passwordField, registerButton);
    }

    public static boolean validateUser(String username, String password) {
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }
}







