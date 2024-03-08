package me.remie.vulcan.leaguetasks.task.tasks.woodcutting;

import simple.robot.utils.WorldArea;

/**
 * Created by Reminisce on Mar 08, 2024 at 12:11 AM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public enum WoodcuttingTree {

    NORMAL("Tree", 1, new WorldArea(1615, 3505, 42, 14, 0)),
    OAK("Oak tree", 15, new WorldArea(1615, 3505, 39, 12, 0)),
    WILLOW("Willow tree", 30, new WorldArea(1625, 3493, 20, 12, 0)),
    MAPLE("Maple tree", 45, new WorldArea(1608, 3489, 12, 13, 0)),
    YEW("Yew tree", 60, new WorldArea(1588, 3483, 12, 15, 0)),
    MAGIC("Magic tree", 75, new WorldArea(1577, 3479, 24, 19, 0));

    private final String treeName;
    private final int requiredLevel;
    private final WorldArea area;

    WoodcuttingTree(final String treeName, final int requiredLevel, final WorldArea area) {
        this.treeName = treeName;
        this.requiredLevel = requiredLevel;
        this.area = area;
    }

    public String getTreeName() {
        return treeName;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public WorldArea getArea() {
        return area;
    }
}
