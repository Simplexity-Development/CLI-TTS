package simplexity.util;

public enum ConsoleColor {
    // Reset
    RESET("0", "reset", "<reset>", ""), // Text Reset
    BOLD("1", "bold", "<bold>", "</bold>"),
    ITALIC("3", "italic", "<i>", "</i>"),
    UNDERLINE("4", "underline", "<u>", "</u>"),
    STRIKETHROUGH("5", "strikethrough", "<s>", "</s>"),
    // Regular Colors
    BLACK("30", "black", "<black>", "</black>"),   // BLACK
    RED("31", "red", "<red>", "</red>"),     // RED
    GREEN("32", "green", "<green>", "</green>"),   // GREEN
    YELLOW("33", "yellow", "<yellow>", "</yellow>"),  // YELLOW
    BLUE("34", "blue", "<blue>", "</blue>"),    // BLUE
    PURPLE("35", "purple", "<purple>", "</purple>"),  // PURPLE
    CYAN("36", "cyan", "<cyan>", "</cyan>"),    // CYAN
    WHITE("37", "white", "<white>", "</white>"),   // WHITE

    // Background
    BLACK_BACKGROUND("40", "black-background", "<black-bg>", "</black-bg>"),  // BLACK
    RED_BACKGROUND("41", "red-background", "<red-bg>", "</red-bg>"),    // RED
    GREEN_BACKGROUND("42", "green-background", "<green-bg>", "</green-bg>"),  // GREEN
    YELLOW_BACKGROUND("43", "yellow-background", "<yellow-bg>", "</yellow-bg>"), // YELLOW
    BLUE_BACKGROUND("44", "blue-background", "<blue-bg>", "</blue-bg>"),   // BLUE
    PURPLE_BACKGROUND("45", "purple-background", "<purple-bg>", "</purple-bg>"), // PURPLE
    CYAN_BACKGROUND("46", "cyan-background", "<cyan-bg>", "</cyan-bg>"),   // CYAN
    WHITE_BACKGROUND("47", "white-background", "<white-bg>", "</white-bg>"),  // WHITE

    // High Intensity
    BLACK_BRIGHT("90", "bright-black", "<br-black>", "</br-black>"),  // BLACK
    RED_BRIGHT("91", "bright-red", "<br-red>", "</br-red>"),    // RED
    GREEN_BRIGHT("92", "bright-green", "<br-green>", "</br-green>"),  // GREEN
    YELLOW_BRIGHT("93", "bright-yellow", "<br-yellow>", "</br-yellow>"), // YELLOW
    BLUE_BRIGHT("94", "bright-blue", "<br-blue>", "</br-blue>"),   // BLUE
    PURPLE_BRIGHT("95", "bright-purple", "<br-purple>", "</br-purple>"), // PURPLE
    CYAN_BRIGHT("96", "bright-cyan", "<br-cyan>", "</br-cyan>"),   // CYAN
    WHITE_BRIGHT("97", "bright-white", "<br-white>", "</br-white>"),  // WHITE

    // High Intensity backgrounds
    BLACK_BACKGROUND_BRIGHT("100", "bright-black-background", "<br-black-bg>", "</br-black-bg>"),// BLACK
    RED_BACKGROUND_BRIGHT("101", "bright-red-background", "<br-red-bg>", "</br-red-bg>"),// RED
    GREEN_BACKGROUND_BRIGHT("102", "bright-green-background", "<br-green-bg>", "</br-green-bg>"),// GREEN
    YELLOW_BACKGROUND_BRIGHT("103", "bright-yellow-background", "<br-yellow-bg>", "</br-yellow-bg>"),// YELLOW
    BLUE_BACKGROUND_BRIGHT("104", "bright-blue-background", "<br-blue-bg>", "</br-blue-bg>"),// BLUE
    PURPLE_BACKGROUND_BRIGHT("105", "bright-purple-background", "<br-purple-bg>", "</br-purple-bg>"), // PURPLE
    CYAN_BACKGROUND_BRIGHT("106", "bright-cyan-background", "<br-cyan-bg>", "</br-cyan-bg>"),  // CYAN
    WHITE_BACKGROUND_BRIGHT("107", "bright-white-background", "<br-white-bg>", "</br-white-bg>");

    // WHITE

    private final String ansiCode;
    private final String name;
    private final String startTag;
    private final String endTag;

    ConsoleColor(String ansiCode, String name, String startTag, String endTag) {
        this.ansiCode = ansiCode;
        this.name = name;
        this.startTag = startTag;
        this.endTag = endTag;
    }

    public String getAnsiCode() {
        return ansiCode;
    }

    public String getName() {
        return name;
    }

    public String getStartTag() {
        return startTag;
    }

    public String getEndTag() {
        return endTag;
    }

    public static ConsoleColor fromTag(String tag) {
        for (ConsoleColor color : values()) {
            if (color.getStartTag().equalsIgnoreCase(tag) || color.getEndTag().equalsIgnoreCase(tag)) {
                return color;
            }
        }
        return null; // Tag not found
    }
}
