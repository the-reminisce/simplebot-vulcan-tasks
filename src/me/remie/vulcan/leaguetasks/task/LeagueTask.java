package me.remie.vulcan.leaguetasks.task;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.task.requirement.ItemRequirement;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.robot.api.ClientContext;

import java.util.Optional;

/**
 * Created by Reminisce on Mar 04, 2024 at 8:54 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public abstract class LeagueTask {

    public final LeagueScript script;
    public final ClientContext ctx;

    private boolean isCompleted;
    private final String name;
    private SkillRequirement skillRequirement;
    private ItemRequirement itemRequirement;

    public LeagueTask(final LeagueScript script, final String name) {
        this.script = script;
        this.ctx = script.ctx;
        this.name = name;
        this.isCompleted = false;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setSkillRequirement(SkillRequirement skillRequirement) {
        this.skillRequirement = skillRequirement;
    }

    public void setItemRequirement(ItemRequirement itemRequirement) {
        this.itemRequirement = itemRequirement;
    }

    public Optional<SkillRequirement> getSkillRequirement() {
        return Optional.ofNullable(skillRequirement);
    }

    public Optional<ItemRequirement> getItemRequirement() {
        return Optional.ofNullable(itemRequirement);
    }

    public boolean canComplete() {
        if (skillRequirement != null) {
            if (!skillRequirement.isMet()) {
                return false;
            }
        }
        if (itemRequirement != null) {
            return itemRequirement.isMet();
        }
        return true;
    }

    public abstract void run();

    // Helpers

    public boolean isAtHome() {
        return LeagueScriptConstants.HOME_AREA.within();
    }

    public boolean teleportHome() {
        if (isAtHome()) {
            return true;
        }
        ctx.chat.sendMessage("::home");
        return ctx.onCondition(this::isAtHome, 350, 10);
    }

}
