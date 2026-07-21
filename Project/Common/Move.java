package Project.Common;
// Date: July 20th   |    UCID: am4239
// Created file: 
//
// Created the Move enum representing ROCK, PAPER, and SCISSORS options.
// Implemented trigger strings and the fromText() lookup method 
// to convert user text input into Move values.

public enum Move {
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors");

    private final String trigger;

    Move(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
    }

    /** Parses input text and returns the matching Move enum, or null if invalid. */
    public static Move fromText(String text) {
        if (text == null) 
            return null;
        String lower = text.toLowerCase().trim();
        for (Move m : values()) {
            if (lower.equals(m.trigger) || lower.startsWith(m.trigger + " ")) {
                return m;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return trigger;
    }
}