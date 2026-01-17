package io.musician101.musiboard.scoreboard;

import io.musician101.musiboard.scoreboard.serialize.DummyObjective;
import io.musician101.musiboard.scoreboard.serialize.DummyScore;
import io.musician101.musiboard.scoreboard.serialize.DummyTeam;
import io.musician101.musiboard.scoreboard.serialize.MusiScoreboardSerializer;
import io.musician101.musiboard.scoreboard.serialize.NamedTextColorSerializer;
import io.musician101.musiboard.scoreboard.serialize.NumberFormatSerializer;
import io.musician101.musiboard.scoreboard.serialize.OptionStatusSerializer;
import io.musician101.musiboard.scoreboard.serialize.TextColorSerializer;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
public class MusiScoreboardManager {

    private static final TypeSerializerCollection SERIALIZERS = TypeSerializerCollection.defaults().childBuilder()
            .register(MusiScoreboard.class, new MusiScoreboardSerializer())
            .register(DummyObjective.class, new DummyObjective.Serializer())
            .register(DummyTeam.class, new DummyTeam.Serializer())
            .register(OptionStatus.class, new OptionStatusSerializer())
            .register(NamedTextColor.class, new NamedTextColorSerializer())
            .register(DummyScore.class, new DummyScore.Serializer())
            .register(NumberFormat.class, new NumberFormatSerializer())
            .register(TextColor.class, new TextColorSerializer())
            .build();
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

    public void load() throws IOException {
        IOException exception = new IOException("One or more errors occurred while loading scoreboards.");
        vanillaScoreboard = new VanillaScoreboard();
        Path dir = getPlugin().getDataFolder().toPath().resolve("scoreboards");
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            }
            catch (IOException e) {
                exception.addSuppressed(exception);
            }
        }

        try (Stream<Path> stream = Files.list(dir)) {
            stream.filter(path -> !Files.isDirectory(path)).filter(path -> path.toString().endsWith(".yml")).map(path -> {
                YamlConfigurationLoader loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).file(path.toFile()).defaultOptions(c -> c.serializers(SERIALIZERS)).build();
                try {
                    ConfigurationNode node = loader.load();
                    MusiScoreboard scoreboard = node.require(MusiScoreboard.class);
                    scoreboards.add(scoreboard);
                    return null;
                }
                catch (IOException e) {
                    return e;
                }
            }).filter(Objects::nonNull).forEach(exception::addSuppressed);
        }
        catch (IOException e) {
            exception.addSuppressed(e);
        }

        if (exception.getSuppressed().length > 0) {
            throw exception;
        }

        getPlugin().getLogger().info(scoreboards.size() + " scoreboard(s) loaded.");
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

    public void save() throws IOException {
        IOException exception = new IOException("One or more errors occurred while loading scoreboards.");
        Path dir = getPlugin().getDataFolder().toPath().resolve("scoreboards");
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            }
            catch (IOException e) {
                exception.addSuppressed(exception);
            }
        }

        scoreboards.stream().filter(MusiScoreboard::saveData).map(scoreboard -> {
            try {
                YamlConfigurationLoader loader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).file(dir.resolve(scoreboard.getName() + ".yml").toFile()).defaultOptions(c -> c.serializers(SERIALIZERS)).build();
                ConfigurationNode node = loader.createNode(n -> n.set(scoreboard));
                loader.save(node);
                return null;
            }
            catch (IOException e) {
                return e;
            }
        }).filter(Objects::nonNull).forEach(exception::addSuppressed);

        if (exception.getSuppressed().length > 0) {
            throw exception;
        }

        getPlugin().getLogger().info(scoreboards.size() + " scoreboard(s) saved.");
    }

    public void setScoreboard(Player player, MusiScoreboard scoreboard) {
        scoreboards.forEach(s -> s.removePlayer(player));
        scoreboard.addPlayer(player);
    }
}
