package me.remie.vulcan.leaguetasks.task.tasks.agility;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 12:48 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class VarrockRooftopAgility extends LeagueTask {

    public static final int VARROCK_REGION_ID = 12853;

    /**
     * While completing this task, we will also complete the following tasks:
     * - Complete 10 Laps of the Varrock Agility Course
     * - Complete the Varrock Agility Course
     */
    public VarrockRooftopAgility(final LeagueScript script) {
        super(script, "Complete 10 Laps of the Varrock Agility Course");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.AGILITY, 30)));
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(VARROCK_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Cities", "Varrock");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(VARROCK_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        if (ctx.players.getLocal().isAnimating()) {
            return;
        }
        final int plane = ctx.pathing.plane();
        //TODO: Implement Varrock Rooftop Agility
    }

}
