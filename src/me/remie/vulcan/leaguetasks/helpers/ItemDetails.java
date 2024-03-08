package me.remie.vulcan.leaguetasks.helpers;

/**
 * Created by Reminisce on Mar 07, 2024 at 5:08 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ItemDetails {

    private final int itemId;
    private final int requiredCoins;
    private final String itemName;

    public ItemDetails(final String itemName, final int itemId, final int requiredCoins) {
        this.itemId = itemId;
        this.requiredCoins = requiredCoins;
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRequiredCoins() {
        return requiredCoins;
    }

    public String getItemName() {
        return itemName;
    }

}
