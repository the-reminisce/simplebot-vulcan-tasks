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
public class EquipElementalStaff extends LeagueTask {

    private final int STAFF_OF_FIRE = 1387;

    public EquipElementalStaff(final LeagueScript script) {
        super(script, "Equip an Elemental Staff");
        setItemRequirement(new ItemRequirement(ctx, Map.of(STAFF_OF_FIRE, 1)));
    }

    @Override
    public void run() {
        final SimpleItem staff = ctx.inventory.populate().filter(STAFF_OF_FIRE).next();
        if (staff != null) {
            staff.menuAction("Wear");
            ctx.sleep(1000);
        }
    }

}
