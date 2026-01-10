package io.musician101.musiboard;

import io.musician101.musiboard.commands.MBMain;
import io.musician101.musiboard.commands.objectives.ObjectivesCommand;
import io.musician101.musiboard.commands.players.PlayersCommand;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.commands.team.TeamCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import io.musician101.musicommand.paper.PaperMusiCommand;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MusiBoard extends JavaPlugin implements Listener {

    private final MusiScoreboardManager manager = new MusiScoreboardManager();

    public static MusiBoard getPlugin() {
        return getPlugin(MusiBoard.class);
    }

    public boolean isPaperInstalled() {
        try {
            Class.forName("io.papermc.paper.scoreboard.numbers.NumberFormat");
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

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
        PaperMusiCommand<ComponentLike> musiCommand = PaperMusiCommand.newAdventureInstance(this);
        musiCommand.registerCommand(new MBMain(), "mb");
        musiCommand.registerCommand(new ObjectivesCommand());
        musiCommand.registerCommand(new PlayersCommand());
        musiCommand.registerCommand(new ScoreboardCommand());
        musiCommand.registerCommand(new TeamCommand());
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
