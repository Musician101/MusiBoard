package io.musician101.musiboard.dialog.score;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.objective.ObjectiveDialog;
import io.musician101.musiboard.dialog.objective.ObjectivesDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ScoresDialog extends MusiDialog {

    private final MusiScoreboard scoreboard;
    private final Objective objective;

    public ScoresDialog(MusiScoreboard scoreboard, Objective objective) {
        super(Component.text("Scores"));
        this.scoreboard = scoreboard;
        this.objective = objective;
    }

    @Override
    protected DialogType type() {
        ActionButton backButton = backButton(showMusiDialogCallback(new ObjectiveDialog(objective, new ObjectivesDialog(scoreboard))));
        return DialogType.dialogList(dialogs()).exitAction(backButton).build();
    }

    private RegistrySet<Dialog> dialogs() {
        List<Dialog> dialogs = new ArrayList<>();
        scoreboard.getEntries().stream().sorted(String::compareTo).forEach(entry -> {
            Score score = objective.getScore(entry);
            dialogs.add(new ScoreDialog(score, this).build());
        });
        return dialogs(dialogs);
    }
}
