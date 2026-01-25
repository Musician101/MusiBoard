package io.musician101.musiboard.dialog.scoreboard;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class NewScoreboardDialog extends MusiDialog {

    private static final String NAME = "name";

    public NewScoreboardDialog() {
        super(Component.text("Create A New Scoreboard"));
    }

    @Override
    protected List<DialogInput> inputs() {
        return List.of(textInput(NAME, Component.text("Name")));
    }

    @Override
    protected DialogType type() {
        ActionButton confirm = actionButton(Component.text("Confirm"), (view, audience) -> {
            String name = view.getText(NAME);
            if (name == null) {
                audience.showDialog(ScoreboardsDialog.editDialog().build());
                return;
            }

            getManager().registerNewScoreboard(name);
            MusiDialog dialog = getManager().getScoreboard(name).map(scoreboard -> (MusiDialog) new ScoreboardDialog(scoreboard)).orElse(ScoreboardsDialog.editDialog());
            audience.showDialog(dialog.build());
        });
        return DialogType.confirmation(confirm, backButton(showMusiDialogCallback(ScoreboardsDialog.editDialog())));
    }
}
