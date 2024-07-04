package fr.nuggetreckt.nswah.util;

import fr.nuggetreckt.nswah.AuctionHouse;

public enum MessageManager {
    //Error messages
    NO_PERMISSION("§cVous n'avez pas la permission."),

    //Success messages

    //Other messages
    TEST("Test"),
    ;

    private final String message;

    MessageManager(String s) {
        this.message = s;
    }

    public String getMessage() {
        return AuctionHouse.getInstance().getPrefix() + this.message;
    }

    public String getRawMessage() {
        return this.message;
    }
}
