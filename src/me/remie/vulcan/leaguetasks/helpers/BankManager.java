package me.remie.vulcan.leaguetasks.helpers;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopHelper;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopType;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.robot.api.ClientContext;

import java.util.Map;
import java.util.Map.Entry;

public class BankManager {

    private final LeagueScript script;
    private final ClientContext ctx;

    public BankManager(LeagueScript script) {
        this.script = script;
        this.ctx = script.ctx;
    }

    public boolean checkAndWithdrawItems(Map<Integer, Integer> itemRequirements) {
        if (!ctx.bank.bankOpen() && !openBank()) {
            ctx.log("Failed to open bank.");
            return false;
        }

        boolean allItemsAvailable = true;
        for (Entry<Integer, Integer> entry : itemRequirements.entrySet()) {
            int itemId = entry.getKey();
            int quantityRequired = entry.getValue();
            if (ctx.bank.populate().filter(itemId).population() < quantityRequired) {
                allItemsAvailable = false;
                ctx.log("Item not found or insufficient quantity in bank: ID " + itemId);
                break; // Break early if any item is missing
            }
        }

        if (!allItemsAvailable) {
            ctx.bank.closeBank();
            return false;
        }

        // Withdraw items
        for (Entry<Integer, Integer> entry : itemRequirements.entrySet()) {
            int itemId = entry.getKey();
            int quantityRequired = entry.getValue();
            ctx.bank.withdraw(itemId, quantityRequired);
            ctx.sleep(200); // Add a small delay to mimic human behavior
        }

        ctx.bank.closeBank();
        return true;
    }

    private boolean openBank() {
        ctx.bank.openBank();
        return ctx.onCondition(() -> ctx.bank.bankOpen(), 2000); // Wait up to 2 seconds
    }

    public void obtainItem(ItemDetails itemDetails, LeagueTask currentTask) {
        // First, try to check and withdraw the item from the bank.
        if (checkAndWithdrawItems(Map.of(itemDetails.getItemId(), 1))) {
            return; // If item was successfully withdrawn, return.
        }

        // If item is not in the bank, proceed to obtain it.
        if (itemDetails.getRequiredCoins() > 0) {
            // Attempt to purchase the item from a shop, now passing currentTask to ShopHelper.
            ShopHelper shopHelper = new ShopHelper(currentTask); // Adjusted to pass currentTask
            if (!shopHelper.buyItem(itemDetails, ShopType.GENERAL_STORE)) {
                ctx.log("Could not buy the item: " + itemDetails.getItemName());
                // Add additional logic here for non-purchasable item acquisition.
            }
        } else {
            // For items that cannot be bought, initiate a gathering task.
            // This part needs custom logic based on your task structure and item requirements.
        }
    }

}
