package io.musician101.musiboard.dialog.numerformat;

import io.musician101.musiboard.dialog.MusiDialog;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface NumberFormatHolder {

    void numberFormat(@Nullable NumberFormat format);

    @Nullable NumberFormat numberFormat();

    @NonNull MusiDialog previousDialog();
}
