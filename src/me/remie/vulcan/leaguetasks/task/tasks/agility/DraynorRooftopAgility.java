package me.remie.vulcan.leaguetasks.task.tasks.agility;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.utils.WorldArea;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 12:54 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class DraynorRooftopAgility extends LeagueTask {

    private static final int DRAYNOR_REGION_ID = 12338;

    private final WorldArea ROUGH_WALL_AREA = new WorldArea(3103, 3277, 3, 5, 0);

    private final WorldArea TIGHT_ROPE_AREA = new WorldArea(3097, 3277, 6, 5, 3);

    private final WorldArea TIGHT_ROPE_AREA_2 = new WorldArea(
            new WorldPoint(3090, 3272, 3),
            new WorldPoint(3087, 3275, 3),
            new WorldPoint(3091, 3279, 3),
            new WorldPoint(3094, 3276, 3)
    );

    private final WorldArea NARROW_WALL_AREA = new WorldArea(3089, 3265, 7, 4, 3);
    private final WorldArea WALL_AREA = new WorldArea(3083, 3256, 6, 6, 3);
    private final WorldArea GAP_AREA = new WorldArea(3087, 3251, 8, 5, 3);
    private final WorldArea CRATE_AREA = new WorldArea(3096, 3256, 6, 6, 3);

    /**
     * While completing this task, we will also complete the following tasks:
     * - Complete 10 Laps of the Draynor Agility Course
     * - Complete a Rooftop Agility Course
     * - Obtain a Mark of Grace
     * - Achieve Your First Level 30
     */
    public DraynorRooftopAgility(final LeagueScript script) {
        super(script, "Complete 10 Laps of the Draynor Agility Course");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.AGILITY, 10)));
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted() && ctx.skills.realLevel(SimpleSkills.Skills.AGILITY) >= 30;
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(DRAYNOR_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Draynor village");
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
        if (plane == 0) {
            if (ROUGH_WALL_AREA.within()) {
                final SimpleObject object = ctx.objects.populate().filter("Rough wall").nextNearest();
                if (object != null) {
                    script.setScriptStatus("Climbing up rough wall");
                    object.menuAction("Climb");
                    ctx.onCondition(() -> !ctx.pathing.inArea(ROUGH_WALL_AREA), 200, 10);
                }
            } else {
                ctx.pathing.step(ROUGH_WALL_AREA.getCenterPoint());
                ctx.sleep(500, 1000);
                return;
            }
        } else if (TIGHT_ROPE_AREA.within() || TIGHT_ROPE_AREA_2.within()) {
            final SimpleObject object = ctx.objects.populate().filter("Tightrope").nextNearest();
            if (object != null) {
                script.setScriptStatus("Cross tightrope");
                object.menuAction("Cross");
                ctx.onCondition(() -> ctx.pathing.inArea(NARROW_WALL_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(NARROW_WALL_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Narrow Wall").nextNearest();
            if (object != null) {
                script.setScriptStatus("Balance narrow wall");
                object.menuAction("Balance");
                ctx.onCondition(() -> ctx.pathing.inArea(WALL_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(WALL_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Wall").nextNearest();
            if (object != null) {
                script.setScriptStatus("Climb wall");
                object.menuAction("Jump-up");
                ctx.onCondition(() -> ctx.pathing.inArea(GAP_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(GAP_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Gap").nextNearest();
            if (object != null) {
                script.setScriptStatus("Jumping down");
                object.menuAction("Jump");
                ctx.onCondition(() -> ctx.pathing.inArea(CRATE_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(CRATE_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Crate").nextNearest();
            if (object != null) {
                script.setScriptStatus("Jumping down");
                object.menuAction("Climb-down");
                ctx.onCondition(() -> ctx.pathing.plane() == 0, 200, 10);
            }
        }
    }

}
