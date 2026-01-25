package io.musician101.musiboard.dialog.team;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scoreboard.Team;
import org.codehaus.plexus.util.StringUtils;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class ColorDialog extends MusiDialog {

    private NamedTextColor color;
    private final Team team;
    private final TeamDialog teamDialog;

    public ColorDialog(NamedTextColor color, Team team, TeamDialog teamDialog) {
        super(Component.text("Select Team Color"));
        this.color = color;
        this.team = team;
        this.teamDialog = teamDialog;
    }

    @Override
    protected List<DialogBody> body() {
        Component testPhrase = Component.text("Sample Text", color);
        return List.of(DialogBody.plainMessage(testPhrase));
    }

    @Override
    protected DialogType type() {
        List<ActionButton> actionButtons = colorButtons();
        actionButtons.add(applyButton((view, audience) -> {
            team.color(color);
            audience.showDialog(teamDialog.build());
        }));
        return multiActionType(actionButtons, backButton(showMusiDialogCallback(teamDialog)));
    }

    private List<ActionButton> colorButtons() {
        List<ActionButton> buttons = new ArrayList<>();
        NamedTextColor.NAMES.keyToValue().forEach((name, color) -> {
            Component label = Component.text(StringUtils.capitalise(name.replaceAll("_", "")));
            buttons.add(actionButton(label, (view, audience) -> {
                this.color = color;
                audience.showDialog(build());
            }));
        });
        return buttons;
    }
}
