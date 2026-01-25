package io.musician101.musiboard.dialog.score;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.numerformat.NumberFormatHolder;
import io.musician101.musiboard.dialog.numerformat.NumberFormatsDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ScoreDialog extends MusiDialog {

    private final static String SCORE = "score";
    private final static String DISPLAY = "display";
    private final Score score;
    private final ScoresDialog scoresDialog;

    public ScoreDialog(Score score, ScoresDialog scoresDialog) {
        super(label(score));
        this.score = score;
        this.scoresDialog = scoresDialog;
    }

    private static Component label(Score score) {
        return Component.join(JoinConfiguration.spaces(), Component.text("Score for " + score.getEntry() + " for objective "), score.getObjective().displayName());
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput scoreInput = textInput(SCORE, Component.text("Score"), score.getScore() + "");
        Component displayComponent = score.customName();
        if (displayComponent == null) {
            displayComponent = Component.empty();
        }

        DialogInput display = textInput(DISPLAY, Component.text("Custom Name"), displayComponent);
        return List.of(scoreInput, display);
    }

    @Override
    protected DialogType type() {
        ActionButton numberFormatButton = actionButton(Component.text("Number Format"), showMusiDialogCallback(new NumberFormatsDialog(new ScoreHolder(this))));
        ActionButton applyButton = applyButton((view, audience) -> {
            String scoreString = view.getText(SCORE);
            if (scoreString != null) {
                try {
                    int score = Integer.parseInt(scoreString);
                    this.score.setScore(score);
                }
                catch (NumberFormatException ignored) {

                }
            }

            String displayString = view.getText(DISPLAY);
            if (displayString != null) {
                score.customName(GsonComponentSerializer.gson().deserialize(displayString));
            }

            audience.showDialog(scoresDialog.build());
        });
        ActionButton discardButton = backButton(showMusiDialogCallback(scoresDialog));
        return multiActionType(List.of(numberFormatButton, applyButton), discardButton, 1);
    }

    public static class ScoreHolder implements NumberFormatHolder {

        private final ScoreDialog dialog;

        private ScoreHolder(ScoreDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void numberFormat(@Nullable NumberFormat format) {
            dialog.score.numberFormat(numberFormat());
        }

        @Override
        public @Nullable NumberFormat numberFormat() {
            return dialog.score.numberFormat();
        }

        @Override
        public MusiDialog previousDialog() {
            return dialog;
        }
    }
}
