package net.wilamowski.drecho.client.presentation;

public enum Views {
    DICTIONARIES("DictionariesView.fxml"),
    LOGIN("LoginView.fxml"),
    MAIN("MainView.fxml"),
    NOTES("NotesView.fxml"),
    PREFERENCES("PreferenceView.fxml"),
    QUICK_VISIT("QuickVisitView.fxml"),
    SETTINGS("SettingsView.fxml"),
    VISIT_SEARCHER("VisitSearcherView.fxml"),
    WELCOME("WelcomeView.fxml");
    private final String fxmlFileName;

    View(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }
}
