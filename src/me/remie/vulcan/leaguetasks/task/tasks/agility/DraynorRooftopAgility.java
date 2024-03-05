package me.remie.vulcan.leaguetasks.task.tasks.agility;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 12:54 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class DraynorRooftopAgility extends LeagueTask {

    public static final int DRAYNOR_REGION_ID = 12338;

    /**
     * While completing this task, we will also complete the following tasks:
     * - Complete 10 Laps of the Draynor Agility Course
     * - Complete a Rooftop Agility Course
     */
    public DraynorRooftopAgility(final LeagueScript script) {
        super(script, "Complete 10 Laps of the Draynor Agility Course");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.AGILITY, 10)));
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(DRAYNOR_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Cities", "Draynor village");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(DRAYNOR_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        if (ctx.players.getLocal().isAnimating()) {
            return;
        }
        final int plane = ctx.pathing.plane();
        //TODO: Implement Draynor Rooftop Agility
    }

}
