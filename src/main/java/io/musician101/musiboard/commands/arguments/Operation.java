package io.musician101.musiboard.commands.arguments;

import io.musician101.musicommand.core.command.CommandException;
import org.bukkit.scoreboard.Score;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum Operation {

    ASSIGNMENT("=") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(b.getScore());
        }
    },
    ADDITION("+=") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(a.getScore() + b.getScore());
        }
    },
    MINUS("-=") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(a.getScore() - b.getScore());
        }
    },
    MULTIPLICATION("*=") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(a.getScore() * b.getScore());
        }
    },
    FLOOR_DIVISION("/=") {
        @Override
        public void apply(Score a, Score b) throws CommandException {
            if (b.getScore() == 0) {
                throw new CommandException("Cannot divide by 0!");
            }

            a.setScore(Math.floorDiv(a.getScore(), b.getScore()));
        }
    },
    MODULUS("%=") {
        @Override
        public void apply(Score a, Score b) throws CommandException {
            if (b.getScore() == 0) {
                throw new CommandException("Cannot divide by 0!");
            }

            a.setScore(Math.floorMod(a.getScore(), b.getScore()));
        }
    },
    MINIMUM("<") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(Math.min(a.getScore(), b.getScore()));
        }
    },
    MAXIMUM(">") {
        @Override
        public void apply(Score a, Score b) {
            a.setScore(Math.max(a.getScore(), b.getScore()));
        }
    },
    SWAPPING("><") {
        @Override
        public void apply(Score a, Score b) {
            int i = a.getScore();
            a.setScore(b.getScore());
            b.setScore(i);
        }
    };

    private final String operator;

    Operation(String operator) {
        this.operator = operator;
    }

    public abstract void apply(Score a, Score b) throws CommandException;

    public String operator() {
        return operator;
    }
}
