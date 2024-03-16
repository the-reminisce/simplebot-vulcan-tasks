package me.remie.vulcan.leaguetasks.task.tasks.thieving;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.utils.WorldArea;

import java.util.Map;

/**
 * Created by Reminisce on Mar 05, 2024 at 9:12 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ThievingPickpocketKnight50 extends LeagueTask {

    private final WorldArea ARDOUGNE_AREA = new WorldArea(2660, 3305, 50, 50, 0);
    private int successfulPickpockets = 0;

    public ThievingPickpocketKnight50(final LeagueScript script) {
        super(script, "Pickpocket a Knight of Ardougne 50 Times");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.THIEVING, 55)));
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.ARDOUGNE_REGION_ID) || !ARDOUGNE_AREA.within()) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Cities", "Ardougne");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.ARDOUGNE_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        final SimpleNpc knight = ctx.npcs.populate().filter("Knight of Ardougne").nextNearest();
        if (knight != null) {
            final int cachedXp = ctx.skills.experience(SimpleSkills.Skills.THIEVING);
            knight.menuAction("Pickpocket");
            ctx.onCondition(() -> {
                boolean success = ctx.skills.experience(SimpleSkills.Skills.THIEVING) > cachedXp;
                if (success) {
                    successfulPickpockets++; // Increment the counter on success
                    System.out.println("Successful pickpockets: " + successfulPickpockets);
                }
                return success || isCompleted();
            }, 350, 10);
        }
    }
}