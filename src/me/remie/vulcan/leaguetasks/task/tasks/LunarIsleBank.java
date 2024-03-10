package me.remie.vulcan.leaguetasks.task.tasks;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleObject;

/**
 * Created by Reminisce on Mar 06, 2024 at 11:14 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class LunarIsleBank extends LeagueTask {

    private static final WorldPoint BANK_TILE = new WorldPoint(2099, 3919, 0);

    public LunarIsleBank(final LeagueScript script) {
        super(script, "Use the Bank on Lunar Isle");
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.LUNAR_ISLE_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Lunar isle");
                ctx.teleporter.teleportStringPath("Cities", "Lunar isle");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.LUNAR_ISLE_REGION_ID), 250, 10);
                return;
            }
        }
        if (!ctx.pathing.onTile(BANK_TILE)) {
            script.setScriptStatus("Navigating to bank");
            ctx.menuActions.step(BANK_TILE);
            ctx.sleep(500, 1000);
            return;
        }
        final SimpleObject bank = ctx.objects.populate().filter("Bank booth").nextNearest();
        if (bank == null) {
            return;
        }
        script.setScriptStatus("Opening up bank");
        bank.menuAction("Bank");
        if (ctx.onCondition(ctx.bank::bankOpen, 350, 10)) {
            ctx.bank.closeBank();
        }
    }

}
