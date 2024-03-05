package me.remie.vulcan.leaguetasks.task.tasks;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.requirement.ItemRequirement;
import simple.hooks.wrappers.SimpleItem;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 9:40 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class EquipMithrilGloves extends LeagueTask {

    private final int MITHRIL_GLOVES = 7458;

    public EquipMithrilGloves(final LeagueScript script) {
        super(script, "Equip Some Mithril Gloves");
        setItemRequirement(new ItemRequirement(ctx, Map.of(MITHRIL_GLOVES, 1)));
    }

    @Override
    public void run() {
        final SimpleItem mithrilGloves = ctx.inventory.populate().filter(MITHRIL_GLOVES).next();
        if (mithrilGloves != null) {
            mithrilGloves.menuAction("Wear");
            ctx.sleep(1000);
        }
    }

}
