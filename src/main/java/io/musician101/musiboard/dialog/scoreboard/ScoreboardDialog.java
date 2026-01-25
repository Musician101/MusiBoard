package io.musician101.musiboard.dialog.scoreboard;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.PlayersDialog;
import io.musician101.musiboard.dialog.objective.ObjectivesDialog;
import io.musician101.musiboard.dialog.team.TeamsDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.VanillaScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class ScoreboardDialog extends MusiDialog {

    private static final String SAVE_DATA = "save_data";
    private final MusiScoreboard scoreboard;

    public ScoreboardDialog(MusiScoreboard scoreboard) {
        super(Component.text("Editing " + scoreboard.getName()));
        this.scoreboard = scoreboard;
    }

    @Override
    protected List<DialogInput> inputs() {
        return List.of(boolInput(SAVE_DATA, Component.text("Save Data"), scoreboard.saveData()));
    }

    @Override
    protected DialogType type() {
        List<ActionButton> buttons = new ArrayList<>();
        buttons.add(dialogButton(new ObjectivesDialog(scoreboard), "Objectives"));
        buttons.add(dialogButton(new DisplaySlotsDialog(scoreboard), "Display Slots"));
        buttons.add(dialogButton(new TeamsDialog(scoreboard), "Teams"));
        buttons.add(dialogButton(new PlayersDialog(scoreboard), "Players"));
        buttons.add(applyButton((view, audience) -> {
            Boolean bool = view.getBoolean(SAVE_DATA);
            scoreboard.saveData(bool == null || bool);
            audience.showDialog(ScoreboardsDialog.editDialog().build());
        }));
        if (!(scoreboard instanceof VanillaScoreboard)) {
            buttons.add(actionButton(Component.text("Delete"), (view, audience) -> getManager().getScoreboards().remove(scoreboard)));
        }

        ActionButton backButton = backButton((view, audience) -> audience.showDialog(ScoreboardsDialog.editDialog().build()));
        return multiActionType(buttons, backButton, 1);
    }

    private ActionButton dialogButton(MusiDialog dialog, String label) {
        return actionButton(Component.text(label), showMusiDialogCallback(dialog));
    }
}
