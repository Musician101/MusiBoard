package io.musician101.musiboard.scoreboard.serialize;

import io.musician101.musiboard.scoreboard.MusiScoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NullMarked
public class MusiScoreboardSerializer implements TypeSerializer<MusiScoreboard> {

    @Override
    public MusiScoreboard deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String name = node.node("Name").getString();
        if (name == null) {
            throw new SerializationException("Scoreboard name can not be null!");
        }

        MusiScoreboard scoreboard = new MusiScoreboard(name);
        scoreboard.saveData(node.node("SaveData").getBoolean(false));
        ConfigurationNode noSave = node.node("NoSave");
        noSave.node("Objectives").getList(String.class, new ArrayList<>()).forEach(scoreboard::disableObjectiveSave);
        noSave.node("Teams").getList(String.class, new ArrayList<>()).forEach(scoreboard::disableTeamSave);
        node.node("Objectives").getList(DummyObjective.class, List.of()).forEach(dummy -> DummyObjective.fromDummy(scoreboard, dummy));
        SerializationException exception = new SerializationException("An error occurred while parsing PlayerScores.");
        node.node("PlayerScores").getList(DummyScore.class, List.of()).stream().map(dummy -> {
            try {
                DummyScore.fromDummy(scoreboard, dummy);
                return null;
            }
            catch (SerializationException e) {
                return e;
            }
        }).filter(Objects::nonNull).forEach(exception::addSuppressed);
        if (exception.getSuppressed().length > 0) {
            throw exception;
        }

        node.node("Teams").getList(DummyTeam.class, List.of()).forEach(dummy -> DummyTeam.fromDummy(scoreboard, dummy));
        node.node("Players").getList(UUID.class, List.of()).forEach(scoreboard::addPlayer);
        for (DisplaySlot displaySlot : DisplaySlot.values()) {
            String objectiveName = node.node("DisplaySlots", displaySlot.getId()).getString();
            if (objectiveName == null) {
                continue;
            }

            Objective objective = scoreboard.getObjective(objectiveName);
            if (objective == null) {
                continue;
            }

            objective.setDisplaySlot(displaySlot);
        }

        return scoreboard;
    }

    @Override
    public void serialize(Type type, @Nullable MusiScoreboard obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        String name = node.node("Name").getString();
        if (name == null) {
            throw new SerializationException("Scoreboard name can not be null!");
        }

        obj.saveData(node.node("SaveData").getBoolean(false));
        node.node("SaveData").set(obj.saveData());
        ConfigurationNode noSave = node.node("NoSave");
        noSave.node("Objectives").set(obj.getObjectivesNoSave());
        noSave.node("Teams").set(obj.getTeamsNoSave());
        List<DummyObjective> objectives = new ArrayList<>();
        List<DummyScore> scores = new ArrayList<>();
        for (Objective o : obj.getObjectives()) {
            if (obj.isObjectiveSaveDisabled(o)) {
                continue;
            }

            objectives.add(DummyObjective.toDummy(o));
            obj.getEntries().stream().map(o::getScore).filter(s -> s.getScore() > 0).forEach(score -> scores.add(DummyScore.toDummy(score)));
            DisplaySlot displaySlot = o.getDisplaySlot();
            if (displaySlot != null) {
                node.node("DisplaySlots", displaySlot.getId()).set(o.getName());
            }
        }

        node.node("Objectives").set(objectives);
        node.node("PlayerScores").set(scores);
        node.node("Teams").set(obj.getTeams().stream().filter(t -> !obj.isTeamSaveDisabled(t)).map(DummyTeam::toDummy).toList());
        node.node("Players").set(obj.getPlayers());
    }
}
