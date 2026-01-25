package io.musician101.musiboard;

import io.musician101.musiboard.commands.main.MBMain;
import io.musician101.musiboard.commands.scoreboard.ScoreboardCommand;
import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.musician101.musiboard.scoreboard.MusiScoreboardManager;
import io.musician101.musicommand.paper.PaperMusiCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Optional;

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

    public static MusiScoreboardManager getManager() {
        return getPlugin().manager;
    }

    public static MusiScoreboard getScoreboard(Player player) {
        return getPlugin().manager.getScoreboard(player);
    }

    public static Optional<MusiScoreboard> getScoreboard(String name) {
        return getPlugin().manager.getScoreboard(name);
    }

    public static Optional<MusiScoreboard> getScoreboardOrDefault(String name) {
        return getPlugin().manager.getScoreboardOrDefault(name);
    }

    @Override
    public void onDisable() {
        try {
            manager.save();
        }
        catch (IOException e) {
            getComponentLogger().error(Component.text("An error occurred while saving scoreboards.", NamedTextColor.RED), e);
        }
    }

    @Override
    public void onEnable() {
        reload();
        PaperMusiCommand<ComponentLike> musiCommand = PaperMusiCommand.newAdventureInstance(this);
        musiCommand.registerCommand(new MBMain(), "mb");
        musiCommand.registerCommand(new ScoreboardCommand());
        try {
            manager.load();
        }
        catch (IOException e) {
            getComponentLogger().error(Component.text("An error occurred while loading scoreboards.", NamedTextColor.RED), e);
        }

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
