package org.vaadin.example;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("lobby")
public class Lobby extends VerticalLayout {

    VerticalLayout buttonLayout = new VerticalLayout();

    public Lobby() {
        setSizeFull();
        addClassName("main-background");

        add(buttonLayout);

        buttonLayout.addClassName("centered-content");

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
        buttonLayout.add(new Span("Dein Gruppen-Code: " + groupCode));

        Button showMembers = new Button("Show Group Members", event -> {
            Notification.show("Gruppenmitglieder: " + GroupManager.getGroupMembers().toString());
        });
        buttonLayout.add(showMembers);

        Button leaveGroupButton = new Button("Leave group", event -> {
            UI.getCurrent().navigate("website");
        });
        buttonLayout.add(leaveGroupButton);
    }

}