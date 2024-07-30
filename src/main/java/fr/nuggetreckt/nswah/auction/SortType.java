package fr.nuggetreckt.nswah.auction;

public enum SortType {
    DATE_ASC("§aDate §8(§7plus vieux§8)"),
    DATE_DESC("§aDate §8(§7plus récent§8)"),
    NAME_ASC("§aNom de l'item §8(§7A > Z§8)"),
    NAME_DESC("§aNom de l'item §8(§7A < Z§8)"),
    PLAYER_ASC("§aNom du joueur §8(§7A > Z§8)"),
    PLAYER_DESC("§aNom du joueur §8(§7A < Z§8)"),
    ;

    private final String displayName;

    SortType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
