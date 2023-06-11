package io.musician101.musiboard;

import io.musician101.musiboard.commands.MBCommand;
import io.musician101.musiboard.commands.objectives.ObjectivesCommand;
import io.musician101.musiboard.commands.players.PlayersCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.commands.team.TeamCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static io.musician101.bukkitier.Bukkitier.registerCommand;

public final class MusiBoard extends JavaPlugin implements Listener {

    @Nonnull
    private final MusiScoreboardManager manager = new MusiScoreboardManager();

    @Nonnull
    public static MusiBoard getPlugin() {
        return getPlugin(MusiBoard.class);
    }

    @Nonnull
    public MusiScoreboardManager getManager() {
        return manager;
    }

    @Override
    public void onDisable() {
        manager.save();
    }

    @Override
    public void onEnable() {
        reload();
        registerCommand(this, new MBCommand());
        registerCommand(this, new ObjectivesCommand());
        registerCommand(this, new PlayersCommand());
        registerCommand(this, new ScoreboardCommand());
        registerCommand(this, new TeamCommand());
        manager.load();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        manager.setScoreboard(player, manager.getScoreboard(player));
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
    }
}
