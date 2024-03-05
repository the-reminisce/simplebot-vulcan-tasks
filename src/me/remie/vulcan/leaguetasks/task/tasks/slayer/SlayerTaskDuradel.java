package me.remie.vulcan.leaguetasks.task.tasks.slayer;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.wrappers.SimpleNpc;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:02 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class SlayerTaskDuradel extends LeagueTask {

    private final WorldPoint DURADEL_LOCATION = new WorldPoint(3077, 3512, 0);

    public SlayerTaskDuradel(final LeagueScript script) {
        super(script, "Receive a Slayer Task From Duradel");
    }

    @Override
    public void run() {
        if (!teleportHome()) {
            return;
        }
        if (ctx.pathing.distanceTo(DURADEL_LOCATION) >= 5) {
            ctx.pathing.step(DURADEL_LOCATION);
            ctx.sleep(500, 1000);
            return;
        }
        final SimpleNpc duradel = ctx.npcs.populate().filter("Duradel").nearest().next();
        if (duradel == null) {
            return;
        }
        duradel.menuAction("Assignment");
        ctx.onCondition(() -> ctx.dialogue.dialogueOpen() || isCompleted(), 250, 10);
    }

}
