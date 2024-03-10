package me.remie.vulcan.leaguetasks.task.tasks.agility;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.utils.WorldArea;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 12:48 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class VarrockRooftopAgility extends LeagueTask {

    public static final int VARROCK_REGION_ID = 12853;

    private final WorldArea ROUGH_WALL_AREA = new WorldArea(3221, 3412, 2, 5, 0);

    private final WorldArea CLOTHES_LINE_AREA = new WorldArea(3212, 3409, 10, 13, 3);

    private final WorldArea LEAP_GAP_AREA_1 = new WorldArea(3201, 3411, 10, 10, 3);

    private final WorldArea BALANCE_WALL_AREA = new WorldArea(3193, 3415, 6, 3, 1);

    private final WorldArea LEAP_GAP_AREA_2 = new WorldArea(3192, 3402, 7, 5, 3);

    private final WorldArea LEAP_GAP_AREA_3 = new WorldArea(3182, 3382, 28, 23, 3);

    private final WorldArea LEAP_GAP_AREA_4 = new WorldArea(3218, 3393, 16, 11, 3);

    private final WorldArea LEDGE_AREA = new WorldArea(3236, 3403, 5, 7, 3);

    private final WorldArea EDGE_AREA = new WorldArea(3236, 3410, 5, 7, 3);


    /**
     * While completing this task, we will also complete the following tasks:
     * - Complete 10 Laps of the Varrock Agility Course
     * - Complete the Varrock Agility Course
     * - Achieve Your First Level 40
     */
    public VarrockRooftopAgility(final LeagueScript script) {
        super(script, "Complete 10 Laps of the Varrock Agility Course");
        setSkillRequirement(new SkillRequirement(script.ctx, Map.of(SimpleSkills.Skills.AGILITY, 30)));
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted() && ctx.skills.realLevel(SimpleSkills.Skills.AGILITY) >= 40;
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(VARROCK_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Varrock");
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
        if (plane == 0) {
            if (ROUGH_WALL_AREA.within()) {
                final SimpleObject object = ctx.objects.populate().filter("Rough wall").nextNearest();
                if (object != null) {
                    script.setScriptStatus("Climbing up wall");
                    object.menuAction("Climb");
                    ctx.onCondition(() -> !ctx.pathing.inArea(ROUGH_WALL_AREA), 200, 10);
                }
            } else {
                ctx.pathing.step(ROUGH_WALL_AREA.getCenterPoint());
                ctx.sleep(500, 1000);
                return;
            }
        } else if (CLOTHES_LINE_AREA.within()) {
            final SimpleObject object = ctx.objects.populate().filter("Clothes line").nextNearest();
            if (object != null) {
                script.setScriptStatus("Balancing across the line");
                object.menuAction("Cross");
                ctx.onCondition(() -> ctx.pathing.inArea(LEAP_GAP_AREA_1), 200, 10);
            }
        } else if (ctx.pathing.inArea(LEAP_GAP_AREA_1)) {
            final SimpleObject object = ctx.objects.populate().filter("Gap").nextNearest();
            if (object != null) {
                script.setScriptStatus("Jumping down");
                object.menuAction("Leap");
                ctx.onCondition(() -> ctx.pathing.inArea(BALANCE_WALL_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(BALANCE_WALL_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Wall").nextNearest();
            if (object != null) {
                script.setScriptStatus("Get across the wall");
                object.menuAction("Balance");
                ctx.onCondition(() -> ctx.pathing.inArea(LEAP_GAP_AREA_2), 200, 10);
            }
        } else if (ctx.pathing.inArea(LEAP_GAP_AREA_2)) {
            final SimpleObject object = ctx.objects.populate().filter("Gap").filter(14833).nextNearest();
            if (object != null) {
                script.setScriptStatus("Leap to next building");
                object.menuAction("Leap");
                ctx.onCondition(() -> ctx.pathing.inArea(LEAP_GAP_AREA_3), 200, 10);
            }
        } else if (ctx.pathing.inArea(LEAP_GAP_AREA_3)) {
            final SimpleObject object = ctx.objects.populate().filter("Gap").filter(14834).nextNearest();
            if (object != null) {
                script.setScriptStatus("Crazy parkour");
                object.menuAction("Leap");
                ctx.onCondition(() -> ctx.pathing.inArea(LEAP_GAP_AREA_4), 200, 10);
            }
        } else if (ctx.pathing.inArea(LEAP_GAP_AREA_4)) {
            final SimpleObject object = ctx.objects.populate().filter("Gap").filter(14835).nextNearest();
            if (object != null) {
                script.setScriptStatus("Leap to next building");
                object.menuAction("Leap");
                ctx.onCondition(() -> ctx.pathing.inArea(LEDGE_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(LEDGE_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Ledge").nextNearest();
            if (object != null) {
                script.setScriptStatus("Climb ledge");
                object.menuAction("Hurdle");
                ctx.onCondition(() -> ctx.pathing.inArea(EDGE_AREA), 200, 10);
            }
        } else if (ctx.pathing.inArea(EDGE_AREA)) {
            final SimpleObject object = ctx.objects.populate().filter("Edge").nextNearest();
            if (object != null) {
                script.setScriptStatus("Jumping off building");
                object.menuAction("Jump-off");
                ctx.onCondition(() -> ctx.pathing.plane() == 0, 200, 10);
            }
        }
    }

}
