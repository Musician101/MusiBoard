package io.musician101.musiboard.dialog.scoreboard;

import io.musician101.musiboard.dialog.MusiDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static io.musician101.musiboard.MusiBoard.getManager;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class ScoreboardsDialog extends MusiDialog {

    private final DialogType type;

    private ScoreboardsDialog(DialogType type) {
        super(Component.text("Select Scoreboard"));
        this.type = type;
    }

    private static ActionButton emptyBackButton() {
        return ActionButton.builder(Component.text("Back")).action(DialogAction.customClick((view, audience) -> {
        }, DEFAULT_CALLBACK_OPTIONS)).build();
    }

    public static ScoreboardsDialog editDialog() {
        List<ActionButton> buttons = new ArrayList<>();
        getManager().getScoreboards().stream().sorted(Comparator.comparing(MusiScoreboard::getName)).forEach(scoreboard -> {
            DialogAction action = DialogAction.customClick((view, audience) -> audience.showDialog(new ScoreboardDialog(scoreboard).build()), DEFAULT_CALLBACK_OPTIONS);
            ActionButton button = ActionButton.builder(Component.text(scoreboard.getName())).action(action).build();
            buttons.add(button);
        });
        buttons.add(newButton());
        DialogType dialogType = DialogType.multiAction(buttons, emptyBackButton(), 2);
        return new ScoreboardsDialog(dialogType);
    }

    private static ActionButton newButton() {
        return ActionButton.builder(Component.text("New")).action(DialogAction.customClick((view, audience) -> audience.showDialog(new NewScoreboardDialog().build()), DEFAULT_CALLBACK_OPTIONS)).build();
    }

    public static ScoreboardsDialog selectDialog() {
        List<ActionButton> buttons = new ArrayList<>();
        getManager().getScoreboards().stream().sorted(Comparator.comparing(MusiScoreboard::getName)).forEach(scoreboard -> buttons.add(selectScoreboardAction(scoreboard)));
        DialogType dialogType = DialogType.multiAction(buttons, emptyBackButton(), 2);
        return new ScoreboardsDialog(dialogType);
    }

    private static ActionButton selectScoreboardAction(MusiScoreboard scoreboard) {
        DialogAction action = DialogAction.customClick((view, audience) -> {
            getManager().setScoreboard((Player) audience, scoreboard);
            audience.sendMessage(MiniMessage.miniMessage().deserialize("<green><mb-prefix> " + scoreboard.getName() + " selected."));
            audience.closeDialog();
        }, DEFAULT_CALLBACK_OPTIONS);
        return ActionButton.builder(Component.text(scoreboard.getName())).action(action).build();
    }

    public static ScoreboardsDialog selectGlobalDialog() {
        List<ActionButton> buttons = new ArrayList<>();
        getManager().getScoreboards().stream().sorted(Comparator.comparing(MusiScoreboard::getName)).forEach(scoreboard -> buttons.add(selectGlobalScoreboardAction(scoreboard)));
        DialogType dialogType = DialogType.multiAction(buttons, emptyBackButton(), 2);
        return new ScoreboardsDialog(dialogType);
    }

    private static ActionButton selectGlobalScoreboardAction(MusiScoreboard scoreboard) {
        DialogAction action = DialogAction.customClick((view, audience) -> {
            Bukkit.getOnlinePlayers().forEach(p -> getManager().setScoreboard(p, scoreboard));
            audience.sendMessage(MiniMessage.miniMessage().deserialize("<green><mb-prefix> " + scoreboard.getName() + " selected."));
            audience.closeDialog();
        }, DEFAULT_CALLBACK_OPTIONS);
        return ActionButton.builder(Component.text(scoreboard.getName())).action(action).build();
    }

    @Override
    protected DialogType type() {
        return type;
    }
}
