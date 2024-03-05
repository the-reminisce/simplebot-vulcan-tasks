package me.remie.vulcan.leaguetasks.task.tasks;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;

/**
 * Created by Reminisce on Mar 04, 2024 at 9:16 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class OpenLeaguesMenu extends LeagueTask {

    public OpenLeaguesMenu(final LeagueScript script) {
        super(script, "Open the Leagues Menu");
    }

    @Override
    public void run() {
        ctx.menuActions.clickButton(42991640);
        ctx.sleep(1000);
    }

}
