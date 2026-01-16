package io.musician101.musiboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Messages {

    TagResolver PREFIX_RESOLVER = TagResolver.resolver("mb-prefix", Tag.selfClosingInserting(Component.text("[MB]")));

    static TagResolver objectiveResolver(Objective objective) {
        return TagResolver.resolver("objective", Tag.selfClosingInserting(objective.displayName()));
    }

    static TagResolver teamResolver(Team team) {
        return TagResolver.resolver("team", Tag.selfClosingInserting(team.displayName()));
    }
}
