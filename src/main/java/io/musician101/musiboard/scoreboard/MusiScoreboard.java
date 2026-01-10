package io.musician101.musiboard.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.musician101.musiboard.MusiBoard.getPlugin;

@NullMarked
public class MusiScoreboard {

    private final String name;
    private final List<String> objectivesNoSave;
    private final List<UUID> players = new ArrayList<>();
    private final Scoreboard scoreboard;
    private final List<String> teamsNoSave;
    private boolean saveData;

    public MusiScoreboard(ConfigurationSection cs) {
        this.name = checkNotNull(cs.getString("Name"));
        this.saveData = cs.getBoolean("SaveData");
        ConfigurationSection noSave = cs.getConfigurationSection("NoSave");
        this.objectivesNoSave = noSave != null ? noSave.getStringList("Objectives") : new ArrayList<>();
        this.teamsNoSave = noSave != null ? noSave.getStringList("Teams") : new ArrayList<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        getList(cs, "Objectives").forEach(o -> {
            String name = checkNotNull(o.getString("Name"));
            String displayName = o.getString("DisplayName", name);
            Criteria criteria = Criteria.create(checkNotNull(o.getString("Criteria")));
            RenderType renderType = Stream.of(RenderType.values()).filter(r -> r.toString().equalsIgnoreCase(o.getString("RenderType"))).findFirst().orElse(RenderType.INTEGER);
            scoreboard.registerNewObjective(name, criteria, GsonComponentSerializer.gson().deserialize(displayName), renderType);
        });
        getList(cs, "PlayerScores").forEach(s -> {
            int scr = s.getInt("Score");
            String name = checkNotNull(s.getString("Name"));
            String objective = checkNotNull(s.getString("Objective"));
            Score score = checkNotNull(scoreboard.getObjective(objective)).getScore(name);
            score.setScore(scr);
        });
        getList(cs, "Teams").forEach(t -> {
            Team team = scoreboard.registerNewTeam(checkNotNull(t.getString("Name")));
            team.setAllowFriendlyFire(t.getBoolean("AllowFriendlyFire"));
            team.setCanSeeFriendlyInvisibles(t.getBoolean("SeeFriendlyInvisibles"));
            team.setOption(Option.NAME_TAG_VISIBILITY, toStatus(t.getString("NameTagVisibility")));
            team.setOption(Option.DEATH_MESSAGE_VISIBILITY, toStatus(t.getString("DeathMessageVisibility")));
            team.setOption(Option.COLLISION_RULE, toStatus(t.getString("CollisionRule")));
            setIfNotNull(cs.getString("DisplayName"), team::displayName);
            setIfNotNull(cs.getString("MemberNamePrefix"), team::prefix);
            setIfNotNull(cs.getString("MemberNameSuffix"), team::prefix);
            t.getStringList("Players").forEach(team::addEntry);
        });
        cs.getStringList("Players").stream().map(UUID::fromString).forEach(players::add);
        ConfigurationSection displaySlots = cs.getConfigurationSection("DisplaySlots");
        if (displaySlots != null) {
            Stream.of(DisplaySlot.values()).forEach(d -> {
                String objective = displaySlots.getString("slot_" + d.toString().toLowerCase());
                if (objective != null) {
                    checkNotNull(scoreboard.getObjective(objective)).setDisplaySlot(d);
                }
            });
        }
    }

    public MusiScoreboard(String name) {
        this(name, true);
    }

    public MusiScoreboard(String name, boolean saveData) {
        this(name, saveData, Bukkit.getScoreboardManager().getNewScoreboard());
    }

    protected MusiScoreboard(String name, boolean saveData, Scoreboard scoreboard) {
        this.name = name;
        this.saveData = saveData;
        this.scoreboard = scoreboard;
        this.objectivesNoSave = new ArrayList<>();
        this.teamsNoSave = new ArrayList<>();
    }

