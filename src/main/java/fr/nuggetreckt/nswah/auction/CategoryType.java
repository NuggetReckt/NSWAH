package fr.nuggetreckt.nswah.auction;

public enum CategoryType {
    ALL(""),
    COMBAT(""),
    TOOLS(""),
    ENCHANTS(""),
    BLOCS(""),
    OTHER(""),
    ;

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
