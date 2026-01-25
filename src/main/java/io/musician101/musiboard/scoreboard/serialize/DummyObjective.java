package io.musician101.musiboard.scoreboard.serialize;

import io.musician101.musiboard.scoreboard.MusiScoreboard;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

@NullMarked
public record DummyObjective(String name, Component displayName, Criteria criteria, RenderType renderType,
                             boolean displayAutoUpdate, @Nullable NumberFormat format) {

    public static Objective fromDummy(MusiScoreboard scoreboard, DummyObjective dummy) {
        Objective objective = scoreboard.registerNewObjective(dummy.name, dummy.criteria, dummy.displayName, dummy.renderType);
        objective.setAutoUpdateDisplay(dummy.displayAutoUpdate);
        objective.numberFormat(dummy.format);
        return objective;
    }

    public static DummyObjective toDummy(Objective objective) {
        return new DummyObjective(objective.getName(), objective.displayName(), objective.getTrackedCriteria(), objective.getRenderType(), objective.willAutoUpdateDisplay(), objective.numberFormat());
    }

    public static class Serializer implements TypeSerializer<DummyObjective> {

        @Override
        public DummyObjective deserialize(Type type, ConfigurationNode node) throws SerializationException {
            String name = node.node("Name").require(String.class);
            Component displayName = GsonComponentSerializer.gson().deserialize(node.node("DisplayName").getString(name));
            String criteriaName = node.node("Criteria").require(String.class);
            Criteria criteria = Criteria.create(criteriaName);
            RenderType renderType = Arrays.stream(RenderType.values()).filter(r -> r.toString().equalsIgnoreCase(node.node("RenderType").getString())).findFirst().orElse(RenderType.INTEGER);
            boolean displayAutoUpdate = node.node("display_auto_update").getBoolean(true);
            NumberFormat format = node.node("format").get(NumberFormat.class);
            return new DummyObjective(name, displayName, criteria, renderType, displayAutoUpdate, format);
        }

        @Override
        public void serialize(Type type, @Nullable DummyObjective obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                node.set(null);
                return;
            }

            node.node("Name").set(obj.name);
            node.node("DisplayName").set(GsonComponentSerializer.gson().serialize(obj.displayName));
            node.node("Criteria").set(obj.criteria.getName());
            node.node("RenderType").set(obj.renderType.toString().toLowerCase());
            node.node("display_auto_udpate").set(obj.displayAutoUpdate);
            node.node("format").set(obj.format);
        }
    }
}
