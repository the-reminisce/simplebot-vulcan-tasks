package me.remie.vulcan.leaguetasks.task.tasks.thieving;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleObject;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:19 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ThievingStealTea extends LeagueTask {

    public ThievingStealTea(final LeagueScript script) {
        super(script, "Steal From the Varrock Tea Stall");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.THIEVING, 5)));
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.VARROCK_SQUARE_REGION_ID) && !ctx.pathing.regionLoaded(LeagueScriptConstants.VARROCK_EAST_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Cities", "Varrock");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.VARROCK_SQUARE_REGION_ID), 250, 10);
                return;
            }
        }
        if (ctx.pathing.regionLoaded(LeagueScriptConstants.VARROCK_SQUARE_REGION_ID) || ctx.pathing.regionLoaded(LeagueScriptConstants.VARROCK_EAST_REGION_ID)) {
            final SimpleObject teaStall = ctx.objects.populate().filter(635).nextNearest();
            if (teaStall != null) {
                teaStall.menuAction("Steal-from");
                ctx.onCondition(() -> ctx.players.getLocal().isAnimating(), 250, 10);
            } else {
                ctx.pathing.step(ctx.players.getLocal().getLocation().getX() + 20, ctx.players.getLocal().getLocation().getY());
                ctx.sleep(1000, 3000);
            }
        }
    }

}
