package me.remie.vulcan.leaguetasks.task.tasks.travel;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;

/**
 * Created by Reminisce on Mar 06, 2024 at 7:47 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TravelMosleHarmless extends LeagueTask {

    private final int MOS_LE_HARMLESS_REGION_ID = 14906;

    public TravelMosleHarmless(final LeagueScript script) {
        super(script, "Visit Mos Le'Harmless");
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(MOS_LE_HARMLESS_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Mos le harmless");
                ctx.teleporter.teleportStringPath("Cities", "Mos le harmless");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(MOS_LE_HARMLESS_REGION_ID), 250, 10);
                return;
            }
        }
        //Teleport back home as we're already at Mos Le'Harmless
        script.setScriptStatus("Teleporting home");
        teleportHome();
    }

}
