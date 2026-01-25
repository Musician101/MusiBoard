package io.musician101.musiboard.dialog.objective;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.dialog.team.TeamsDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class NewObjectiveDialog extends MusiDialog {

    private static final String CRITERIA = "criteria";
    private static final String DISPLAY_NAME = "display_name";
    private static final String NAME = "name";
    private static final String RENDER_TYPE = "render_type";
    private final MusiScoreboard scoreboard;

    public NewObjectiveDialog(MusiScoreboard scoreboard) {
        super(Component.text("Create A New Objective"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput name = textInput(NAME, Component.text("Name"));
        DialogInput displayName = textInput(DISPLAY_NAME, Component.text("Display Name"));
        DialogInput criteria = textInput(CRITERIA, Component.text("Criteria"));
        List<OptionEntry> renderTypeEntries = Arrays.stream(RenderType.values()).map(r -> OptionEntry.create(r.toString(), Component.text(r.toString()), r == RenderType.INTEGER)).toList();
        DialogInput renderType = singleOptionInput(RENDER_TYPE, Component.text("Render Type"), renderTypeEntries);
        return List.of(name, displayName, criteria, renderType);
    }

    @Override
    protected DialogType type() {
        ActionButton confirm = actionButton(Component.text("Confirm"), this::confirmAction);
        return DialogType.confirmation(confirm, backButton(showMusiDialogCallback(new TeamsDialog(scoreboard))));
    }

    private void confirmAction(DialogResponseView view, Audience audience) {
        String name = view.getText(NAME);
        MusiDialog dialog = new ObjectivesDialog(scoreboard);
        if (name != null && scoreboard.getObjective(name) != null) {
            String displayNameString = view.getText(DISPLAY_NAME);
            Component displayName = displayNameString == null ? Component.text(name) : GsonComponentSerializer.gson().deserialize(displayNameString);
            String criteriaString = view.getText(CRITERIA);
            Criteria criteria = criteriaString == null ? Criteria.DUMMY : Criteria.create(criteriaString);
            String renderTypeString = view.getText(RENDER_TYPE);
            RenderType renderType = Arrays.stream(RenderType.values()).filter(r -> r.toString().equalsIgnoreCase(renderTypeString)).findFirst().orElse(RenderType.INTEGER);
            Objective objective = scoreboard.registerNewObjective(name, criteria, displayName, renderType);
            dialog = new ObjectiveDialog(objective, new ObjectivesDialog(scoreboard));
        }

        audience.showDialog(dialog.build());
    }
}
