package me.remie.vulcan.leaguetasks;

import me.remie.vulcan.leaguetasks.data.LeagueScriptConstants;
import me.remie.vulcan.leaguetasks.helpers.TeleporterScrollHelper;
import me.remie.vulcan.leaguetasks.task.LeagueTask;
import me.remie.vulcan.leaguetasks.task.tasks.CraftGhorrockTablet;
import me.remie.vulcan.leaguetasks.task.tasks.EquipElementalStaff;
import me.remie.vulcan.leaguetasks.task.tasks.EquipGodBook;
import me.remie.vulcan.leaguetasks.task.tasks.EquipMithrilGloves;
import me.remie.vulcan.leaguetasks.task.tasks.LunarIsleBank;
import me.remie.vulcan.leaguetasks.task.tasks.OpenLeaguesMenu;
import me.remie.vulcan.leaguetasks.task.tasks.agility.DraynorRooftopAgility;
import me.remie.vulcan.leaguetasks.task.tasks.agility.TreeGnomeAgility;
import me.remie.vulcan.leaguetasks.task.tasks.agility.VarrockRooftopAgility;
import me.remie.vulcan.leaguetasks.task.tasks.emotes.EmoteExplore;
import me.remie.vulcan.leaguetasks.task.tasks.emotes.EmoteUriTransformation;
import me.remie.vulcan.leaguetasks.task.tasks.fishing.FishAnchovies;
import me.remie.vulcan.leaguetasks.task.tasks.slayer.CheckSlayerTask;
import me.remie.vulcan.leaguetasks.task.tasks.slayer.SlayerTaskDuradel;
import me.remie.vulcan.leaguetasks.task.tasks.thieving.ThievingLevel20;
import me.remie.vulcan.leaguetasks.task.tasks.thieving.ThievingPickpocketMan;
import me.remie.vulcan.leaguetasks.task.tasks.thieving.ThievingStealSilk;
import me.remie.vulcan.leaguetasks.task.tasks.travel.TravelDeathsDomain;
import me.remie.vulcan.leaguetasks.task.tasks.travel.TravelFossilIsland;
import me.remie.vulcan.leaguetasks.task.tasks.travel.TravelMosleHarmless;
import me.remie.vulcan.leaguetasks.task.tasks.travel.TravelSpiritTrees;
import me.remie.vulcan.leaguetasks.task.tasks.woodcutting.tasks.ChopMagicLogs;
import me.remie.vulcan.leaguetasks.task.tasks.woodcutting.tasks.ChopNormalLogs;
import me.remie.vulcan.leaguetasks.task.tasks.woodcutting.tasks.ChopWillowLogs;
import me.remie.vulcan.leaguetasks.utils.LeaguePanel;

import net.runelite.api.ChatMessageType;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;

import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;
import simple.robot.utils.ScriptUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Reminisce on Mar 04, 2024 at 08:44 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
@ScriptManifest(author = "Reminisce", name = "RLeagues Tasks", category = Category.OTHER,
        version = "0.0.1", description = "Completes a ton of leagues tasks", discord = "reminisce", servers = {"Vulcan"}, vip = true)
public class LeagueScript extends Script implements LoopingScript, MouseListener {

    private long startTime;
    private String status;

    private boolean checkedTasks;
    private LeagueTask currentTask;
    private int pointsGained, pointsTotal;
    private List<LeagueTask> tasks;

    private LeagueScriptPaint paintHelper;

    private TeleporterScrollHelper teleporterScrollHelper;

    private final Pattern POINTS_PATTERN = Pattern.compile("you have earned ([\\d,]+) league points\\. you now have ([\\d,]+) league points\\.");

