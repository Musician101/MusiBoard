package io.musician101.musiboard.dialog;

import io.musician101.musiboard.dialog.scoreboard.ScoreboardDialog;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.musician101.musiboard.MusiBoard.getManager;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class PlayersDialog extends MusiDialog {

    private final MusiScoreboard scoreboard;
    private final List<UUID> players = new ArrayList<>();

    public PlayersDialog(MusiScoreboard scoreboard) {
        super(Component.text("Players"));
        this.scoreboard = scoreboard;
    }

    @Override
    protected List<DialogInput> inputs() {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(this::playerCheckbox).toList();
    }

    private DialogInput playerCheckbox(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        players.add(uuid);
        String name = player.getName() == null ? uuid.toString() : player.getName();
        return boolInput(uuid.toString(), Component.text(name), scoreboard.hasPlayer(player));
    }

    @Override
    protected DialogType type() {
        ActionButton applyButton = applyButton((view, audience) -> players.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            Boolean bool = view.getBoolean(uuid.toString());
            if (bool != null && bool) {
                getManager().setScoreboard(player, scoreboard);
            }
        }));
        return DialogType.confirmation(applyButton, backButton(showMusiDialogCallback(new ScoreboardDialog(scoreboard))));
    }
}
