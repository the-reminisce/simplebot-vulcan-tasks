package me.remie.vulcan.leaguetasks.task;

import me.remie.vulcan.leaguetasks.LeagueScript;
import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.helpers.TeleporterScrollHelper;
import me.remie.vulcan.leaguetasks.task.requirement.ItemRequirement;
import me.remie.vulcan.leaguetasks.task.requirement.SkillRequirement;
import simple.hooks.simplebot.ChatMessage;
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
        return this.name;
    }

    public boolean isCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public void setSkillRequirement(SkillRequirement skillRequirement) {
        this.skillRequirement = skillRequirement;
    }

    public void setItemRequirement(ItemRequirement itemRequirement) {
        this.itemRequirement = itemRequirement;
    }

    public Optional<SkillRequirement> getSkillRequirement() {
        return Optional.ofNullable(this.skillRequirement);
    }

    public Optional<ItemRequirement> getItemRequirement() {
        return Optional.ofNullable(this.itemRequirement);
    }

    public boolean canComplete() {
        if (this.skillRequirement != null) {
            if (!this.skillRequirement.isMet()) {
                return false;
            }
        }
        if (this.itemRequirement != null) {
            return this.itemRequirement.isMet();
        }
        return true;
    }

    public abstract void run();

    public void onChatMessage(final ChatMessage e) {
    }

    // Helpers

    public TeleporterScrollHelper getTeleporterScrollHelper() {
        return this.script.getTeleporterScrollHelper();
    }

    public boolean isAtHome() {
        return LeagueScriptConstants.HOME_AREA.within();
    }

    public boolean teleportHome() {
        if (isAtHome()) {
            return true;
        }
        script.setScriptStatus("Teleporting home");
        ctx.chat.sendMessage("::home");
        return ctx.onCondition(this::isAtHome, 350, 10);
    }

}
