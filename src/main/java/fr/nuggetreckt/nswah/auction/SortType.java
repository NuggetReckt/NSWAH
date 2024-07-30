package fr.nuggetreckt.nswah.auction;

public enum SortType {
    DATE_DESC("§aDate §8(§7plus récent§8)"),
    DATE_ASC("§aDate §8(§7plus vieux§8)"),
    PLAYER_ASC("§aNom du joueur §8(§7A > Z§8)"),
    PLAYER_DESC("§aNom du joueur §8(§7A < Z§8)"),
    PRICE_ASC("§aPrix §8(§7moins cher§8)"),
    PRICE_DESC("§aPrix §8(§7plus cher§8)"),
    ;

    private final String displayName;

    SortType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
