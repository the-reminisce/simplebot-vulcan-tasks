package me.remie.vulcan.leaguetasks.task.tasks.crafting;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.helpers.BankManager;
import me.remie.vulcan.leaguetasks.helpers.ItemDetails;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopHelper;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopType;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.queries.SimpleEntityQuery;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

import java.util.Map;

import static net.runelite.api.ItemID.COWHIDE;

/**
 * Created by Pepsiplaya on Mar 05, 2024 at 10:51 PM
 */
public class CraftLeatherBody extends LeagueTask {
    BankManager bankManager = new BankManager(script);
    private static final int LEATHER = 1741;
    private static final int NEEDLE_ID = 1733;
    private static final int THREAD_ID = 1734;
    private static final ItemDetails NEEDLE = new ItemDetails("Needle", NEEDLE_ID, 5_000);
    private static final ItemDetails THREAD = new ItemDetails("Thread", THREAD_ID, 5_000);
    private final ShopHelper shopHelper;

    public CraftLeatherBody(final LeagueScript script) {
        super(script, "Craft a Leather Body");
        this.shopHelper = new ShopHelper(this);
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.CRAFTING, 14)));
    }

    @Override
    public void run() {
        if (hasItemInInventory(NEEDLE_ID) && hasItemInInventory(THREAD_ID) && hasItemInInventory(LEATHER)) {
            if (!ctx.dialogue.dialogueOpen()) {
                SimpleItem leather = ctx.inventory.populate().filter(LEATHER).next();
                SimpleItem needle = ctx.inventory.populate().filter(NEEDLE_ID).next();
                needle.menuAction("Use");
                ctx.sleep(600);
                leather.click(0);
                ctx.sleep(600);
            } else {
                ctx.dialogue.clickDialogueOption(1);
                ctx.sleep(600);
            }
        }
        if (!hasItemInInventory(NEEDLE_ID)) {
            if (!teleportHome()) {
                return;
            }
            shopHelper.buyItem(NEEDLE, ShopType.GENERAL_STORE);
        }

        if (!hasItemInInventory(THREAD_ID)) {
            if (!teleportHome()) {
                return;
            }
            shopHelper.buyItem(THREAD, ShopType.GENERAL_STORE);
        }

        if (!hasItemInInventory(LEATHER)) {
            obtainLeather();
        }

        // Proceed with crafting the leather body if all items are now obtained...
    }

    private boolean hasItemInInventory(int itemId) {
        return !ctx.inventory.populate().filter(itemId).isEmpty();
    }

    private void obtainLeather() {
        if (hasItemInInventory(THREAD_ID) && hasItemInInventory(NEEDLE_ID)) {
            if (ctx.inventory.populate().filter(COWHIDE).population() < 1) {
                killCowsAndLootHides();
                return;
            }
            tanCowhidesIntoLeather();
        }
    }

    private void killCowsAndLootHides() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.LUMBRIDGE_COWS_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Cows");
                ctx.teleporter.teleportStringPath("Training", "Cows & Goblins");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.LUMBRIDGE_COWS_REGION_ID), 250, 10);
                return;
            }
        } else {
            SimpleGroundItem drop = ctx.groundItems.nearest().peekNext();
            if (!ctx.groundItems.populate().filter(COWHIDE).isEmpty() && drop != null && ctx.inventory.canPickupItem(drop)) {
                drop.menuAction("Take");
                ctx.sleep(600);
                return;
            }
            SimpleNpc cow = cows(ctx).nearest().next();
            if (cow != null && !isNpcEngaged(cow)) {
                engageCow(cow);
            }
        }
    }

    private void tanCowhidesIntoLeather() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.AL_KHARID_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Al Kharid");
                ctx.teleporter.teleportStringPath("Cities", "Al kharid");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.AL_KHARID_REGION_ID), 250, 10);
                return;
            }
        } else {
            SimpleNpc tanner = ctx.npcs.populate().filter(3231).nearest().next();
            if (tanner != null) {
                tanner.menuAction("Trade");
                ctx.sleep(6000);
                ctx.menuActions.sendAction(24, 0, 21233812, 0, "Tan", "<col=ff7000>1");
            } else {
                ctx.pathing.step(ctx.players.getLocal().getLocation().getX() - 5, ctx.players.getLocal().getLocation().getY() + 5);
            }
        }
    }

    private void engageCow(SimpleNpc cow) {
        script.setScriptStatus("Engaging Cows");
        ctx.sleep(1, 4000);
        if (!isNpcEngaged(cow)) {
            cow.menuAction("Attack");
            ctx.sleep(600, 1200);
        }
    }

    public final static SimpleEntityQuery<SimpleNpc> cows(ClientContext ctx) {
        return ctx.npcs.populate().filter(2791, 2792).filter(n -> {
            if (n == null) {
                return false;
            }
            if (n.isDead()) {
                return false;
            }
            if (n.getInteracting() == ctx.players.getLocal().getPlayer()) {
                return true;
            }
            if (n.getInteracting() != null && n.getInteracting() != ctx.players.getLocal().getInteracting()) {
                return false;
            }
            return true;
        });
    }

    private boolean isNpcEngaged(SimpleNpc npc) {
        return npc.getInteracting() != null;
    }


}