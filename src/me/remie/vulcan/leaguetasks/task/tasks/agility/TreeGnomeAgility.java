package me.remie.vulcan.leaguetasks.task.tasks.agility;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.utils.WorldArea;

/**
 * Created by Reminisce on Mar 04, 2024 at 12:35 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TreeGnomeAgility extends LeagueTask {

    private final WorldPoint PIPE_LOCATION = new WorldPoint(2484, 3430, 0);
    private final WorldArea GNOME_PIPE_AREA = new WorldArea(new WorldPoint(2489, 3431, 0), new WorldPoint(2482, 3427, 0));
    private final WorldArea GNOME_LOG_AREA = new WorldArea(new WorldPoint(2467, 3441, 0), new WorldPoint(2489, 3435, 0));
    private final WorldArea GNOME_NET_AREA = new WorldArea(new WorldPoint(2469, 3431, 0), new WorldPoint(2478, 3423, 0));
    private final WorldArea GNOME_NET_AREA_2 = new WorldArea(new WorldPoint(2489, 3426, 0), new WorldPoint(2481, 3418, 0));

    public TreeGnomeAgility(final LeagueScript script) {
        super(script, "Complete the Gnome Stronghold Agility Course");
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted() && ctx.skills.realLevel(SimpleSkills.Skills.AGILITY) >= 10;
    }

    @Override
    public void run() {
        if (!ctx.pathing.regionLoaded(LeagueScriptConstants.GNOME_STRONGHOLD_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                ctx.teleporter.teleportStringPath("Skilling", "Agility Area");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.GNOME_STRONGHOLD_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        if (ctx.players.getLocal().isAnimating()) {
            return;
        }
        final int plane = ctx.pathing.plane();
        if (GNOME_LOG_AREA.within()) {
            SimpleObject object = ctx.objects.populate().filter("Log balance").nextNearest();
            if (object != null) {
                object.menuAction("Walk-across");
                ctx.onCondition(() -> !ctx.pathing.inArea(GNOME_LOG_AREA), 200, 10);
            }
        } else if (GNOME_NET_AREA.within()) {
            SimpleObject object = ctx.objects.populate().filter("Obstacle net").nextNearest();
            if (object != null) {
                object.menuAction("Climb-over");
                ctx.onCondition(() -> ctx.pathing.plane() == 1, 200, 10);
            }
        } else if (plane == 1) {
            SimpleObject object = ctx.objects.populate().filter("Tree branch").nextNearest();
            if (object != null) {
                object.menuAction("Climb");
                ctx.onCondition(() -> ctx.pathing.plane() == 2, 200, 10);
            }
        } else if (ctx.pathing.reachable(new WorldPoint(2473, 3420, 2))) {
            SimpleObject object = ctx.objects.populate().filter("Balancing rope").nextNearest();
            if (object != null) {
                object.menuAction("Walk-on");
                ctx.onCondition(() -> !ctx.pathing.reachable(new WorldPoint(2473, 3420, 2)), 200, 10);
            }
        } else if (ctx.pathing.reachable(new WorldPoint(2483, 3420, 2))) {
            SimpleObject object = ctx.objects.populate().filter("Tree branch").nextNearest();
            if (object != null) {
                object.menuAction("Climb-down");
                ctx.onCondition(() -> !ctx.pathing.reachable(new WorldPoint(2483, 3420, 2)), 200, 10);
            }
        } else if (GNOME_NET_AREA_2.within()) {
            SimpleObject object = ctx.objects.populate().filter("Obstacle net").nextNearest();
            if (object != null) {
                object.menuAction("Climb-over");
                ctx.onCondition(() -> !ctx.pathing.inArea(GNOME_NET_AREA_2), 200, 10);
            }
        } else if (GNOME_PIPE_AREA.within()) {
            SimpleObject object = ctx.objects.populate().filter("Obstacle pipe").filterWithin(PIPE_LOCATION, 1).nextNearest();
            if (object != null) {
                object.menuAction("Squeeze-through");
                ctx.onCondition(() -> !ctx.pathing.inArea(GNOME_PIPE_AREA), 200, 10);
            }
        } else if (plane == 0  && !GNOME_LOG_AREA.within()) {
            ctx.pathing.step(GNOME_LOG_AREA.getCenterPoint());
        }
    }

}
