package net.wilamowski.drecho.client.presentation;

public enum Views {
    DICTIONARIES("Dictionaries"),
    LOGIN("Login"),
    MAIN("Main"),
    NOTES("Notes"),
    PREFERENCES("Preference"),
    QUICK_VISIT("QuickVisit"),
    SETTINGS("Settings"),
    VISIT_SEARCHER("VisitSearcher"),
    WELCOME("Welcome");
    private String fxmlFileName;

    Views(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