    private LeaguePanel leaguePanel;

    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
        this.leaguePanel = new LeaguePanel(ctx);
        this.status = "Waiting to start...";
        this.tasks = Arrays.asList(
                new OpenLeaguesMenu(this),
                new EmoteExplore(this),
                new EmoteUriTransformation(this),
                new EquipMithrilGloves(this),
                new EquipElementalStaff(this),
                new SlayerTaskDuradel(this),
                new CheckSlayerTask(this),
                new TravelDeathsDomain(this),
                new TravelSpiritTrees(this),
                new TravelFossilIsland(this),
                new TravelMosleHarmless(this),
                new LunarIsleBank(this),
                new CraftGhorrockTablet(this),
                new ThievingLevel20(this),
                new ThievingStealSilk(this),
                new EquipGodBook(this),
                new ThievingPickpocketMan(this),
                new FishAnchovies(this),
                new TreeGnomeAgility(this),
                new DraynorRooftopAgility(this),
                new VarrockRooftopAgility(this),
                new ChopNormalLogs(this),
                new ChopWillowLogs(this),
                new ChopMagicLogs(this)
        );
        setupPaint();
        this.teleporterScrollHelper = new TeleporterScrollHelper(ctx);
    }

    private void setupPaint() {
        if (this.paintHelper != null) {
            return;
        }
        this.paintHelper = new LeagueScriptPaint(this);
        this.paintHelper.addLine(() -> "Points: " + ctx.paint.formatValue(this.pointsGained));
    }

    @Override
    public void onProcess() {
        if (!this.checkedTasks) {
            checkTasks();
            return;
        }
        if (this.currentTask == null || this.currentTask.isCompleted()) {
            getNewTask();
            return;
        }
        if (!ctx.npcs.populate().filter(LeagueScriptConstants.PILLORY_GUARD_ID).isEmpty() &&
                !ctx.npcs.filter(n -> n.getInteracting() != null && n.getInteracting().equals(ctx.players.getLocal().getPlayer())).isEmpty()) {
            final SimpleNpc guard = ctx.npcs.nextNearest();
            if (guard != null) {
                guard.menuAction("Dismiss");
                ctx.sleep(1250);
                return;
            }
        }

        if (ctx.pathing.energyLevel() >= 30 && !ctx.pathing.running()) {
            ctx.pathing.running(true);
            return;
        }
        
        this.currentTask.run();
    }

    private void checkTasks() {
        setScriptStatus("Opening League panel");
        if (!leaguePanel.openLeaguePanel()) {
            return;
        }

        if (leaguePanel.doWeNeedToClearFilters()) {
            setScriptStatus("Clearing filters");
            leaguePanel.clearAllFilterOptions();
            return;
        }

        final SimpleWidget leaguesWidget = leaguePanel.getLeaguePanelWidget();
        if (leaguesWidget == null) {
            return;
        }

        setScriptStatus("Crunching numbers...");
        final SimpleWidget[] children = leaguesWidget.getDynamicChildren();
        if (children == null || children.length == 0) {
            return;
        }
        for (final SimpleWidget child : children) {
            final String taskName = ScriptUtils.stripHtml(child.getText().toLowerCase());
            this.tasks.stream().filter(task -> task.getName().equalsIgnoreCase(taskName)).findFirst().ifPresent((task) -> {
                if (child.getWidget().getTextColor() == 0xff7700) {
                    task.setCompleted(true);
                    ctx.log("Task " + task.getName() + " is already completed");
                }
            });
        }
        ctx.sleep(1500, 2500);
        ctx.menuActions.clickButton(43057155);
        ctx.onCondition(() -> {
            final SimpleWidget leaguesWidget1 = ctx.widgets.getWidget(657, 18);
            return leaguesWidget1 == null || leaguesWidget1.isHidden();
        }, 1000, 5);
        this.checkedTasks = true;
    }

    private void getNewTask() {
        if (this.currentTask != null) {
            this.currentTask.setCompleted(true);
        }
        final Optional<LeagueTask> newTask = tasks.stream().filter(task -> !task.isCompleted() && task.canComplete()).findFirst();
        if (newTask.isPresent()) {
            this.currentTask = newTask.get();
        }
    }

    public boolean isTaskCompleted(final Class<? extends LeagueTask> task) {
        for (final LeagueTask t : this.tasks) {
            if (t.getClass().equals(task)) {
                return t.isCompleted();
            }
        }
        return false;
    }

    public <T extends LeagueTask> T getTask(final Class<T> task) {
        for (final LeagueTask t : this.tasks) {
            if (t.getClass().equals(task)) {
                return (T) t;
            }
        }
        return null;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public String getScriptStatus() {
        return this.status;
    }
    public void setScriptStatus(String status) {
        this.status = status;
    }

    public TeleporterScrollHelper getTeleporterScrollHelper() {
        return this.teleporterScrollHelper;
    }

    @Override
    public void onTerminate() {
        ctx.log("Gained " + this.pointsGained + " points, total: " + this.pointsTotal);
    }

    @Override
    public void onChatMessage(ChatMessage e) {
        if (this.currentTask != null) {
            this.currentTask.onChatMessage(e);
        }
        if (e.getType() != ChatMessageType.GAMEMESSAGE || e.getSender() != null || !e.getName().isEmpty()) {
            return;
        }
        final String message = ScriptUtils.stripHtml(e.getMessage().toLowerCase());
        if (message.matches("congratulations, you've completed an? .*leagues task:.*")) {
            final String taskName = message.split(":")[1].trim();
            this.tasks.stream().filter(task -> task.getName().equalsIgnoreCase(taskName)).findFirst().ifPresent((task) -> task.setCompleted(true));
        } else if (message.startsWith("you have earned") && message.contains("league points. you now have")) {
            final Matcher matcher = POINTS_PATTERN.matcher(message);
            if (matcher.find()) {
                final int pointsEarned = Integer.parseInt(matcher.group(1).replace(",", ""));
                final int totalPoints = Integer.parseInt(matcher.group(2).replace(",", ""));
                this.pointsGained += pointsEarned;
                this.pointsTotal = totalPoints;
                ctx.log("Gained " + pointsEarned + " points, total: " + totalPoints);
            }
        }
    }

    @Override
    public void paint(final Graphics g1) {
        if (this.paintHelper == null) {
            return;
        }
        final Graphics2D g = (Graphics2D) g1;
        this.paintHelper.drawPaint(g);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (this.paintHelper != null) {
            this.paintHelper.handleMouseClick(e);
        }
    }

    @Override
    public int loopDuration() {
        return 150;
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}
