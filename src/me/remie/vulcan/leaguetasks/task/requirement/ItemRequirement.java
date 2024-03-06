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

    public ItemRequirement(final ClientContext ctx, final Map<Integer, Integer> itemRequirements) {
        super(ctx);
        this.itemRequirements = itemRequirements;
    }

    @Override
    public boolean isMet() {
        final Map<Integer, Integer> items = ctx.inventory.populate().toStream()
                .collect(Collectors.groupingBy(SimpleItem::getId, Collectors.summingInt(SimpleItem::getQuantity)));
        for (final Map.Entry<Integer, Integer> entry : this.itemRequirements.entrySet()) {
            final int itemId = entry.getKey();
            final int requiredQuantity = entry.getValue();
            final int currentQuantity = items.getOrDefault(itemId, 0);
            if (currentQuantity < requiredQuantity) {
                return false;
            }
        }
        return true;
    }

}
