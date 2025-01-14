package simplexity.util;

import java.util.ArrayList;

public class ColorTags {

    public static String parse(String input) {
        StringBuilder result = new StringBuilder();
        ArrayList<ConsoleColor> activeColors = new ArrayList<>();
        int tagStart = -1;
        StringBuilder currentText = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // Detect the start of a tag
            if (c == '<') {
                result.append(currentText); // Append collected text
                currentText.setLength(0);   // Clear the buffer
                tagStart = i;
            }
            // Detect the end of a tag
            else if (c == '>' && tagStart != -1) {
                String tag = input.substring(tagStart, i + 1);
                ConsoleColor color = ConsoleColor.fromTag(tag);

                if (color != null) {
                    if (tag.equalsIgnoreCase(color.getStartTag())) {
                        // Opening tag: Add the ANSI code
                        activeColors.add(0, color);
                        result.append(generateAnsiCode(activeColors));
                    } else if (tag.equalsIgnoreCase(color.getEndTag())) {
                        // Closing tag: Remove the ANSI code
                        activeColors.remove(color); // Remove specific color
                        result.append(generateAnsiCode(activeColors));
                    }
                }
                tagStart = -1; // Reset tag tracking
            }
            // Append characters outside of tags
            else if (tagStart == -1) {
                currentText.append(c);
            }
        }

        // Append any remaining text
        result.append(currentText);

        // Reset to default at the end
        result.append("\u001B[0m"); // ANSI reset

        return result.toString();
    }

    /**
     * Generates the ANSI code sequence for the currently active colors.
     *
     * @param activeColors Stack of active ConsoleColor enums.
     * @return Combined ANSI escape code.
     */
    private static String generateAnsiCode(ArrayList<ConsoleColor> activeColors) {
        if (activeColors.isEmpty()) {
            return "\u001B[0m"; // Reset if no colors are active
        }

        StringBuilder ansiCode = new StringBuilder("\u001B[");
        for (int i = 0; i < activeColors.size(); i++) {
            ansiCode.append(activeColors.get(i).getAnsiCode());
            if (i < activeColors.size() - 1) {
                ansiCode.append(";"); // Separate codes with semicolons
            }
        }
        ansiCode.append("m");
        return ansiCode.toString();
    }
}
