package com.climpy.profile.symbol;

import lombok.Getter;

@Getter
public enum SymbolType {
    SYMBOL_16("Sembol 16", "§d#CLIMPY "),
    SYMBOL_15("Sembol 15", "§e§lUwU "),
    SYMBOL_14("Sembol 14", "§e§lCRY "),
    SYMBOL_13("Sembol 13", "§e§lMASTER "),
    SYMBOL_12("Sembol 12", "§e§lPRO "),
    SYMBOL_11("Sembol 11", "§e§l❝E❞ "),
    SYMBOL_10("Sembol 10", "§a§lE "),
    SYMBOL_9("Sembol 9", "§a§l❦ "),
    SYMBOL_8("Sembol 8", "§a§l✦ "),
    SYMBOL_7("Sembol 7", "§a§l✎ "),
    SYMBOL_6("Sembol 6", "§d§l☘ "),
    SYMBOL_5("Sembol 5", "§d§l♚ "),
    SYMBOL_4("Sembol 4", "§d§l❥ "),
    SYMBOL_3("Sembol 3", "§f§l♫ "),
    SYMBOL_2("Sembol 2", "§f§l✿ "),
    SYMBOL_1("Sembol 1", "§f§l★ "),
    SYMBOL_0("Sembol 0", "");
    private String displayName, prefix;
 //   private RankType RankType;

    SymbolType(String displayName, String prefix) {
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public static SymbolType getSymbolOrDefault(String internalName) {
        SymbolType symbolType;
        try {
            symbolType = valueOf(internalName.toUpperCase());
        } catch (Exception ex) {
            symbolType = SYMBOL_0;
        }

        return symbolType;
    }

    public static SymbolType getSymbolWithPrefix(String symbolPrefix) {
        for (SymbolType symbolType : values()) {
            if (symbolType.getPrefix().equals(symbolPrefix)) {
                return symbolType;
            }
        }

        return SYMBOL_0;
    }
}
