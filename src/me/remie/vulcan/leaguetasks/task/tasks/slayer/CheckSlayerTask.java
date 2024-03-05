package me.remie.vulcan.leaguetasks.task.tasks.slayer;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:02 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class CheckSlayerTask extends LeagueTask {

    public CheckSlayerTask(final LeagueScript script) {
        super(script, "Check Your Slayer Task");
    }

    @Override
    public boolean canComplete() {
        return script.isTaskCompleted(SlayerTaskDuradel.class);
    }

    @Override
    public void run() {
        // Nothing to do here as the task is completed by the SlayerTaskDuradel class
    }

}
