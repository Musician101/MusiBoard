package io.musician101.musiboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.text;

@NullMarked
public interface Messages {

    Component PREFIX = text("[MB] ");
    TagResolver PREFIX_RESOLVER = TagResolver.resolver("mb-prefix", Tag.selfClosingInserting(Component.text("[MB]")));

    static TagResolver objectiveResolver(Objective objective) {
        return TagResolver.resolver("objective", Tag.selfClosingInserting(objective.displayName()));
    }

    static TagResolver teamResolver(Team team) {
        return TagResolver.resolver("team", Tag.selfClosingInserting(team.displayName()));
    }
}
