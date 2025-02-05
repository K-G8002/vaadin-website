package org.vaadin.example;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GroupManager {
    private static boolean selfhilfegruppeLoggedIn = false;
    private static String currentGroupCode = null;
    private static Set<String> groupMembers = new HashSet<>();

    public static boolean isSelfhilfegruppeLoggedIn() {
        return selfhilfegruppeLoggedIn;
    }

    public static void setSelfhilfegruppeLoggedIn(boolean loggedIn) {
        selfhilfegruppeLoggedIn = loggedIn;
    }

    public static String createGroup() {
        currentGroupCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase(); // z. B. 6 Zeichen Code
        groupMembers.clear();
        groupMembers.add("Selbsthilfegruppe"); // der Ersteller wird als Mitglied hinzugefügt
        return currentGroupCode;
    }

    public static String getCurrentGroupCode() {
        return currentGroupCode;
    }

    public static boolean joinGroup(String code, String username) {
        if (currentGroupCode != null && currentGroupCode.equalsIgnoreCase(code)) {
            groupMembers.add(username);
            return true;
        }
        return false;
    }

    public static Set<String> getGroupMembers() {
        return groupMembers;
    }

    public static void clearGroup() {
        currentGroupCode = null;
        groupMembers.clear();
    }
}
