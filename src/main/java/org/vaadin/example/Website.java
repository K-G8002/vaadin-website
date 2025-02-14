package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;
import java.util.List;

@Route("website")
public class Website extends VerticalLayout {

    private static final List<String> avatarUrls = Arrays.asList(
            "https://www.gravatar.com/avatar/placeholder?s=100&d=identicon",
            "https://www.gravatar.com/avatar/placeholder?s=100&d=monsterid",
            "https://www.gravatar.com/avatar/placeholder?s=100&d=retro",
            "https://www.gravatar.com/avatar/placeholder?s=100&d=wavatar",
            "https://www.gravatar.com/avatar/placeholder?s=100&d=robohash",
            "https://www.gravatar.com/avatar/placeholder?s=100&d=mp"
    );

    private int currentAvatarIndex = 0;

    public Website() {
        addClassName("centered-content");

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            Notification.show("Fehler: Keine aktive Sitzung.");
            return;
        }

        String currentUser = (String) session.getAttribute("user");
        String nickname = (String) session.getAttribute("nickname");
        String avatarUrl = (String) session.getAttribute("avatar");

        if (nickname == null || avatarUrl == null) {
            Image avatarImage = new Image(avatarUrls.get(currentAvatarIndex), "Avatar");
            avatarImage.setWidth("150px");
            avatarImage.setHeight("150px");
            avatarImage.getStyle().set("border-radius", "50%");

            Button previousButton = new Button("<", event -> {
                currentAvatarIndex = (currentAvatarIndex - 1 + avatarUrls.size()) % avatarUrls.size();
                avatarImage.setSrc(avatarUrls.get(currentAvatarIndex));
            });

            Button nextButton = new Button(">", event -> {
                currentAvatarIndex = (currentAvatarIndex + 1) % avatarUrls.size();
                avatarImage.setSrc(avatarUrls.get(currentAvatarIndex));
            });
            add(new HorizontalLayout(previousButton, avatarImage, nextButton));

            TextField nicknameField = new TextField("Nickname");
            nicknameField.setPlaceholder("LustigerName");
            add(nicknameField);

        } else {
            Image avatarImage = new Image(avatarUrl, "Avatar");
            avatarImage.setWidth("100px");
            avatarImage.setHeight("100px");
            avatarImage.getStyle().set("border-radius", "50%");

            Span nicknameSpan = new Span("Nickname: " + nickname);

            add(avatarImage, nicknameSpan);
        }

        Button createGroupButton = new Button("Create Group", event -> {
                String code = GroupManager.createGroup();
                Notification.show("Gruppe erstellt. Code: " + code);
        });
        add(createGroupButton);

        Button showMembers = new Button("Show Group Members", event -> {
            Notification.show("Gruppenmitglieder: " + GroupManager.getGroupMembers().toString());
        });
        add(showMembers);

        Button joinGroupButton = new Button("Join Group", event -> {
            if (nickname == null || avatarUrl == null) {
                Notification.show("Bitte richte zuerst dein Profil (Avatar und Nickname) ein.");
                return;
            }
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
            dialog.add(new VerticalLayout(codeField, submitButton));
            dialog.open();
        });
        add(joinGroupButton);
    }
}













