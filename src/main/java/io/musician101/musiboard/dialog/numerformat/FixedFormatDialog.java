package io.musician101.musiboard.dialog.numerformat;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput.MultilineOptions;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.scoreboard.numbers.FixedFormat;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class FixedFormatDialog extends MusiDialog {

    private static final String VALUE = "value";
    private final NumberFormatsDialog numberFormatsDialog;

    public FixedFormatDialog(NumberFormatsDialog numberFormatsDialog) {
        super(Component.text("Fixed Format"));
        this.numberFormatsDialog = numberFormatsDialog;
    }

    protected DialogType type() {
        ActionButton apply = applyButton((view, audience) -> {
            String text = view.getText(VALUE);
            Component component = text == null ? Component.empty() : GsonComponentSerializer.gson().deserialize(text);
            numberFormatsDialog.formatHolder.numberFormat(NumberFormat.fixed(component));
            audience.showDialog(numberFormatsDialog.build());
        });
        ActionButton backButton = backButton(showMusiDialogCallback(numberFormatsDialog));
        return DialogType.confirmation(apply, backButton);
    }

    @Override
    protected List<DialogInput> inputs() {
        DialogInput value = DialogInput.text(VALUE, Component.empty())
                .labelVisible(false)
                .initial(valueString())
                .multiline(MultilineOptions.create(512, 512))
                .maxLength(512)
                .build();
        return List.of(value);
    }

    private String valueString() {
        if (numberFormatsDialog.formatHolder.numberFormat() instanceof FixedFormat fixed) {
            return GsonComponentSerializer.gson().serialize(fixed.component());
        }

        return "";
    }
}
