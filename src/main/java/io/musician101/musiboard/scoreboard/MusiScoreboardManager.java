package io.musician101.musiboard.scoreboard;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static io.musician101.musiboard.MusiBoard.getPlugin;

public class MusiScoreboardManager {

    private final List<MusiScoreboard> scoreboards = new ArrayList<>();
    private VanillaScoreboard vanillaScoreboard;

    @NotNull
    public Optional<MusiScoreboard> getDefaultScoreboard() {
        return getScoreboard(getPlugin().getConfig().getString("defaultScoreboard", "minecraft"));
    }

    @NotNull
    public MusiScoreboard getDefaultScoreboardOrVanilla() {
        return getDefaultScoreboard().orElse(vanillaScoreboard);
    }

    @NotNull
    public MusiScoreboard getScoreboard(@NotNull Player player) {
        if (vanillaScoreboard.hasPlayer(player)) {
            return vanillaScoreboard;
        }

        return scoreboards.stream().filter(scoreboard -> scoreboard.hasPlayer(player)).findFirst().orElseGet(() -> {
            vanillaScoreboard.addPlayer(player);
            return vanillaScoreboard;
        });
    }

    @NotNull
    public Optional<MusiScoreboard> getScoreboard(@NotNull String name) {
        if (name.equals(vanillaScoreboard.getName())) {
            return Optional.of(vanillaScoreboard);
        }

        return scoreboards.stream().filter(m -> name.equals(m.getName())).findFirst();
    }

    @NotNull
    public Optional<MusiScoreboard> getScoreboardOrDefault(@NotNull String name) {
        return getScoreboard(name).or(this::getDefaultScoreboard);
    }

    @NotNull
    public MusiScoreboard getScoreboardOrDefaultOrVanilla(@NotNull String name) {
        return getScoreboardOrDefault(name).orElse(vanillaScoreboard);
    }

    @NotNull
    public MusiScoreboard getScoreboardOrVanilla(@NotNull String name) {
        return getScoreboard(name).orElse(vanillaScoreboard);
    }

    @NotNull
    public List<MusiScoreboard> getScoreboards() {
        return scoreboards;
    }

    @NotNull
    public VanillaScoreboard getVanillaScoreboard() {
        return vanillaScoreboard;
    }

    public void load() {
        Logger logger = getPlugin().getLogger();
        vanillaScoreboard = new VanillaScoreboard();
        Path dir = getPlugin().getDataFolder().toPath().resolve("scoreboards");
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            }
            catch (IOException e) {
                logger.log(Level.SEVERE, "An error occurred while trying to load scoreboards.", e);
                return;
            }
        }

        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> scoreboards.add(new MusiScoreboard(YamlConfiguration.loadConfiguration(path.toFile()))));
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while trying to load scoreboards.", e);
        }

        logger.info(scoreboards.size() + " scoreboard(s) loaded.");
    }

    public boolean registerNewScoreboard(@NotNull String name) {
        if (name.equals(vanillaScoreboard.getName())) {
            return false;
        }

        Optional<MusiScoreboard> board = getScoreboard(name);
        if (board.isPresent()) {
            return false;
        }

        MusiScoreboard b = new MusiScoreboard(name);
        return scoreboards.add(b);
    }

    public void save() {
        scoreboards.stream().filter(MusiScoreboard::saveData).forEach(MusiScoreboard::save);
        getPlugin().getLogger().info(scoreboards.size() + " scoreboard(s) saved.");
    }

    public void setScoreboard(@NotNull Player player, @NotNull MusiScoreboard scoreboard) {
        scoreboards.forEach(s -> s.removePlayer(player));
        scoreboard.addPlayer(player);
    }
}
