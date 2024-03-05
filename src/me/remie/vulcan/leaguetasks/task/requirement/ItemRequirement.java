package me.remie.vulcan.leaguetasks.task.requirement;

import simple.hooks.wrappers.SimpleItem;
import simple.robot.api.ClientContext;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Reminisce on Mar 04, 2024 at 8:59 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class ItemRequirement extends Requirement {

    private final Map<Integer, Integer> itemRequirements;

    public ItemRequirement(final ClientContext ctx, Map<Integer, Integer> itemRequirements) {
        super(ctx);
        this.itemRequirements = itemRequirements;
    }

    @Override
    public boolean isMet() {
        Map<Integer, Integer> items = ctx.inventory.populate().toStream()
                .collect(Collectors.groupingBy(SimpleItem::getId, Collectors.summingInt(SimpleItem::getQuantity)));
        for (Map.Entry<Integer, Integer> entry : itemRequirements.entrySet()) {
            int itemId = entry.getKey();
            int requiredQuantity = entry.getValue();
            int currentQuantity = items.getOrDefault(itemId, 0);
            if (currentQuantity < requiredQuantity) {
                return false;
            }
        }
        return true;
    }

}
