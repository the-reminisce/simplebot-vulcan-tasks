package me.remie.vulcan.leaguetasks.task.requirement;

import simple.hooks.filters.SimpleSkills;
import simple.robot.api.ClientContext;

import java.util.Map;

/**
 * Created by Reminisce on Mar 04, 2024 at 8:55 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class SkillRequirement extends Requirement {

    private final Map<SimpleSkills.Skills, Integer> skillRequirements;

    public SkillRequirement(final ClientContext ctx, final Map<SimpleSkills.Skills, Integer> skillRequirements) {
        super(ctx);
        this.skillRequirements = skillRequirements;
    }

    @Override
    public boolean isMet() {
        for (final Map.Entry<SimpleSkills.Skills, Integer> entry : this.skillRequirements.entrySet()) {
            final SimpleSkills.Skills skill = entry.getKey();
            final int requiredLevel = entry.getValue();
            final int currentLevel = ctx.skills.realLevel(skill);
            if (currentLevel < requiredLevel) {
                return false;
            }
        }
        return true;
    }

}
