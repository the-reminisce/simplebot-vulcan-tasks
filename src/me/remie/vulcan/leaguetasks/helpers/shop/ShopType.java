package me.remie.vulcan.leaguetasks.helpers.shop;

/**
 * Created by Reminisce on Mar 07, 2024 at 5:17 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public enum ShopType {

    MELEE("melee"),
    RANGE("range"),
    MAGIC("magic"),
    GENERAL_STORE("general store"),
    VULCAN_TOKENS("vulcan tokens"),
    GENERAL_SUPPLIES("general supplies"),
    CONSTRUCTION("construction"),
    IRONMAN("ironman"),
    CUSTOM("all custom items on vulcan");

    private final String name;
    private final int menuActionId;

    ShopType(final String name) {
        this.name = name;
        this.menuActionId = 54788112 + (ordinal() * 2);
    }

    public String getName() {
        return name;
    }

    public int getMenuActionId() {
        return menuActionId;
    }

}
