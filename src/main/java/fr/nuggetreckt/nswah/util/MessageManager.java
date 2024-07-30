package fr.nuggetreckt.nswah.util;

import fr.nuggetreckt.nswah.AuctionHouse;

public enum MessageManager {
    //Error messages
    NO_PERMISSION("§cVous n'avez pas la permission."),
    NO_INVENTORY_ROOM("§cVous n'avez pas assez de place dans votre inventaire !"),
    NO_ENOUGH_MONEY("§cVous n'avez pas assez d'argent pour acheter cet item !"),

    //Success messages
    PAYMENT_SUCCESS("§fPayement de §3%dNSc §fà §3%s §feffectué avec succès."),
    PAYMENT_RECEIVED("§3%s §fvous a acheté un item pour §3%dNSc§f. L'argent à été versé sur votre compte."),
    ITEM_PRICE_UPDATED("§fLe prix de vente de votre item à été mis à jour avec succès."),
    ITEM_PRICE_SET("§fLe prix de vente de votre item à été défini à §3%dNSc§f."),
    ITEM_PUT_ON_SALE("§fVotre item à été mis en vente avec succès !"),
    ITEM_REMOVED("§fVotre item à été retiré de la vente avec succès."),

    //Other messages
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
