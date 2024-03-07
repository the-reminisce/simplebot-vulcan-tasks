package me.remie.vulcan.leaguetasks.task.tasks;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.tasks.thieving.ThievingLevel20;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;

/**
 * Created by Reminisce on Mar 06, 2024 at 11:31 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class EquipGodBook extends LeagueTask {

    private static final int COINS = 995;
    private static final int UNHOLY_BOOK = 3840;
    private static final int REQUIRED_COINS = 30_000;

    private static final int SHOP_KEEPER_ID = 7913;
    private static final int SHOP_WIDGET_ID = 836;
    private static final WorldPoint SHOP_TILE = new WorldPoint(3077, 3500, 0);

    private boolean checkedBank = false;
    private boolean hasEnoughCoins = false;

    public EquipGodBook(final LeagueScript script) {
        super(script, "Equip a Completed God Book");
    }

    @Override
    public void run() {
        if (!teleportHome()) {
            return;
        }
        if (!ctx.inventory.populate().filter(UNHOLY_BOOK).isEmpty()) {
            final SimpleItem book = ctx.inventory.next();
            if (book != null) {
                if (!closeShop()) {
                    return;
                }
                book.menuAction("Wear");
                ctx.onCondition(this::isCompleted, 250, 10);
            }
            return;
        }

        if (!checkedBank || !hasEnoughCoins) {
            checkAndManageCoins();
            return;
        }

        final SimpleWidget shopWidget = getShopWidget();
        if (isShopOpen(shopWidget)) {
            final String shopName = getShopName();
            if (shopName != null && shopName.equalsIgnoreCase("magic")) {
                ctx.menuActions.sendAction(57, 30, 54788137, 2, UNHOLY_BOOK, "Buy 1", "<col=ff9040>Unholy book</col>");
                ctx.onCondition(() -> !ctx.inventory.populate().filter(UNHOLY_BOOK).isEmpty(), 250, 10);
            } else {
                ctx.menuActions.clickButton(54788116);
                ctx.sleep(500, 1000);
            }
        } else {
            if (!ctx.pathing.onTile(SHOP_TILE)) {
                ctx.menuActions.step(SHOP_TILE);
                ctx.sleep(500, 1000);
                return;
            }
            final SimpleNpc shopNpc = ctx.npcs.populate().filter(SHOP_KEEPER_ID).next();
            if (shopNpc != null) {
                shopNpc.menuAction("Trade");
                ctx.onCondition(this::isShopOpen, 250, 10);
            }
        }
    }

    /**
     * Check if the player has enough coins in their inventory or bank and manage them accordingly
     * If the player has enough coins, set the hasEnoughCoins flag to true
     * If the player does not have enough coins, check the bank for coins and withdraw them if possible
     * If the player does not have enough coins and cannot withdraw them from the bank, run the ThievingLevel20 task to obtain coins
     * <p>
     * This method is honestly a bit of a mess and could be refactored to be more readable and maintainable.
     * Also, this method has the possibility to fail if we fail to withdraw the coins from the bank, but we don't handle that case yet.
     */
    private void checkAndManageCoins() {
        final int coinsInInventory = ctx.inventory.populate().filter(COINS).population(true);
        if (coinsInInventory >= REQUIRED_COINS) {
            hasEnoughCoins = true;
            return;
        }
        if (!checkedBank) {
            if (ctx.bank.openBank()) {
                ctx.sleep(500, 1000);
                final int coinsInBank = ctx.bank.populate().filter(COINS).population(true);
                if (coinsInInventory + coinsInBank >= REQUIRED_COINS) {
                    hasEnoughCoins = true;
                    final SimpleItem coins = ctx.bank.next();
                    if (coins != null) {
                        final int coinsToWithdraw = REQUIRED_COINS - coinsInInventory;
                        coins.menuAction("withdraw-x");
                        if (ctx.onCondition(ctx.dialogue::pendingInput, 250, 10)) {
                            ctx.keyboard.sendKeys(String.valueOf(coinsToWithdraw), true);
                        }
                        ctx.onCondition(() -> ctx.inventory.populate().filter(COINS).population(true) >= REQUIRED_COINS, 250, 10);
                    }
                }
                if (ctx.bank.closeBank()) {
                    checkedBank = true;
                }
            }
            return;
        }
        if (!hasEnoughCoins) {
            script.getTask(ThievingLevel20.class).run();
        }
    }

    private SimpleWidget getShopWidget() {
        return ctx.widgets.getWidget(836, 14);
    }

    private boolean isShopOpen() {
        return isShopOpen(getShopWidget());
    }

    private boolean isShopOpen(final SimpleWidget shopWidget) {
        return shopWidget != null && !shopWidget.isHidden();
    }

    private String getShopName() {
        final SimpleWidget shopWidget = getShopWidget();
        if (!isShopOpen(shopWidget)) {
            return null;
        }
        return shopWidget.getText();
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

}
