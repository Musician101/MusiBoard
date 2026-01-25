package io.musician101.musiboard.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput.OptionEntry;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.set.RegistryValueSet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickCallback.Options;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public abstract class MusiDialog {

    protected static final Options DEFAULT_CALLBACK_OPTIONS = Options.builder().uses(1).lifetime(ClickCallback.DEFAULT_LIFETIME).build();

    protected final Component label;

    public MusiDialog(Component label) {
        this.label = label;
    }

    public Dialog build() {
        return Dialog.create(b -> b.empty().base(base()).type(type()));
    }

    protected DialogBase base() {
        return DialogBase.builder(label).externalTitle(label).inputs(inputs()).body(body()).build();
    }

    protected List<DialogInput> inputs() {
        return List.of();
    }

    protected List<DialogBody> body() {
        return List.of();
    }

    protected abstract DialogType type();

    protected DialogAction customClick(DialogActionCallback callback) {
        return customClick(callback, DEFAULT_CALLBACK_OPTIONS);
    }

    protected DialogAction customClick(DialogActionCallback callback, Options options) {
        return DialogAction.customClick(callback, options);
    }

    protected ActionButton backButton() {
        return backButton((view, audience) -> {

        });
    }

    protected ActionButton backButton(DialogActionCallback callback) {
        return actionButton(Component.text("Back"), callback);
    }

    protected ActionButton applyButton(DialogActionCallback callback) {
        return actionButton(Component.text("Apply"), callback);
    }

    protected ActionButton actionButton(Component label, DialogActionCallback callback) {
        return ActionButton.builder(label).action(customClick(callback)).build();
    }

    protected RegistryValueSet<Dialog> dialogs(Dialog... dialogs) {
        return dialogs(Arrays.asList(dialogs));
    }

    protected RegistryValueSet<Dialog> dialogs(List<Dialog> dialogs) {
        return RegistrySet.valueSet(RegistryKey.DIALOG, dialogs);
    }

    protected DialogAction showMusiDialogAction(MusiDialog dialog) {
        return showDialogAction(dialog.build());
    }

    protected DialogActionCallback showMusiDialogCallback(MusiDialog dialog) {
        return showDialogCallback(dialog.build());
    }

    protected DialogAction showDialogAction(Dialog dialog) {
        return customClick(showDialogCallback(dialog));
    }

    protected DialogActionCallback showDialogCallback(Dialog dialog) {
        return (view, audience) -> audience.showDialog(dialog);
    }

    protected DialogType multiActionType(List<ActionButton> buttons, ActionButton exitAction) {
        return multiActionType(buttons, exitAction, 2);
    }

    protected DialogType multiActionType(List<ActionButton> buttons, ActionButton exitAction, int columns) {
        return DialogType.multiAction(buttons, exitAction, columns);
    }

    protected DialogInput boolInput(String key, Component label, boolean initial) {
        return DialogInput.bool(key, label).initial(initial).build();
    }

    protected DialogInput textInput(String key, Component label) {
        return textInput(key, label, "");
    }

    protected DialogInput textInput(String key, Component label, String initial) {
        return DialogInput.text(key, label).initial(initial).build();
    }

    protected DialogInput textInput(String key, Component label, Component initial) {
        return DialogInput.text(key, label).initial(GsonComponentSerializer.gson().serialize(initial)).build();
    }

    protected DialogInput singleOptionInput(String key, Component label, List<OptionEntry> entries) {
        return DialogInput.singleOption(key, label, entries).build();
    }
}
