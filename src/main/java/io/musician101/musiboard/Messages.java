package io.musician101.musiboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Messages {

    TagResolver PREFIX_RESOLVER = TagResolver.resolver("mb-prefix", Tag.selfClosingInserting(Component.text("[MB]")));
}
