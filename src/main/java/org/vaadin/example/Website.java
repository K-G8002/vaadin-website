package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
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
    private String nickname;
    private String avatarUrl;
    private String BodyWeight;
    private String Gender;

    public Website() {
        addClassName("centered-content");

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
        nicknameField.setPlaceholder("Name");
        add(nicknameField);

        TextField BodyWeightField = new TextField("Body Weight");
        BodyWeightField.setPlaceholder("Körpergewicht");
        add(BodyWeightField);

        Select<String> GenderSelector = new Select<>();
        GenderSelector.setLabel("Gender");
        GenderSelector.setItems("Männlich", "Weiblich");
        GenderSelector.setValue("Männlich");
        add(GenderSelector);

        Button saveProfileButton = new Button("Profil speichern", event -> {
            nickname = nicknameField.getValue();
            avatarUrl = avatarUrls.get(currentAvatarIndex);
            BodyWeight = BodyWeightField.getValue();
            Gender = GenderSelector.getValue();
            if (nickname.isEmpty()) {
                Notification.show("Bitte gib einen Namen ein.");
                return;
            }
            if (avatarUrl.isEmpty()) {
                Notification.show("Bitte wähle ein Avatar aus.");
                return;
            }
            if (BodyWeight.isEmpty()) {
                Notification.show("Bitte gib dein Körpergewicht an.");
                return;
            }
            if (Gender.isEmpty()) {
                Notification.show("Bitte gib dein Geschlecht an.");
                return;
            }
            Notification.show("Profil gespeichert!");
        });

        add(saveProfileButton);

        Button createGroupButton = new Button("Create Group", event -> {
            if (nickname == null || avatarUrl == null || BodyWeight == null || Gender == null) {
                Notification.show("Bitte richte zuerst dein Profil ein.");
                return;
            }
            VaadinSession session = VaadinSession.getCurrent();
            if (session == null) {
                Notification.show("Fehler: Keine aktive Sitzung.");
                return;
            }
            String code = GroupManager.createGroup();
            session.setAttribute("groupCode", code);
            UI.getCurrent().navigate("lobby");
        });

        add(createGroupButton);

        Button joinGroupButton = new Button("Join Group", event -> {
            if (nickname == null || avatarUrl == null || BodyWeight == null || Gender == null) {
                Notification.show("Bitte richte zuerst dein Profil ein.");
                return;
            }
            Dialog dialog = new Dialog();
            TextField codeField = new TextField("Gruppen-Code");
            Button submitButton = new Button("Beitreten", e -> {
                String enteredCode = codeField.getValue();
                if (GroupManager.joinGroup(enteredCode, nickname)) {
                    VaadinSession session = VaadinSession.getCurrent();
                    session.setAttribute("groupCode", enteredCode);
                    Notification.show("Erfolgreich der Gruppe beigetreten.");
                    UI.getCurrent().navigate("lobby");
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

@Route("lobby")
class Lobby extends VerticalLayout {

    public Lobby() {
        addClassName("centered-content");

        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            Notification.show("Fehler: Keine aktive Sitzung.");
            UI.getCurrent().navigate("website");
            return;
        }

        String groupCode = (String) session.getAttribute("groupCode");
        if (groupCode == null) {
            Notification.show("Du bist keiner Gruppe zugeordnet.");
            UI.getCurrent().navigate("website");
            return;
        }

        add(new Span("Dein Gruppen-Code: " + groupCode));

        Button showMembers = new Button("Show Group Members", event -> {
            Notification.show("Gruppenmitglieder: " + GroupManager.getGroupMembers().toString());
        });

        add(showMembers);
    }
}












