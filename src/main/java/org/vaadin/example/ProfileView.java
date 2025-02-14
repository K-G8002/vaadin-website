package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;
import java.util.List;

@Route("profile")

public class ProfileView extends VerticalLayout {

    public ProfileView() {
        addClassName("centered-content");
        String currentUser = (String) VaadinSession.getCurrent().getAttribute("user");
        if ("Selbsthilfegruppe".equals(currentUser)) {
            Notification.show("Der Admin-Account kann sein Profil nicht ändern");
            UI.getCurrent().navigate("website");
            return;
        }

        add(new H1("Profil einrichten"));

        // Nickname-Feld
        TextField nicknameField = new TextField("Nickname");
        nicknameField.setPlaceholder("Gib deinen Nickname ein");

        // Avatare zur Auswahl
        HorizontalLayout avatarsLayout = new HorizontalLayout();
        List<String> avatarUrls = Arrays.asList(
                "https://www.gravatar.com/avatar/placeholder?s=100&d=identicon",
                "https://www.gravatar.com/avatar/placeholder?s=100&d=monsterid",
                "https://www.gravatar.com/avatar/placeholder?s=100&d=retro"
        );

        // Variable zur Speicherung des ausgewählten Avatars
        final String[] selectedAvatar = {null};

        for (String url : avatarUrls) {
            Image avatar = new Image(url, "Avatar");
            avatar.setWidth("100px");
            avatar.setHeight("100px");
            avatar.getStyle().set("cursor", "pointer");
            // Beim Klick den Avatar auswählen und eine Rückmeldung geben
            avatar.addClickListener(event -> {
                selectedAvatar[0] = url;
                Notification.show("Avatar ausgewählt!");
            });
            avatarsLayout.add(avatar);
        }

        // Button, um das Profil zu speichern
        Button saveButton = new Button("Profil speichern", event -> {
            if (nicknameField.getValue() == null || nicknameField.getValue().isEmpty() || selectedAvatar[0] == null) {
                Notification.show("Bitte wähle einen Avatar und gib einen Nickname ein.");
            } else {
                VaadinSession.getCurrent().setAttribute("nickname", nicknameField.getValue());
                VaadinSession.getCurrent().setAttribute("avatar", selectedAvatar[0]);
                UI.getCurrent().navigate("website");
            }
        });

        add(nicknameField, avatarsLayout, saveButton);
    }
}
