package me.remie.vulcan.leaguetasks.task.tasks.travel;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.wrappers.SimpleObject;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:40 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TravelSpiritTrees extends LeagueTask {

    private final int TREE_GNOME_REGION_ID = 10033;

    /**
     * While completing this task, we will also complete the following tasks:
     * - Travel Between Your Spirit Trees
     * - Visit the Tree Gnome Stronghold
     */
    public TravelSpiritTrees(final LeagueScript script) {
        super(script, "Travel Between Your Spirit Trees");
    }

    @Override
    public void run() {
        if (ctx.pathing.regionLoaded(LeagueScriptConstants.GNOME_STRONGHOLD_REGION_ID)) {
            teleportHome();
            return;
        }
        if (!ctx.pathing.regionLoaded(TREE_GNOME_REGION_ID)) {
            if (!teleportHome()) {
                return;
            }
            if (ctx.teleporter.open()) {
                script.setScriptStatus("Teleporting to Tree gnome");
                ctx.teleporter.teleportStringPath("Cities", "Tree gnome");
                ctx.onCondition(() -> ctx.pathing.regionLoaded(TREE_GNOME_REGION_ID), 250, 10);
                return;
            }
            return;
        }
        final SimpleObject tree = ctx.objects.populate().filter(1293).nextNearest();
        if (tree == null) {
            return;
        }
        if (!getTeleporterScrollHelper().isOpen()) {
            script.setScriptStatus("Talk to tree");
            tree.menuAction("Talk-to");
            ctx.sleep(1000);
            return;
        }
        script.setScriptStatus("Teleporting");
        getTeleporterScrollHelper().teleport(1);
        ctx.onCondition(() -> ctx.pathing.regionLoaded(LeagueScriptConstants.GNOME_STRONGHOLD_REGION_ID) || isCompleted(), 250, 10);
    }

}
