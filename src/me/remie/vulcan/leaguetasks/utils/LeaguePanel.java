package me.remie.vulcan.leaguetasks.utils;

import simple.hooks.simplebot.Game;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.api.ClientContext;

public class LeaguePanel {
    private final ClientContext ctx;
    private static final int WIDGET_ID = 657;

    public LeaguePanel(final ClientContext ctx) {
        this.ctx = ctx;
    }

    public boolean doWeNeedToClearFilters() {
for (LeaguePanelFilterType filterType : LeaguePanelFilterType.values()) {
            SimpleWidget dropDownMenu = ctx.widgets.getWidget(WIDGET_ID, filterType.getWidgetId());
            if (!isOptionSelected(dropDownMenu)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOptionSelected(SimpleWidget widget) {
        if (widget == null) {
            return false;
        }
        final SimpleWidget child = widget.getChild(4);
        if (child == null) {
            return false;
        }
        return "all".equalsIgnoreCase(child.getText());
    }

    private boolean chooseAllInDropDown(int filterWidgetId, int filterDropdownOptionId) {
        final SimpleWidget filterDropDownMenu = ctx.widgets.getWidget(WIDGET_ID, filterWidgetId);
        if (filterDropDownMenu == null || isOptionSelected(filterDropDownMenu)) {
            return true;
        }
        final SimpleWidget selectedOption = filterDropDownMenu.getChild(4);
        final SimpleWidget dropDownMenu = ctx.widgets.getWidget(WIDGET_ID, filterDropdownOptionId);
        if (!chooseOptionInDropdown(selectedOption, dropDownMenu)) {
            ctx.log("Something went wrong choosing all in options");
            return false;
        }
        return true;
    }

    public boolean clearAllFilterOptions() {
        for (LeaguePanelFilterType filterType : LeaguePanelFilterType.values()) {
            if (!chooseAllInDropDown(filterType.getWidgetId(), filterType.getAllOptionWidgetId())) {
                ctx.log("Something went wrong choosing " + filterType.name() + " option");
                return false;
            }
        }
        return true;
    }

    private boolean chooseOptionInDropdown(SimpleWidget dropDownWidget, SimpleWidget dropDownOptionList) {
        if (dropDownWidget == null || dropDownWidget.getChild(1) == null) {
            return false;
        }

        dropDownWidget.click(1);
        ctx.sleep(1500);
        if (dropDownOptionList != null && !dropDownOptionList.isHidden()) {
            final SimpleWidget selectedOption = dropDownOptionList.getChild(1);
            if (selectedOption != null) {
                selectedOption.click(1);
                ctx.sleep(1300);
                return true;
            }
        }
        return false;
    }

    public boolean isLeaguePanelOpen() {
        final SimpleWidget leaguePanelWidget = getLeaguePanelWidget();
        return leaguePanelWidget != null && !leaguePanelWidget.isHidden();
    }

    public boolean openLeaguePanel() {
        if (isLeaguePanelOpen()) {
            return true;
        }

        ctx.log("Opening leagues menu");
        ctx.game.tab(Game.Tab.QUESTS);
        ctx.menuActions.clickButton(41222167);
        ctx.sleep(1000);
        ctx.menuActions.clickButton(42991640);
        return ctx.onCondition(() -> isLeaguePanelOpen(), 1000, 5);
    }

    public SimpleWidget getLeaguePanelWidget() {
        return ctx.widgets.getWidget(WIDGET_ID,18);
    }
}
