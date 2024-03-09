package me.remie.vulcan.leaguetasks.task.tasks.woodcutting;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.helpers.ItemDetails;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopHelper;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopType;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Reminisce on Mar 07, 2024 at 5:48 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class WoodcuttingCore extends LeagueTask {

    private static final WorldPoint BANK_TILE = new WorldPoint(1592, 3476, 0);
    private static final ItemDetails IRON_AXE = new ItemDetails("Iron axe", 1349, 1_000);
    private static final ItemDetails RUNE_AXE = new ItemDetails("Rune axe", 1359, 250_000);

    private final ShopHelper ironAxeShopHelper;
    private final ShopHelper runeAxeShopHelper;

    private final WoodcuttingTree selectedTree;

    public WoodcuttingCore(final LeagueScript script, final String taskName, final WoodcuttingTree selectedTree) {
        super(script, taskName);
        this.selectedTree = selectedTree;
        this.ironAxeShopHelper = new ShopHelper(this);
        this.runeAxeShopHelper = new ShopHelper(this);
    }

    @Override
    public void run() {
        if (!getShopHelper().hasItem()) {
            getShopHelper().buyItem(getRequiredAxe(), ShopType.GENERAL_STORE);
            return;
        }
        if (!LeagueScriptConstants.WOODCUTTING_GUILD_AREA.within()) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Skilling", "Woodcutting Area");
                ctx.onCondition(LeagueScriptConstants.WOODCUTTING_GUILD_AREA::within, 350, 10);
                return;
            }
            return;
        }
        if (ctx.inventory.inventoryFull()) {
            handleBanking();
            return;
        }

        final WoodcuttingTree tree = getBestTree();
        if (ctx.pathing.inArea(tree.getArea()) && ctx.pathing.distanceTo(tree.getArea().getCenterPoint()) >= 17) {
            ctx.menuActions.step(tree.getArea().getCenterPoint());
            return;
        }
        if (ctx.players.getLocal().getAnimation() == -1) {
            final SimpleObject treeObject = ctx.objects.populate().filter(tree.getTreeName())
                    .filter(LeagueScriptConstants.WOODCUTTING_GUILD_AREA).filter(tree.getArea()).nextNearest();
            if (treeObject != null) {
                treeObject.menuAction("Chop down");
                ctx.onCondition(() -> ctx.players.getLocal().getAnimation() != -1, 250, 10);
                return;
            } else {
                System.out.println("We can't find a tree");
            }
        }
    }

    private void handleBanking() {
        final double distance = ctx.pathing.distanceTo(BANK_TILE);
        if (distance >= 10) {
            ctx.pathing.walkPath(ctx.pathing.createLocalPath(BANK_TILE));
            return;
        } else if (!ctx.pathing.onTile(BANK_TILE)) {
            ctx.menuActions.step(BANK_TILE);
            return;
        }
        if (!ctx.bank.openBank(true)) {
            return;
        }
        final Set<Integer> depositedIds = new HashSet<>();
        for (final SimpleItem item : ctx.inventory.populate().omit(getRequiredAxe().getItemId())) {
            if (depositedIds.contains(item.getId())) {
                continue;
            }
            item.menuAction("Deposit-All");
            depositedIds.add(item.getId());
            ctx.sleep(50, 150);
        }
        ctx.sleep(500, 1000);
        ctx.bank.closeBank();
    }

    public WoodcuttingTree getBestTree() {
        if (this.selectedTree != null && ctx.skills.realLevel(SimpleSkills.Skills.WOODCUTTING) >= this.selectedTree.getRequiredLevel()) {
            return this.selectedTree;
        }
        WoodcuttingTree bestTree = WoodcuttingTree.NORMAL;
        for (final WoodcuttingTree tree : WoodcuttingTree.values()) {
            if (ctx.skills.realLevel(SimpleSkills.Skills.WOODCUTTING) >= tree.getRequiredLevel()) {
                bestTree = tree;
            }
        }
        return bestTree;
    }

    private ItemDetails getRequiredAxe() {
        if (ctx.skills.realLevel(SimpleSkills.Skills.WOODCUTTING) >= 41) {
            return RUNE_AXE;
        }
        return IRON_AXE;
    }

    private ShopHelper getShopHelper() {
        if (ctx.skills.realLevel(SimpleSkills.Skills.WOODCUTTING) >= 41) {
            return runeAxeShopHelper;
        }
        return ironAxeShopHelper;
    }


}
