package io.musician101.musiboard.dialog.team;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput.MultilineOptions;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class TeamDialog extends MusiDialog {

    private static final String ALLOW_FRIENDLY_FIRE = "allow_friendly_fire";
    private static final String COLLISION_RULE = "collision_rule";
    private static final String DISPLAY_NAME = "display_name";
    private static final String DEATH_MESSAGE_VISIBILITY = "death_message_visibility";
    private static final String ENTRIES = "entries";
    private static final String NAME_TAG_VISIBILITY = "name_tag_visibility";
    private static final String PREFIX = "prefix";
    private static final String SEE_FRIENDLY_INVISIBLES = "see_friendly_invisibles";
    private static final String SUFFIX = "suffix";
    private final Team team;
    private final TeamsDialog teamsDialog;

    public TeamDialog(Team team, TeamsDialog teamsDialog) {
        super(Component.join(JoinConfiguration.spaces(), Component.text("Editing"), team.displayName()));
        this.team = team;
        this.teamsDialog = teamsDialog;
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput displayName = textInput(DISPLAY_NAME, Component.text("Display Name"), team.displayName());
        DialogInput prefix = textInput(PREFIX, Component.text("Prefix"), team.prefix());
        DialogInput suffix = textInput(SUFFIX, Component.text("Suffix"), team.suffix());
        DialogInput allowFriendlyFire = boolInput(ALLOW_FRIENDLY_FIRE, Component.text("Allow Friendly Fire"), team.allowFriendlyFire());
        DialogInput seeFriendlyInvisibles = boolInput(SEE_FRIENDLY_INVISIBLES, Component.text("See Friendly Invisibles"), team.canSeeFriendlyInvisibles());
        DialogInput collisionRule = optionInput(COLLISION_RULE, Option.COLLISION_RULE, "Collision Rule");
        DialogInput deathMessageVisibility = optionInput(DEATH_MESSAGE_VISIBILITY, Option.DEATH_MESSAGE_VISIBILITY, "Death Message Visibility");
        DialogInput nameTagVisibility = optionInput(NAME_TAG_VISIBILITY, Option.NAME_TAG_VISIBILITY, "Name Tag Visibility");
        String teamEntries = String.join("\n", team.getEntries());
        DialogInput entries = DialogInput.text(ENTRIES, Component.text("Entries")).multiline(MultilineOptions.create(Integer.MAX_VALUE, 100)).initial(teamEntries).build();
        return List.of(displayName, prefix, suffix, allowFriendlyFire, seeFriendlyInvisibles, collisionRule, deathMessageVisibility, nameTagVisibility, entries);
    }

    private DialogInput optionInput(String key, Option option, String label) {
        List<OptionEntry> entries = Arrays.stream(OptionStatus.values()).map(s -> {
            String name = switch (s) {
                case ALWAYS -> "Always";
                case FOR_OTHER_TEAMS -> "For Other Teams";
                case FOR_OWN_TEAM -> "For Own Team";
                case NEVER -> "Never";
            };
            return OptionEntry.create(s.toString(), Component.text(name), team.getOption(option) == s);
        }).toList();
        return singleOptionInput(key, Component.text(label), entries);
    }

    @Override
    protected DialogType type() {
        ActionButton colorButton = actionButton(Component.text("Color"), (view, audience) -> {
            // Vanilla only accepts the hard coded colors. This might change in the future, and we'll update to reflect that change when that happens
            audience.showDialog(new ColorDialog((NamedTextColor) team.color(), team, this).build());
        });
        ActionButton applyButton = applyButton(this::applyAction);
        ActionButton deleteButton = actionButton(Component.text("Delete"), (view, audience) -> {
            team.unregister();
            audience.showDialog(teamsDialog.build());
        });
        ActionButton discardButton = backButton(showMusiDialogCallback(teamsDialog));
        return multiActionType(List.of(colorButton, applyButton, deleteButton), discardButton, 1);
    }

    private void applyAction(DialogResponseView view, Audience audience) {
        team.displayName(textResult(view.getText(DISPLAY_NAME)));
        team.prefix(textResult(view.getText(PREFIX)));
        team.suffix(textResult(view.getText(SUFFIX)));
        team.setAllowFriendlyFire(boolResult(view.getBoolean(ALLOW_FRIENDLY_FIRE)));
        team.setCanSeeFriendlyInvisibles(boolResult(view.getBoolean(SEE_FRIENDLY_INVISIBLES)));
        team.setOption(Option.COLLISION_RULE, optionResult(view.getText(COLLISION_RULE)));
        team.setOption(Option.DEATH_MESSAGE_VISIBILITY, optionResult(view.getText(DEATH_MESSAGE_VISIBILITY)));
        team.setOption(Option.NAME_TAG_VISIBILITY, optionResult(view.getText(NAME_TAG_VISIBILITY)));
        String entriesString = view.getText(ENTRIES);
        if (entriesString != null) {
            List<String> entries = Stream.concat(team.getEntries().stream(), Arrays.stream(entriesString.split("\n"))).distinct().toList();
            team.addEntries(entries);
        }

        audience.showDialog(teamsDialog.build());
    }

    @Nullable
    private Component textResult(@Nullable String response) {
        return response == null ? null : GsonComponentSerializer.gson().deserialize(response);
    }

    private boolean boolResult(@Nullable Boolean response) {
        return response != null && response;
    }

    private OptionStatus optionResult(@Nullable String response) {
        if (response == null) {
            return OptionStatus.ALWAYS;
        }

        return switch (response) {
            // Keep this just for sanity/future-proofing
            case "Always" -> OptionStatus.ALWAYS;
            case "For Other Teams" -> OptionStatus.FOR_OTHER_TEAMS;
            case "For Own Team" -> OptionStatus.FOR_OWN_TEAM;
            case "Never" -> OptionStatus.NEVER;
            default -> OptionStatus.ALWAYS;
        };
    }
}
