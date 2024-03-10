package me.remie.vulcan.leaguetasks.task.tasks.travel;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.wrappers.SimpleObject;

/**
 * Created by Reminisce on Mar 06, 2024 at 7:38 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TravelFossilIsland extends LeagueTask {

    private final int CAVE_EXIT_ID = 30878;
    private final int WYVERN_CAVERN_REGION_ID = 14495;
    private final int FOSSIL_ISLAND_REGION_ID = 14907;

    public TravelFossilIsland(final LeagueScript script) {
        super(script, "Travel to Fossil Island");
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(WYVERN_CAVERN_REGION_ID) && !ctx.pathing.regionLoaded(FOSSIL_ISLAND_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Ancient wyvern");
                ctx.teleporter.teleportStringPath("Dungeons", "Ancient wyvern cave");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(WYVERN_CAVERN_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        if (ctx.pathing.regionLoaded(WYVERN_CAVERN_REGION_ID)) {
            final SimpleObject caveExit = ctx.objects.populate().filter(CAVE_EXIT_ID).nextNearest();
            if (caveExit == null) {
                return;
            }
            script.setScriptStatus("Exiting cave");
            caveExit.menuAction("Exit");
            ctx.onCondition(() -> ctx.pathing.regionLoaded(FOSSIL_ISLAND_REGION_ID) || isCompleted(), 250, 10);
            return;
        }
        //Teleport back home as we're already on Fossil Island
        script.setScriptStatus("Teleporting home");
        teleportHome();
    }

}
