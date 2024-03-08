package me.remie.vulcan.leaguetasks.task.tasks.thieving;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:19 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ThievingLevel20 extends LeagueTask {

    /**
     * This task will achieve our first level 20 stat via thieving.
     * While completing this task, we will also complete the following tasks:
     * - Achieve Your First Level 20
     * - Achieve Your First Level 10
     * - Achieve Your First Level 5
     * - Achieve Your First Level Up
     */
    public ThievingLevel20(final LeagueScript script) {
        super(script, "Achieve Your First Level 20");
    }

    @Override
    public void run() {
        if (!teleportHome()) {
            return;
        }
        if (ctx.inventory.inventoryFull()) {
            if (!ctx.bank.bankOpen()) {
                final SimpleNpc banker = ctx.npcs.populate().filter(3887).nextNearest();
                if (banker != null) {
                    banker.menuAction("Bank");
                    ctx.onCondition(ctx.bank::bankOpen, 250, 10);
                }
                return;
            } else {
                for (final SimpleItem item : ctx.inventory.populate()) {
                    if (item.getId() == 995) {
                        continue;
                    }
                    item.menuAction("Deposit-All");
                    ctx.sleep(250, 500);
                }
                ctx.sleep(1250);
                ctx.bank.closeBank();
            }
            return;
        }

        final SimpleObject stall = ctx.objects.populate().filter(getStallId()).nextNearest();
        if (stall == null) {
            return;
        }
        if (ctx.pathing.distanceTo(stall.getLocation()) > 1) {
            ctx.pathing.step(stall.getLocation());
            ctx.sleep(500, 1000);
            return;
        }
        stall.menuAction("Steal from");
        ctx.sleep(1250);
    }

    private int getStallId() {
        final int level = ctx.skills.realLevel(SimpleSkills.Skills.THIEVING);
        if (level >= 75) {
            return 4878;
        } else if (level >= 65) {
            return 4877;
        } else if (level >= 50) {
            return 4876;
        } else if (level >= 20) {
            return 4875;
        }
        return 4874;
    }

}
