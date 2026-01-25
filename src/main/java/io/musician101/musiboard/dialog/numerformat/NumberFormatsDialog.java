package io.musician101.musiboard.dialog.numerformat;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.scoreboard.numbers.FixedFormat;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import io.papermc.paper.scoreboard.numbers.StyledFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public class NumberFormatsDialog extends MusiDialog {

    protected final NumberFormatHolder formatHolder;

    public NumberFormatsDialog(NumberFormatHolder formatHolder) {
        super(Component.text("Number Format"));
        this.formatHolder = formatHolder;
    }

    @Override
    protected List<DialogBody> body() {
        NumberFormat format = formatHolder.numberFormat();
        String input = "Number format is currently not set.";
        if (format != null) {
            String formatType = switch (format) {
                case FixedFormat ignored -> "Fixed";
                case StyledFormat ignored -> "Styled";
                default -> "Blank";
            };
            input = "<bold><gold>" + formatType + "</bold></gold> is currently selected.";
        }

        Component message = MiniMessage.miniMessage().deserialize(input);
        return List.of(DialogBody.plainMessage(message));
    }

    @Override
    protected DialogType type() {
        ActionButton blankButton = actionButton(Component.text("Blank"), (view, audience) -> {
            formatHolder.numberFormat(NumberFormat.blank());
            audience.showDialog(formatHolder.previousDialog().build());
        });
        ActionButton fixedButton = actionButton(Component.text("Fixed"), showMusiDialogCallback(new FixedFormatDialog(this)));
        ActionButton styleButton = actionButton(Component.text("Styled"), showMusiDialogCallback(new StyledFormatDialog(this)));
        ActionButton backButton = backButton(showMusiDialogCallback(formatHolder.previousDialog()));
        return multiActionType(List.of(blankButton, fixedButton, styleButton), backButton, 1);
    }
}
