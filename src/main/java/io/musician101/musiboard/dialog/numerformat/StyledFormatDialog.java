package io.musician101.musiboard.dialog.numerformat;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import io.papermc.paper.scoreboard.numbers.StyledFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class StyledFormatDialog extends MusiDialog {

    private final static String COLOR = "color";
    private static final String SHADOW_COLOR = "shadow_color";
    private final NumberFormatsDialog numberFormatsDialog;

    public StyledFormatDialog(NumberFormatsDialog numberFormatsDialog) {
        super(Component.text("Styled Format"));
        this.numberFormatsDialog = numberFormatsDialog;
    }

    @Override
    protected List<DialogInput> inputs() {
        List<DialogInput> inputs = new ArrayList<>();
        inputs.add(textInput(COLOR, Component.text("Color"), initialTextColor()));
        inputs.add(textInput(SHADOW_COLOR, Component.text("Shadow Color"), initialShadowColor()));
        TextDecoration.NAMES.keyToValue().forEach((name, deco) -> {
            boolean initial = numberFormatsDialog.formatHolder.numberFormat() instanceof StyledFormat styled && styled.style().hasDecoration(deco);
            Component label = Component.text(StringUtils.capitalize(name));
            inputs.add(boolInput(name, label, initial));
        });
        return inputs;
    }

    private String initialShadowColor() {
        if (numberFormatsDialog.formatHolder.numberFormat() instanceof StyledFormat styled) {
            ShadowColor color = styled.style().shadowColor();
            if (color == null) {
                return "";
            }

            return color.asHexString();
        }

        return "";
    }

    private String initialTextColor() {
        if (numberFormatsDialog.formatHolder.numberFormat() instanceof StyledFormat styled) {
            TextColor color = styled.style().color();
            if (color == null) {
                return "";
            }

            if (color instanceof NamedTextColor named) {
                String key = NamedTextColor.NAMES.key(named);
                return key == null ? "" : key;
            }

            return color.asHexString();
        }

        return "";
    }

    @SuppressWarnings("PatternValidation")
    @Nullable
    private ShadowColor shadowColor(@Nullable String colorValue) {
        if (colorValue == null) {
            return null;
        }

        return ShadowColor.fromHexString(colorValue);
    }

    @Nullable
    private TextColor color(@Nullable String colorValue) {
        if (colorValue == null) {
            return null;
        }

        TextColor color = TextColor.fromHexString(colorValue);
        if (color != null) {
            return color;
        }

        return NamedTextColor.NAMES.value(colorValue);
    }

    @Override
    protected DialogType type() {
        ActionButton applyButton = applyButton((view, audience) -> {
            TextColor color = color(view.getText(COLOR));
            Style style = Style.style(color);
            style.shadowColor(shadowColor(view.getText(SHADOW_COLOR)));
            TextDecoration.NAMES.keyToValue().forEach((name, deco) -> {
                Boolean value = view.getBoolean(name);
                style.decoration(deco, State.byBoolean(value));
            });
            numberFormatsDialog.formatHolder.numberFormat(NumberFormat.styled(style));
            audience.showDialog(numberFormatsDialog.build());
        });
        ActionButton backButton = backButton(showMusiDialogCallback(numberFormatsDialog));
        return multiActionType(List.of(applyButton), backButton, 1);
    }
}
