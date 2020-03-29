package com.climpy.profile.symbol;

import com.climpy.profile.rank.RankType;
import lombok.Getter;

@Getter
public enum SymbolType {
    SYMBOL_16("Sembol 16", "§d#CLIMPY ", RankType.CVIPPLUSPLUS),
    SYMBOL_15("Sembol 15", "§e§lUwU ", RankType.CVIPPLUSPLUS),
    SYMBOL_14("Sembol 14", "§e§lCRY ", RankType.CVIPPLUS),
    SYMBOL_13("Sembol 13", "§e§lMASTER ", RankType.CVIP),
    SYMBOL_12("Sembol 12", "§e§lPRO ", RankType.CVIP),
    SYMBOL_11("Sembol 11", "§e§l❝E❞ ", RankType.CVIP),
    SYMBOL_10("Sembol 10", "§a§lE ", RankType.CVIP),
    SYMBOL_9("Sembol 9", "§a§l❦ ", RankType.MVIPPLUS),
    SYMBOL_8("Sembol 8", "§a§l✦ ", RankType.MVIPPLUS),
    SYMBOL_7("Sembol 7", "§a§l✎ ", RankType.MVIP),
    SYMBOL_6("Sembol 6", "§d§l☘ ", RankType.MVIP),
    SYMBOL_5("Sembol 5", "§d§l♚ ", RankType.MVIP),
    SYMBOL_4("Sembol 4", "§d§l❥ ", RankType.VIPPLUS),
    SYMBOL_3("Sembol 3", "§f§l♫ ", RankType.VIPPLUS),
    SYMBOL_2("Sembol 2", "§f§l✿ ", RankType.VIP),
    SYMBOL_1("Sembol 1", "§f§l★ ", RankType.VIP),
    SYMBOL_0("Sembol 0", "", RankType.DEFAULT);
    private String displayName, prefix;
    private RankType rankType;

    SymbolType(String displayName, String prefix, RankType rankType) {
        this.displayName = displayName;
        this.prefix = prefix;
        this.rankType = rankType;
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

    public static SymbolType getDefaultSymbolByRank(RankType rankType) {
        if (rankType == RankType.VIP)
            return SYMBOL_1;
        if (rankType == RankType.VIPPLUS)
            return SYMBOL_4;
        if (rankType == RankType.CVIP)
            return SYMBOL_7;
        if (rankType == RankType.CVIPPLUSPLUS) {
            return SYMBOL_11;
        }
        return SYMBOL_0;
    }
}
