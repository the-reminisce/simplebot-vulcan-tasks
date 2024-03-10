package me.remie.vulcan.leaguetasks.task.tasks.travel;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleObject;

/**
 * Created by Reminisce on Mar 04, 2024 at 9:13 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TravelDeathsDomain extends LeagueTask {

    private final int DEATHS_COFFIN_ID = 39535;
    private final int DEATHS_DOMAIN_REGION_ID = 12633;
    private final WorldPoint DEATHS_DOMAIN_COFFIN = new WorldPoint(3093, 3510, 0);

    public TravelDeathsDomain(final LeagueScript script) {
        super(script, "Visit Death's Domain");
    }

    @Override
    public void run() {
        if (!teleportHome()) {
            return;
        }
        if (ctx.pathing.distanceTo(DEATHS_DOMAIN_COFFIN) >= 5) {
            script.setScriptStatus("Navigating to Death's domain");
            ctx.pathing.step(DEATHS_DOMAIN_COFFIN);
            ctx.sleep(500, 1000);
            return;
        }
        final SimpleObject coffin = ctx.objects.populate().filter(DEATHS_COFFIN_ID).nextNearest();
        if (coffin == null) {
            return;
        }
        script.setScriptStatus("Entering Death's domain");
        coffin.menuAction("Enter");
        ctx.onCondition(() -> ctx.pathing.regionLoaded(DEATHS_DOMAIN_REGION_ID) || isCompleted(), 250, 10);
    }

}
