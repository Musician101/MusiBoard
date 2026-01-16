package io.musician101.musiboard.scoreboard.serialize;

import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@NullMarked
public class DummyScore {

    private final int score;
    private final String name;
    private final String objective;
    @Nullable
    private final Component display;
    @Nullable
    private final NumberFormat format;

    public DummyScore(int score, String name, String objective, @Nullable Component display, @Nullable NumberFormat format) {
        this.score = score;
        this.name = name;
        this.objective = objective;
        this.display = display;
        this.format = format;
    }

    public static DummyScore toDummy(Score score) {
        return new DummyScore(score.getScore(), score.getEntry(), score.getObjective().getName(), score.customName(), score.numberFormat());
    }

    public static Score fromDummy(MusiScoreboard scoreboard, DummyScore dummy) throws SerializationException {
        Objective objective = scoreboard.getObjective(dummy.objective);
        if (objective == null) {
            throw new SerializationException("Objective " + dummy.objective + " does not exist.");
        }

        Score score = objective.getScore(dummy.name);
        score.setScore(dummy.score);
        score.customName(dummy.display);
        score.numberFormat(dummy.format);
        return score;
    }

    public static class Serializer implements TypeSerializer<DummyScore> {

        @Override
        public DummyScore deserialize(Type type, ConfigurationNode node) throws SerializationException {
            int score = node.node("Score").require(Integer.class);
            String name = node.node("Name").require(String.class);
            String objective = node.node("Objective").require(String.class);
            String displayString = node.node("display").getString();
            Component display = null;
            if (displayString != null) {
                display = GsonComponentSerializer.gson().deserialize(displayString);
            }

            NumberFormat format = node.node("format").get(NumberFormat.class);
            return new DummyScore(score, name, objective, display, format);
        }

        @Override
        public void serialize(Type type, @Nullable DummyScore obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                node.set(null);
                return;
            }

            node.node("Score").set(obj.score);
            node.node("Name").set(obj.name);
            node.node("Objective").set(obj.objective);
            String display = obj.display == null ? null : GsonComponentSerializer.gson().serialize(obj.display);
            node.node("display").set(display);
            node.node("format").set(obj.format);
        }
    }
}
