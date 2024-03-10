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
public class EmoteExplore extends LeagueTask {

    public EmoteExplore(final LeagueScript script) {
        super(script, "Use the Explore Emote");
    }

    @Override
    public void run() {
        script.setScriptStatus("Performing Explorer emote");
        ctx.game.tab(Game.Tab.EMOTES);
        ctx.sleep(1000);
        ctx.menuActions.sendAction(57, 49, 14155778, 1, "Perform", "<col=ff9040>Explore</col>");
        ctx.sleep(1000);
    }

}
