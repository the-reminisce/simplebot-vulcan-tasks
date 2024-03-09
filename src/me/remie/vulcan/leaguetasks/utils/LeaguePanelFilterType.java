package me.remie.vulcan.leaguetasks.utils;

public enum LeaguePanelFilterType {
    FILTER_TIER(27, 35),
    FILTER_TYPE(28, 36),
    FILTER_AREA(29, 37),
    FILTER_COMPLETED(31, 39);

    private final int widgetId;
    private final int allOptionWidgetId;

    LeaguePanelFilterType( int widgetId, int allOptionWidgetId) {
        this.widgetId = widgetId;
        this.allOptionWidgetId = allOptionWidgetId;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getAllOptionWidgetId() {
        return allOptionWidgetId;
    }
}
