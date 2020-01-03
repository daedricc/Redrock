package com.climpy.server;

public enum ServerState {
    SETUP("Açılış sürüyor..", "setup"),
    INACTIVE("Kapalı", "inactive"),
    ACTIVE("Çevrimiçi", "active");

    ServerState(String displayName, String internalName) {
      this.displayName = displayName;
      this.internalName = internalName;
    }

    private String displayName;

    private String internalName;

    public String getDisplayName() {
      return this.displayName;
    }

    public String getInternalName() {
      return this.internalName;
    }

    public static ServerState getStateType(String serverState) {
      ServerState[] arrayOfServerState;
      int i;
      byte b;
      for (arrayOfServerState = values(), i = arrayOfServerState.length, b = 0; b < i; ) {
        ServerState stateType = arrayOfServerState[b];
        if (!stateType.getInternalName().equalsIgnoreCase(serverState)) {
          b++;
          continue;
        }
        return stateType;
      }
      return INACTIVE;
    }
}
