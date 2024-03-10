package me.remie.vulcan.leaguetasks.data;

import net.runelite.api.coords.WorldPoint;
import simple.robot.utils.WorldArea;

/**
 * Created by Reminisce on Mar 04, 2024 at 11:10 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class LeagueScriptConstants {

    // NPC ID's
    public static final int PILLORY_GUARD_ID = 380;
    public static final int BANKER_NPC_ID = 3887;

    // Item ID's
    public static final int COINS_ID = 995;

    // Region ID's
    public static final int LUNAR_ISLE_REGION_ID = 8253;
    public static final int LUMBRIDGE_REGION_ID = 12850;
    public static final int CATHERBY_REGION_ID = 11061;
    public static final int GNOME_STRONGHOLD_REGION_ID = 9781;

    // Areas
    public static final WorldArea HOME_AREA = new WorldArea(new WorldPoint(3072, 3521, 0), new WorldPoint(3072, 3464, 0), new WorldPoint(3137, 3474, 0), new WorldPoint(3137, 3521, 0));

    public static final WorldArea WOODCUTTING_GUILD_AREA = new WorldArea(
            new WorldPoint(1653, 3517, 0),
            new WorldPoint(1632, 3517, 0),
            new WorldPoint(1631, 3519, 0),
            new WorldPoint(1623, 3519, 0),
            new WorldPoint(1620, 3516, 0),
            new WorldPoint(1611, 3516, 0),
            new WorldPoint(1603, 3511, 0),
            new WorldPoint(1603, 3506, 0),
            new WorldPoint(1607, 3506, 0),
            new WorldPoint(1607, 3501, 0),
            new WorldPoint(1600, 3501, 0),
            new WorldPoint(1600, 3504, 0),
            new WorldPoint(1581, 3504, 0),
            new WorldPoint(1576, 3499, 0),
            new WorldPoint(1564, 3499, 0),
            new WorldPoint(1562, 3497, 0),
            new WorldPoint(1562, 3477, 0),
            new WorldPoint(1581, 3477, 0),
            new WorldPoint(1586, 3472, 0),
            new WorldPoint(1595, 3472, 0),
            new WorldPoint(1596, 3473, 0),
            new WorldPoint(1596, 3479, 0),
            new WorldPoint(1601, 3485, 0),
            new WorldPoint(1601, 3497, 0),
            new WorldPoint(1607, 3497, 0),
            new WorldPoint(1607, 3491, 0),
            new WorldPoint(1612, 3487, 0),
            new WorldPoint(1617, 3487, 0),
            new WorldPoint(1623, 3493, 0),
            new WorldPoint(1631, 3493, 0),
            new WorldPoint(1633, 3492, 0),
            new WorldPoint(1633, 3489, 0),
            new WorldPoint(1648, 3489, 0),
            new WorldPoint(1656, 3497, 0),
            new WorldPoint(1656, 3502, 0),
            new WorldPoint(1658, 3502, 0),
            new WorldPoint(1658, 3507, 0),
            new WorldPoint(1655, 3510, 0),
            new WorldPoint(1655, 3517, 0)
    );

    public static final WorldArea CATHERBY_BANK_AREA = new WorldArea(
            new WorldPoint(2805, 3443, 0),
            new WorldPoint(2813, 3437,0)
    );
}