    void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
        players.add(player.getUniqueId());
    }

    public void clearSlot(DisplaySlot slot) {
        scoreboard.clearSlot(slot);
    }

    public void disableObjectiveSave(Objective objective) {
        disableObjectiveSave(objective.getName());
    }

    public void disableObjectiveSave(String objective) {
        objectivesNoSave.add(objective);
    }

    public void disableTeamSave(Team team) {
        disableTeamSave(team.getName());
    }

    public void disableTeamSave(String team) {
        teamsNoSave.add(team);
    }

    public void enableObjectiveSave(Objective objective) {
        enableObjectiveSave(objective.getName());
    }

    public void enableObjectiveSave(String objective) {
        objectivesNoSave.add(objective);
    }

    public void enableTeamSave(Team team) {
        enableTeamSave(team.getName());
    }

    public void enableTeamSave(String team) {
        teamsNoSave.add(team);
    }

    public Set<String> getEntries() {
        return scoreboard.getEntries();
    }

    @Nullable
    public Team getEntryTeam(String entry) throws IllegalArgumentException {
        return scoreboard.getEntryTeam(entry);
    }

    private Stream<ConfigurationSection> getList(ConfigurationSection cs, String key) {
        return cs.getList(key, List.of()).stream().filter(ConfigurationSection.class::isInstance).map(ConfigurationSection.class::cast);
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Objective getObjective(String name) throws IllegalArgumentException {
        return scoreboard.getObjective(name);
    }

    @Nullable
    public Objective getObjective(DisplaySlot slot) throws IllegalArgumentException {
        return scoreboard.getObjective(slot);
    }

    public Set<Objective> getObjectives() {
        return scoreboard.getObjectives();
    }

    public Set<Objective> getObjectivesByCriteria(Criteria criteria) throws IllegalArgumentException {
        return scoreboard.getObjectivesByCriteria(criteria);
    }

    public List<String> getObjectivesNoSave() {
        return objectivesNoSave;
    }

    public Set<Score> getScores(String entry) throws IllegalArgumentException {
        return scoreboard.getScores(entry);
    }

    @Nullable
    public Team getTeam(String teamName) throws IllegalArgumentException {
        return scoreboard.getTeam(teamName);
    }

    public Set<Team> getTeams() {
        return scoreboard.getTeams();
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean isObjectiveSaveDisabled(Objective objective) {
        return isObjectiveSaveDisabled(objective.getName());
    }

    public boolean isObjectiveSaveDisabled(String objective) {
        return objectivesNoSave.contains(objective);
    }

    public boolean isTeamSaveDisabled(Team team) {
        return isTeamSaveDisabled(team.getName());
    }

    public boolean isTeamSaveDisabled(String team) {
        return teamsNoSave.contains(team);
    }

    public Objective registerNewObjective(String name, Criteria criteria, Component displayName) throws IllegalArgumentException {
        return registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    public Objective registerNewObjective(String name, Criteria criteria, Component displayName, RenderType renderType) throws IllegalArgumentException {
        return scoreboard.registerNewObjective(name, criteria, displayName, renderType);
    }

    public Team registerNewTeam(String name) {
        return scoreboard.registerNewTeam(name);
    }

    void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void resetScores(String entry) {
        scoreboard.resetScores(entry);
    }

    public void save() {
        if (saveData) {
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("Name", name);
            yaml.set("SaveData", saveData);
            ConfigurationSection noSave = yaml.createSection("noSave");
            noSave.set("Objectives", objectivesNoSave);
            noSave.set("Teams", teamsNoSave);
            List<ConfigurationSection> objectives = new ArrayList<>();
            List<ConfigurationSection> scores = new ArrayList<>();
            scoreboard.getObjectives().stream().filter(objective -> objectivesNoSave.stream().noneMatch(o -> o.equals(objective.getName()))).forEach(o -> {
                YamlConfiguration objective = new YamlConfiguration();
                objective.set("CriteriaName", o.getTrackedCriteria().getName());
                set(objective, "DisplayName", o.displayName());
                objective.set("Name", o.getName());
                objective.set("RenderType", o.getRenderType().toString().toLowerCase());
                objectives.add(objective);
                scoreboard.getEntries().forEach(e -> {
                    YamlConfiguration score = new YamlConfiguration();
                    Score s = o.getScore(e);
                    int scoreAmount = s.getScore();
                    if (scoreAmount == 0) {
                        return;
                    }

                    score.set("Score", scoreAmount);
                    score.set("Name", e);
                    score.set("Objective", o.getName());
                    scores.add(score);
                });
            });
            yaml.set("Objectives", objectives);
            yaml.set("PlayerScores", scores);
            yaml.set("Teams", scoreboard.getTeams().stream().filter(team -> teamsNoSave.stream().noneMatch(t -> t.equals(team.getName()))).map(t -> {
                YamlConfiguration y = new YamlConfiguration();
                y.set("AllowFriendlyFire", t.allowFriendlyFire());
                y.set("SeeFriendlyInvisibles", t.canSeeFriendlyInvisibles());
                y.set("NameTagVisibility", toString(t.getOption(Option.NAME_TAG_VISIBILITY)));
                y.set("DeathMessageVisibility", toString(t.getOption(Option.DEATH_MESSAGE_VISIBILITY)));
                y.set("CollisionRule", toString(t.getOption(Option.COLLISION_RULE)));
                set(y, "DisplayName", t.displayName());
                y.set("Name", t.getName());
                set(y, "MemberNamePrefix", t.prefix());
                set(y, "MemberNameSuffix", t.suffix());
                y.set("Players", t.getEntries());
                return y;
            }).toList());
            YamlConfiguration y = new YamlConfiguration();
            Arrays.stream(DisplaySlot.values()).forEach(d -> {
                Objective o = scoreboard.getObjective(d);
                if (o != null) {
                    y.set("slot_" + d.toString().toLowerCase(), o.getName());
                }
            });
            yaml.set("DisplaySlots", y);
            yaml.set("Players", players.stream().map(UUID::toString).toList());
            try {
                Path data = getPlugin().getDataFolder().toPath().resolve("scoreboards");
                if (Files.notExists(data)) {
                    Files.createDirectory(data);
                }

                Path path = data.resolve(name + ".yml");
                if (Files.notExists(path)) {
                    Files.createFile(path);
                }

                yaml.save(path.toFile());
            }
            catch (IOException e) {
                getPlugin().getLogger().log(Level.SEVERE, "An error occurred while trying to save " + name + ".yml", e);
            }
        }
    }

    public boolean saveData() {
        return saveData;
    }

    public void saveData(boolean saveData) {
        this.saveData = saveData;
    }

    private void setIfNotNull(@Nullable String value, Consumer<Component> action) {
        if (value != null) {
            action.accept(GsonComponentSerializer.gson().deserialize(value));
        }
    }

    private void set(ConfigurationSection cs, String key, Component value) {
        cs.set(key, GsonComponentSerializer.gson().serialize(value));
    }

    private OptionStatus toStatus(@Nullable String string) {
        return Stream.of(OptionStatus.values()).filter(o -> o.toString().equalsIgnoreCase(string)).findFirst().orElse(OptionStatus.ALWAYS);
    }

    private String toString(OptionStatus status) {
        return status.toString().toLowerCase();
    }
}
