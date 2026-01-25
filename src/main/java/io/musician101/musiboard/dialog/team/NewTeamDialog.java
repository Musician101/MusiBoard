package io.musician101.musiboard.dialog.team;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class NewTeamDialog extends MusiDialog {

    private static final String NAME = "name";
    private final MusiScoreboard scoreboard;

    public NewTeamDialog(MusiScoreboard scoreboard) {
        super(Component.text("Create A New Team"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected List<DialogInput> inputs() {
        return List.of(textInput(NAME, Component.text("Name")));
    }

    @Override
    protected DialogType type() {
        ActionButton confirm = actionButton(Component.text("Confirm"), (view, audience) -> {
            String name = view.getText(NAME);
            MusiDialog dialog = new TeamsDialog(scoreboard);
            if (name != null && scoreboard.getTeam(name) != null) {
                Team team = scoreboard.registerNewTeam(name);
                dialog = new TeamDialog(team, new TeamsDialog(scoreboard));
            }

            audience.showDialog(dialog.build());
        });
        return DialogType.confirmation(confirm, backButton(showMusiDialogCallback(new TeamsDialog(scoreboard))));
    }
}
