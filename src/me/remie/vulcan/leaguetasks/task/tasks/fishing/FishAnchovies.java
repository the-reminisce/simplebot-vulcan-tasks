package me.remie.vulcan.leaguetasks.task.tasks.fishing;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.helpers.ItemDetails;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopHelper;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopType;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.robot.utils.WorldArea;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;

/**
 * Created by McRea on Mar 10, 2024
 *
 * @author mcrea
 * @Discord mcrea
 */
public class FishAnchovies extends LeagueTask {

    private final int FISHING_SPOT_REGION = 11317; // Catherby shore
    private final ShopHelper fishingNetShopHelper;
    private final int FISHING_NET_ID = 303;
    private final ItemDetails SMALL_FISHING_NET =  new ItemDetails("Small fishing net", FISHING_NET_ID, 300);
    private final int FISHING_SPOT_NPC_ID = 1518;
    private final WorldPoint[] catherbyBankToFishingSpotPath = {
            new WorldPoint(2808, 3439, 0),
            new WorldPoint(2811, 3435, 0),
            new WorldPoint(2817, 3436, 0),
            new WorldPoint(2823, 3437, 0),
            new WorldPoint(2829, 3437, 0),
            new WorldPoint(2834, 3435, 0),
            new WorldPoint(2839, 3434, 0),
            new WorldPoint(2843, 3432, 0),
            new WorldPoint(2847, 3430, 0),
    };

    private final WorldArea fishingSpotArea = new WorldArea(
            new WorldPoint(2844, 3433,0),
            new WorldPoint(2857, 3422,0)
    );

    /**
     * This task achieves fishing level, fish for upcoming task.
     * While completing this task we will also complete the following task:
     * - Catch a Shrimp
     */
    public FishAnchovies(final LeagueScript script) {
        super(script, "Catch an Anchovy");
        this.fishingNetShopHelper = new ShopHelper(this);
    }

    @Override
    public void run() {
        // Validate we got everything before continue
        if (!doWeHaveRequiredItem()) {
            if (!teleportHome()) {
                return;
            }
            if (!getRequiredItem()) {
                ctx.log("Issues getting required items.");
                return;
            }
            return;
        }

        // Teleport to Catherby
        if (!areWeInCatherbyRegion()) {
            if (!teleportHome()) {
                return;
            }
            teleportToCatherby();
            return;
        }

        // Navigate to bank and deposit inventory
        if (ctx.inventory.inventoryFull()) {
            depositItemsInCatherbyBank();
            return;
        }

        // Navigate to fishing spot
        if (!fishingSpotArea.within()) {
            navigateToFishingSpot();
            return;
        }

        if (fishAnchovies()) {
            // Catch some extra fish for next task
            ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 1500, 15);
        }

    }

    private boolean teleportToCatherby() {
        script.setScriptStatus("Teleport to Catherby");
        if (ctx.teleporter.open()) {
            ctx.teleporter.teleportStringPath("Cities", "Catherby");
            ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.CATHERBY_REGION_ID), 250, 10);
            return true;
        }
        return false;
    }

    private boolean areWeInCatherbyRegion() {
        final int playerCurrentRegion = ctx.players.getLocal().getLocation().getRegionID();
        return playerCurrentRegion == LeagueScriptConstants.CATHERBY_REGION_ID || playerCurrentRegion == FISHING_SPOT_REGION;
    }

    private boolean openUpBank() {
        if (!ctx.bank.bankOpen()) {
            script.setScriptStatus("Opening up bank");
            final SimpleNpc banker = ctx.npcs.populate().filter(LeagueScriptConstants.BANKER_NPC_ID, 1613).nextNearest();
            if (banker != null) {
                banker.menuAction("Bank");
                return ctx.onCondition(ctx.bank::bankOpen, 250, 10);
            }
            return false;
        }
        return true;
    }

    private boolean depositInventory() {
        if (!openUpBank()) {
            return false;
        }

        if (!ctx.bank.bankOpen()) {
            return false;
        }

        script.setScriptStatus("Depositing inventory");
        ctx.bank.depositAllExcept(LeagueScriptConstants.COINS_ID, FISHING_NET_ID);
        ctx.sleep(1000);
        script.setScriptStatus("Closing bank");
        ctx.bank.closeBank();
        ctx.sleep(1000);
        return true;
    }

    private boolean getRequiredItem() {
        if (!ctx.inventory.populate().omit(LeagueScriptConstants.COINS_ID, FISHING_NET_ID).isEmpty()) {
            return depositInventory();
        }
        
        script.setScriptStatus("Acquiring " + SMALL_FISHING_NET.getItemName());
        if (fishingNetShopHelper.buyItem(SMALL_FISHING_NET, ShopType.GENERAL_STORE)) {
            ctx.sleep(1000);
        }
        return true;
    }

    private boolean navigateToFishingSpot() {
        script.setScriptStatus("Navigating to fishing spot");
        ctx.pathing.walkPath(catherbyBankToFishingSpotPath);
        ctx.sleep(1000);
        return true;
    }

    private boolean doWeHaveRequiredItem() {
        return !ctx.inventory.populate().filter(FISHING_NET_ID).isEmpty();
    }

    private boolean fishAnchovies() {
        if (ctx.players.getLocal().getAnimation() != -1) {
            return true;
        }

        final SimpleNpc fishSpot = ctx.npcs.populate().filter(FISHING_SPOT_NPC_ID).nextNearest();
        if (fishSpot != null) {
            script.setScriptStatus("Fishing");
            fishSpot.menuAction("Small Net");
            return ctx.onCondition(() -> ctx.players.getLocal().isAnimating(), 250, 10);
        }
        return false;
    }

    private boolean depositItemsInCatherbyBank() {
        if (LeagueScriptConstants.CATHERBY_BANK_AREA.within()) {
            return depositInventory();
        }
        script.setScriptStatus("Navigate to bank");
        ctx.pathing.walkPath(catherbyBankToFishingSpotPath, true);
        ctx.sleep(1000);
        return true;
    }
}
