package io.musician101.musiboard.scoreboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static io.musician101.musiboard.MusiBoard.getPlugin;

public class MusiScoreboardManager {

    private final List<MusiScoreboard> scoreboards = new ArrayList<>();
    private VanillaScoreboard vanillaScoreboard;

    @Nonnull
    public Optional<MusiScoreboard> getDefaultScoreboard() {
        return getScoreboard(getPlugin().getConfig().getString("defaultScoreboard", "minecraft"));
    }

    @Nonnull
    public MusiScoreboard getDefaultScoreboardOrVanilla() {
        return getDefaultScoreboard().orElse(vanillaScoreboard);
    }

    @Nonnull
    public MusiScoreboard getScoreboard(@Nonnull Player player) {
        if (vanillaScoreboard.hasPlayer(player)) {
            return vanillaScoreboard;
        }

        return scoreboards.stream().filter(scoreboard -> scoreboard.hasPlayer(player)).findFirst().orElseGet(() -> {
            vanillaScoreboard.addPlayer(player);
            return vanillaScoreboard;
        });
    }

    @Nonnull
    public Optional<MusiScoreboard> getScoreboard(@Nonnull String name) {
        if (name.equals(vanillaScoreboard.getName())) {
            return Optional.of(vanillaScoreboard);
        }

        return scoreboards.stream().filter(m -> name.equals(m.getName())).findFirst();
    }

    @Nonnull
    public Optional<MusiScoreboard> getScoreboardOrDefault(@Nonnull String name) {
        return getScoreboard(name).or(this::getDefaultScoreboard);
    }

    @Nonnull
    public MusiScoreboard getScoreboardOrDefaultOrVanilla(@Nonnull String name) {
        return getScoreboardOrDefault(name).orElse(vanillaScoreboard);
    }

    @Nonnull
    public MusiScoreboard getScoreboardOrVanilla(@Nonnull String name) {
        return getScoreboard(name).orElse(vanillaScoreboard);
    }

    @Nonnull
    public List<MusiScoreboard> getScoreboards() {
        return scoreboards;
    }

    @Nonnull
    public VanillaScoreboard getVanillaScoreboard() {
        return vanillaScoreboard;
    }

    public void load() {
        Logger logger = getPlugin().getLogger();
        vanillaScoreboard = new VanillaScoreboard();
        try (Stream<Path> stream = Files.list(getPlugin().getDataFolder().toPath())) {
            stream.forEach(path -> scoreboards.add(new MusiScoreboard(YamlConfiguration.loadConfiguration(path.toFile()))));
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while trying to load scoreboards.", e);
        }

        logger.info(scoreboards.size() + " scoreboard(s) loaded.");
    }

    public boolean registerNewScoreboard(@Nonnull String name) {
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
        scoreboards.forEach(MusiScoreboard::save);
        getPlugin().getLogger().info(scoreboards.size() + " scoreboard(s) saved.");
    }

    public void setScoreboard(@Nonnull Player player, @Nonnull MusiScoreboard scoreboard) {
        scoreboards.forEach(s -> s.removePlayer(player));
        scoreboard.addPlayer(player);
    }
}
