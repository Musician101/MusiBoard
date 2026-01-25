package io.musician101.musiboard.scoreboard.serialize;

import io.musician101.musiboard.scoreboard.MusiScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class DummyTeam {

    private final String name;
    private Component displayName;
    private boolean allowFriendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private OptionStatus collisionRule = OptionStatus.ALWAYS;
    private OptionStatus deathMessageVisibility = OptionStatus.ALWAYS;
    private OptionStatus nameTagVisibility = OptionStatus.ALWAYS;
    // Setting this to null will mimic vanilla behavior.
    @Nullable
    private NamedTextColor color = null;
    private Component prefix = Component.empty();
    private Component suffix = Component.empty();
    private List<String> players = List.of();

    public DummyTeam(String name) {
        this.name = name;
        this.displayName = Component.text(name);
    }

    public static Team fromDummy(MusiScoreboard scoreboard, DummyTeam dummy) {
        Team team = scoreboard.registerNewTeam(dummy.name);
        team.displayName(dummy.displayName);
        team.color(dummy.color);
        team.setAllowFriendlyFire(dummy.allowFriendlyFire);
        team.setCanSeeFriendlyInvisibles(dummy.seeFriendlyInvisibles);
        team.setOption(Option.NAME_TAG_VISIBILITY, dummy.nameTagVisibility);
        team.setOption(Option.DEATH_MESSAGE_VISIBILITY, dummy.deathMessageVisibility);
        team.setOption(Option.COLLISION_RULE, dummy.collisionRule);
        team.prefix(dummy.prefix);
        team.suffix(dummy.suffix);
        team.addEntries(dummy.players);
        return team;
    }

    public static DummyTeam toDummy(Team team) {
        DummyTeam dummy = new DummyTeam(team.getName());
        dummy.displayName = team.displayName();
        dummy.allowFriendlyFire = team.allowFriendlyFire();
        dummy.seeFriendlyInvisibles = team.canSeeFriendlyInvisibles();
        dummy.nameTagVisibility = team.getOption(Option.NAME_TAG_VISIBILITY);
        dummy.deathMessageVisibility = team.getOption(Option.DEATH_MESSAGE_VISIBILITY);
        dummy.collisionRule = team.getOption(Option.COLLISION_RULE);
        dummy.color = NamedTextColor.nearestTo(team.color());
        dummy.prefix = team.prefix();
        dummy.suffix = team.suffix();
        dummy.players = new ArrayList<>(team.getEntries());
        return dummy;
    }


    public static class Serializer implements TypeSerializer<DummyTeam> {

        @Override
        public DummyTeam deserialize(Type type, ConfigurationNode node) throws SerializationException {
            String name = node.node("Name").require(String.class);
            DummyTeam team = new DummyTeam(name);
            GsonComponentSerializer gson = GsonComponentSerializer.gson();
            team.displayName = gson.deserialize(node.node("DisplayName").getString(name));
            team.allowFriendlyFire = node.node("AllowFriendlyFire").getBoolean(true);
            team.seeFriendlyInvisibles = node.node("SeeFriendlyInvisibles").getBoolean(true);
            team.nameTagVisibility = node.node("nameTagVisibility").require(OptionStatus.class);
            team.deathMessageVisibility = node.node("DeathMessageVisibility").require(OptionStatus.class);
            team.collisionRule = node.node("CollisionRule").require(OptionStatus.class);
            team.color = node.node("TeamColor").require(NamedTextColor.class);
            team.prefix = getComponent("MemberNamePrefix", node);
            team.suffix = getComponent("MemberNameSuffix", node);
            team.players = node.node("Players").getList(String.class, List.of());
            return team;
        }

        private Component getComponent(String path, ConfigurationNode node) {
            String string = node.node(path).getString();
            if (string == null) {
                return Component.empty();
            }

            return GsonComponentSerializer.gson().deserialize(string);
        }

        @Override
        public void serialize(Type type, @Nullable DummyTeam obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                node.set(null);
                return;
            }

            node.node("Name").set(obj.name);
            node.node("DisplayName").set(GsonComponentSerializer.gson().serialize(obj.displayName));
            node.node("AllowFriendlyFire").set(obj.allowFriendlyFire);
            node.node("SeeFriendlyInvisibles").set(obj.seeFriendlyInvisibles);
            node.node("nameTagVisibility").set(obj.nameTagVisibility);
            node.node("DeathMessageVisibility").set(obj.deathMessageVisibility);
            node.node("CollisionRule").set(obj.collisionRule);
            node.node("TeamColor").set(obj.color);
            node.node("Players").set(obj.players);
        }
    }
}
