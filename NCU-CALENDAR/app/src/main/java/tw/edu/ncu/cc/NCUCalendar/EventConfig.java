package tw.edu.ncu.cc.NCUCalendar;

public class EventConfig {
    private String serverAddress;
    private String language;

    public EventConfig( String serverAddress, String language ) {
        this.serverAddress = serverAddress;
        this.language = language;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getLanguage() {
        return language;
    }
}
