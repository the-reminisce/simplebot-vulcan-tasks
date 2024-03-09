package me.remie.vulcan.leaguetasks.task.tasks;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.helpers.ItemDetails;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopHelper;
import me.remie.vulcan.leaguetasks.helpers.shop.ShopType;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.wrappers.SimpleItem;

/**
 * Created by Reminisce on Mar 06, 2024 at 11:31 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class EquipGodBook extends LeagueTask {

    private static final int UNHOLY_BOOK = 3840;
    private static final ItemDetails itemDetails = new ItemDetails("Unholy book", UNHOLY_BOOK, 45_000);

    private final ShopHelper shopHelper;

    public EquipGodBook(final LeagueScript script) {
        super(script, "Equip a Completed God Book");
        this.shopHelper = new ShopHelper(this);
    }

    @Override
    public void run() {
        if (!teleportHome()) {
            return;
        }
        if (!ctx.inventory.populate().filter(UNHOLY_BOOK).isEmpty()) {
            final SimpleItem book = ctx.inventory.next();
            if (book != null) {
                book.menuAction("Wear");
                ctx.onCondition(this::isCompleted, 250, 10);
            }
            return;
        }
        shopHelper.buyItem(itemDetails, ShopType.MAGIC);
    }

}
