package io.musician101.musiboard.dialog.scoreboard;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class DisplaySlotsDialog extends MusiDialog {

    private final MusiScoreboard scoreboard;

    public DisplaySlotsDialog(MusiScoreboard scoreboard) {
        super(Component.text("Display Slots"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected List<DialogInput> inputs() {
        List<DialogInput> inputs = new ArrayList<>();
        scoreboard.getObjectives().stream().sorted(Comparator.comparing(Objective::getName)).forEach(objective -> {
            inputs.add(singleOptionInput(objective.getName(), objective.displayName(), entries(objective)));
        });
        return inputs;
    }

    private List<OptionEntry> entries(Objective objective) {
        List<OptionEntry> entries = new ArrayList<>();
        entries.add(OptionEntry.create("none", Component.text("None"), objective.getDisplaySlot() == null));
        for (DisplaySlot slot : DisplaySlot.values()) {
            entries.add(OptionEntry.create(slot.toString(), Component.text(slot.getId()), objective.getDisplaySlot() == slot));
        }

        return entries;
    }

    @Override
    protected DialogType type() {
        ActionButton applyButton = applyButton((view, audience) -> {
            scoreboard.getObjectives().forEach(objective -> {
                String slotId = view.getText(objective.getName());
                if (slotId != null) {
                    DisplaySlot displaySlot = Arrays.stream(DisplaySlot.values()).filter(slot -> slot.getId().equals(slotId)).findFirst().orElse(null);
                    objective.setDisplaySlot(displaySlot);
                }
            });
            audience.showDialog(new ScoreboardDialog(scoreboard).build());
        });
        ActionButton discardButton = backButton(showMusiDialogCallback(new ScoreboardDialog(scoreboard)));
        return DialogType.confirmation(applyButton, discardButton);
    }
}
