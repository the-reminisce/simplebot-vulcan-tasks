package me.remie.vulcan.leaguetasks.helpers;

import simple.hooks.wrappers.SimpleWidget;
import simple.robot.api.ClientContext;

import java.util.Optional;

/**
 * Created by Reminisce on Mar 06, 2024 at 8:42 PM
 *
 * @author Reminisce <thereminisc3@gmail.com>
 * @Discord reminisce <138751815847116800>
 */
public class TeleporterScrollHelper {

    private final ClientContext ctx;

    private static final int TELEPORT_WIDGET_ID = 187;

    public TeleporterScrollHelper(final ClientContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Retrieves the teleport interface widget.
     *
     * @return an Optional containing the teleport interface widget if it exists and is visible,
     *         otherwise an empty Optional
     */
    private Optional<SimpleWidget> getWidget() {
        return Optional.ofNullable(ctx.widgets.getWidget(TELEPORT_WIDGET_ID, 0))
                .filter(widget -> !widget.isHidden());
    }

    /**
     * Checks if the teleport widget is visible.
     *
     * @return true if the widget is visible, false otherwise
     */
    public boolean isOpen() {
        return getWidget().isPresent();
    }

    /**
     * Sends a teleport action to the teleport interface.
     *
     * @param option the option index to select for the teleport action
     */
    public void teleport(final int option) {
        getWidget().ifPresent(widget -> ctx.menuActions.sendAction(30, Math.max(0, option), 12255235, 0, "Continue", ""));
    }

}
