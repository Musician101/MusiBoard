package io.musician101.musiboard.dialog.objective;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.numerformat.NumberFormatHolder;
import io.musician101.musiboard.dialog.numerformat.NumberFormatsDialog;
import io.musician101.musiboard.dialog.score.ScoresDialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class ObjectiveDialog extends MusiDialog {

    private static final String CRITERIA = "criteria";
    private static final String DISPLAY_AUTO_UPDATE = "display_auto_update";
    private static final String DISPLAY_NAME = "display_name";
    private static final String RENDER_TYPE = "render_type";
    private final Objective objective;
    private final ObjectivesDialog objectivesDialog;

    public ObjectiveDialog(Objective objective, ObjectivesDialog objectivesDialog) {
        super(Component.join(JoinConfiguration.spaces(), Component.text("Editing"), objective.displayName()));
        this.objective = objective;
        this.objectivesDialog = objectivesDialog;
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput displayName = textInput(DISPLAY_NAME, Component.text("Display Name"), objective.displayName());
        DialogInput criteria = textInput(CRITERIA, Component.text("Criteria"), objective.getTrackedCriteria().getName());
        List<OptionEntry> renderTypeEntries = Arrays.stream(RenderType.values()).map(r -> OptionEntry.create(r.toString(), Component.text(r.toString()), objective.getRenderType() == r)).toList();
        DialogInput renderType = singleOptionInput(RENDER_TYPE, Component.text("Render Type"), renderTypeEntries);
        DialogInput displayAutoUpdate = boolInput(DISPLAY_AUTO_UPDATE, Component.text("Display Auto Update"), objective.willAutoUpdateDisplay());
        return List.of(displayName, criteria, renderType, displayAutoUpdate);
    }

    @Override
    protected DialogType type() {
        ActionButton formatsButton = actionButton(Component.text("Number Format"), showMusiDialogCallback(new NumberFormatsDialog(new ObjectiveHolder(this))));
        ActionButton scoresButton = actionButton(Component.text("Scores"), showMusiDialogCallback(new ScoresDialog(objectivesDialog.scoreboard, objective)));
        ActionButton applyButton = applyButton(this::applyAction);
        ActionButton deleteButton = actionButton(Component.text("Delete"), (view, audience) -> {
            objective.unregister();
            audience.showDialog(objectivesDialog.build());
        });
        List<ActionButton> buttons = List.of(formatsButton, scoresButton, applyButton, deleteButton);
        return multiActionType(buttons, backButton(showMusiDialogCallback(objectivesDialog)), 1);
    }

    private void applyAction(DialogResponseView view, Audience audience) {
        String displayNameString = view.getText(DISPLAY_NAME);
        Component displayName = Component.text(displayNameString == null ? objective.getName() : displayNameString);
        objective.displayName(displayName);
        String renderTypeString = view.getText(RENDER_TYPE);
        RenderType renderType = Arrays.stream(RenderType.values()).filter(r -> r.toString().equalsIgnoreCase(renderTypeString)).findFirst().orElse(RenderType.INTEGER);
        objective.setRenderType(renderType);
        Boolean displayAutoUpdate = view.getBoolean(DISPLAY_AUTO_UPDATE);
        objective.setAutoUpdateDisplay(displayAutoUpdate == null || displayAutoUpdate);
        audience.showDialog(objectivesDialog.build());
    }

    public static class ObjectiveHolder implements NumberFormatHolder {

        private final ObjectiveDialog dialog;

        private ObjectiveHolder(ObjectiveDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void numberFormat(@Nullable NumberFormat format) {
            dialog.objective.numberFormat(numberFormat());
        }

        @Override
        public @Nullable NumberFormat numberFormat() {
            return dialog.objective.numberFormat();
        }

        @Override
        public MusiDialog previousDialog() {
            return dialog;
        }
    }
}
