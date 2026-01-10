package io.musician101.musiboard.scoreboard;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

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

@NullMarked
public class MusiScoreboardManager {

    private final List<MusiScoreboard> scoreboards = new ArrayList<>();
    // This is initialized when load() is called.
    @SuppressWarnings("NotNullFieldNotInitialized")
    private VanillaScoreboard vanillaScoreboard;

    public Optional<MusiScoreboard> getDefaultScoreboard() {
        return getScoreboard(getPlugin().getConfig().getString("defaultScoreboard", "minecraft"));
    }

    public MusiScoreboard getDefaultScoreboardOrVanilla() {
        return getDefaultScoreboard().orElse(vanillaScoreboard);
    }

    public MusiScoreboard getScoreboard(Player player) {
        if (vanillaScoreboard.hasPlayer(player)) {
            return vanillaScoreboard;
        }

        return scoreboards.stream().filter(scoreboard -> scoreboard.hasPlayer(player)).findFirst().orElseGet(() -> {
            vanillaScoreboard.addPlayer(player);
            return vanillaScoreboard;
        });
    }

    public Optional<MusiScoreboard> getScoreboard(String name) {
        if (name.equals(vanillaScoreboard.getName())) {
            return Optional.of(vanillaScoreboard);
        }

        return scoreboards.stream().filter(m -> name.equals(m.getName())).findFirst();
    }

    public Optional<MusiScoreboard> getScoreboardOrDefault(String name) {
        return getScoreboard(name).or(this::getDefaultScoreboard);
    }

    public MusiScoreboard getScoreboardOrDefaultOrVanilla(String name) {
        return getScoreboardOrDefault(name).orElse(vanillaScoreboard);
    }

    public MusiScoreboard getScoreboardOrVanilla(String name) {
        return getScoreboard(name).orElse(vanillaScoreboard);
    }

    public List<MusiScoreboard> getScoreboards() {
        return scoreboards;
    }

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

    public boolean registerNewScoreboard(String name) {
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

    public void setScoreboard(Player player, MusiScoreboard scoreboard) {
        scoreboards.forEach(s -> s.removePlayer(player));
        scoreboard.addPlayer(player);
    }
}
