package me.remie.vulcan.leaguetasks.task.tasks.thieving;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.utils.WorldArea;

/**
 * Created by Reminisce on Mar 05, 2024 at 9:12 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ThievingPickpocketMan extends LeagueTask {

    private final WorldArea LUMBRIDGE_CASTLE_AREA = new WorldArea(3202, 3205, 24, 30, 0);

    public ThievingPickpocketMan(final LeagueScript script) {
        super(script, "Pickpocket a Citizen");
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.LUMBRIDGE_REGION_ID) || !LUMBRIDGE_CASTLE_AREA.within()) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleport to Lumbridge");
                ctx.teleporter.teleportStringPath("Cities", "Lumbridge");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.LUMBRIDGE_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        final SimpleNpc man = ctx.npcs.populate().filter("Man", "Woman").nextNearest();
        if (man != null) {
            script.setScriptStatus("Pickpocket " + man.getName());
            final int cachedXp = ctx.skills.experience(SimpleSkills.Skills.THIEVING);
            man.menuAction("Pickpocket");
            ctx.onCondition(() -> ctx.skills.experience(SimpleSkills.Skills.THIEVING) > cachedXp || isCompleted(), 350, 10);
        }
    }

}
