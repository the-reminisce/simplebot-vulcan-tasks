package me.remie.vulcan.leaguetasks.task.tasks.emotes;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import simple.hooks.simplebot.Game;

/**
 * Created by Reminisce on Mar 04, 2024 at 9:13 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class EmoteUriTransformation extends LeagueTask {

    public EmoteUriTransformation(final LeagueScript script) {
        super(script, "Transform into Uri");
    }

    @Override
    public void run() {
        script.setScriptStatus("Performing transform into Uri");
        ctx.game.tab(Game.Tab.EMOTES);
        ctx.sleep(1000);
        ctx.menuActions.sendAction(57, 45, 14155778, 1, "Perform", "<col=ff9040>Uri transform</col>");
        ctx.sleep(1000);
    }

}
