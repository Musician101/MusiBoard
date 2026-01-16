package io.musician101.musiboard.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@NullMarked
public class MusiScoreboard {

    private final String name;
    private final List<String> objectivesNoSave;
    private final List<UUID> players = new ArrayList<>();
    private final Scoreboard scoreboard;
    private final List<String> teamsNoSave;
    private boolean saveData;

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

    public List<String> getTeamsNoSave() {
        return teamsNoSave;
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

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void resetScores(String entry) {
        scoreboard.resetScores(entry);
    }

    public boolean saveData() {
        return saveData;
    }

    public void saveData(boolean saveData) {
        this.saveData = saveData;
    }
}
