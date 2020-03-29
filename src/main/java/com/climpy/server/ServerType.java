package com.climpy.server;

public enum ServerType {
    LOBI("Lobi", "lobi", false),
    PRACTICE("Practice", "practice", false),
    SURVIVAL_GAMES("Survival Games", "survival games", false),
    GAME_LOBBY("Oyun Lobisi", "game lobby", true),
    UNKNOWN("Unknown", "unknown", false);

    ServerType(String displayName, String internalName, boolean announceable) {
        this.displayName = displayName;
        this.internalName = internalName;
        this.announceable = announceable;
    }

    private String displayName;

    private String internalName;

    private boolean announceable;

    public String getDisplayName() {
        return this.displayName;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public boolean isAnnounceable() {
        return this.announceable;
    }

    public static ServerType getServerType(String serverType) {
        ServerType[] arrayOfServerType;
        int i;
        byte b;
        for (arrayOfServerType = values(), i = arrayOfServerType.length, b = 0; b < i; ) {
            ServerType typeServer = arrayOfServerType[b];
            if (!typeServer.getInternalName().equalsIgnoreCase(serverType)) {
                b++;
                continue;
            }
            return typeServer;
        }
        return UNKNOWN;
    }
}
