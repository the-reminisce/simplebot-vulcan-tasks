package me.remie.vulcan.leaguetasks.helpers.shop;

import me.remie.vulcan.leaguetasks.helpers.ItemDetails;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.tasks.thieving.ThievingLevel20;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.api.ClientContext;

/**
 * Created by Reminisce on Mar 07, 2024 at 5:10 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ShopHelper {

    private static final int COINS = 995;
    private static final int SHOP_WIDGET_ID = 836;
    private static final int SHOP_KEEPER_ID = 7913;
    private static final WorldPoint SHOP_TILE = new WorldPoint(3077, 3500, 0);

    private final ClientContext ctx;
    private final LeagueTask task;

    private boolean checkedBank = false;
    private boolean hasEnoughCoins = false;

    private boolean hasCheckedForItem = false;
    private boolean hasItem = false;

    public ShopHelper(final LeagueTask task) {
        this.ctx = task.ctx;
        this.task = task;
    }

    public boolean buyItem(final ItemDetails itemDetails, final ShopType shopType) {
        if (hasItem || (!ctx.inventory.populate().filter(itemDetails.getItemId()).isEmpty() || !ctx.equipment.populate().filter(itemDetails.getItemId()).isEmpty())) {
            hasItem = true;
            return true;
        }
        if (!checkItemInInventoryAndBank(itemDetails)) {
            return false;
        }

        if (!checkAndManageCoins(itemDetails.getRequiredCoins())) {
            return false;
        }

        if (!openShop()) {
            return false;
        }

        final SimpleWidget shopWidget = getShopWidget();
        if (isShopOpen(shopWidget)) {
            if (shopType.getName().equalsIgnoreCase(getShopName(shopWidget))) {
                ctx.menuActions.sendAction(57, 30, 54788137, 2, itemDetails.getItemId(),
                        "Buy 1", "<col=ff9040>" + itemDetails.getItemName() + "</col>");
                return ctx.onCondition(() -> !ctx.inventory.populate().filter(itemDetails.getItemId()).isEmpty() && closeShop(), 250, 10);
            } else {
                ctx.menuActions.clickButton(shopType.getMenuActionId());
                ctx.sleep(500, 1000);
            }
        }

        return false;
    }

    private boolean checkItemInInventoryAndBank(final ItemDetails itemDetails) {
        if (hasCheckedForItem) {
            return true;
        }
        if (!ctx.inventory.populate().filter(itemDetails.getItemId()).isEmpty() || !ctx.equipment.populate().filter(itemDetails.getItemId()).isEmpty()) {
            hasItem = true;
            hasCheckedForItem = true;
            return true;
        }
        if (!checkedBank) {
            if (ctx.bank.openBank()) {
                ctx.sleep(500, 1000);
                if (!ctx.bank.populate().filter(itemDetails.getItemId()).isEmpty()) {
                    hasItem = true;
                    final SimpleItem item = ctx.bank.next();
                    if (item != null) {
                        item.menuAction("withdraw-1");
                        ctx.onCondition(() -> ctx.inventory.populate().filter(itemDetails.getItemId()).population(true) > 0, 250, 10);
                    }
                }
                hasCheckedForItem = true;
                ctx.bank.closeBank();
                return true;
            }
        }
        return false;
    }

    private boolean checkAndManageCoins(final int requiredCoins) {
        if (checkedBank && hasEnoughCoins) {
            return true;
        }

        int coinsInInventory = ctx.inventory.populate().filter(COINS).population(true);
        if (coinsInInventory >= requiredCoins) {
            checkedBank = true;
            hasEnoughCoins = true;
            return true;
        }

        if (!checkedBank) {
            if (ctx.bank.openBank()) {
                ctx.sleep(500, 1000);
                int coinsInBank = ctx.bank.populate().filter(COINS).population(true);
                if (coinsInInventory + coinsInBank >= requiredCoins) {
                    hasEnoughCoins = true;
                    final SimpleItem coins = ctx.bank.next();
                    if (coins != null) {
                        final int coinsToWithdraw = requiredCoins - coinsInInventory;
                        coins.menuAction("withdraw-x");
                        if (ctx.onCondition(ctx.dialogue::pendingInput, 250, 10)) {
                            ctx.keyboard.sendKeys(String.valueOf(coinsToWithdraw), true);
                        }
                        ctx.onCondition(() -> ctx.inventory.populate().filter(COINS).population(true) >= requiredCoins, 250, 10);
                    }
                }
                ctx.bank.closeBank();
                checkedBank = true;
            }
        }

        if (!hasEnoughCoins) {
            task.script.getTask(ThievingLevel20.class).run();
            return false;
        }

        return ctx.inventory.populate().filter(COINS).population(true) >= requiredCoins;
    }

    public boolean openShop() {
        if (!task.teleportHome()) {
            return false;
        }
        if (isShopOpen()) {
            return true;
        }
        if (!ctx.pathing.onTile(SHOP_TILE)) {
            ctx.menuActions.step(SHOP_TILE);
            ctx.sleep(500, 1000);
            return false;
        }

        final SimpleNpc shopNpc = ctx.npcs.populate().filter(SHOP_KEEPER_ID).next();
        if (shopNpc != null) {
            shopNpc.menuAction("Trade");
            return ctx.onCondition(this::isShopOpen, 250, 10);
        }

        return false;
    }

    public boolean closeShop() {
        if (!isShopOpen()) {
            return true;
        }

        final SimpleWidget widget = ctx.widgets.getWidget(SHOP_WIDGET_ID, 10);
        if (widget != null) {
            widget.click(1);
            return ctx.onCondition(() -> !isShopOpen(), 500, 5);
        }

        return false;
    }

    private SimpleWidget getShopWidget() {
        return ctx.widgets.getWidget(SHOP_WIDGET_ID, 14);
    }

    private boolean isShopOpen() {
        return isShopOpen(getShopWidget());
    }

    private boolean isShopOpen(final SimpleWidget shopWidget) {
        return shopWidget != null && !shopWidget.isHidden();
    }

    private String getShopName(final SimpleWidget shopWidget) {
        if (!isShopOpen(shopWidget)) {
            return null;
        }
        return shopWidget.getText();
    }

    public boolean hasItem() {
        return this.hasItem;
    }


}
