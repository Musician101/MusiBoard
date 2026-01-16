package io.musician101.musiboard.scoreboard.serialize;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@NullMarked
public class OptionStatusSerializer implements TypeSerializer<OptionStatus> {

    private final static BiMap<OptionStatus, String> STATUSES = ImmutableBiMap.of(
            OptionStatus.ALWAYS, "always",
            OptionStatus.NEVER, "never",
            OptionStatus.FOR_OTHER_TEAMS, "pushOtherTeams",
            OptionStatus.FOR_OWN_TEAM, "pushOwnTeam");

    @Override
    public OptionStatus deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String status = node.require(String.class).toLowerCase();
        return STATUSES.inverse().getOrDefault(status, OptionStatus.ALWAYS);
    }

    @Override
    public void serialize(Type type, @Nullable OptionStatus obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.set(STATUSES.getOrDefault(obj, "always"));
    }
}
