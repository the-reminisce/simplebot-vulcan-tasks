package me.remie.vulcan.leaguetasks.task.tasks.thieving;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:19 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ThievingStealSilk extends LeagueTask {

    public ThievingStealSilk(final LeagueScript script) {
        super(script, "Steal some Silk");
    }

    @Override
    public void run() {
        script.getTask(ThievingLevel20.class).run();
    }

}
