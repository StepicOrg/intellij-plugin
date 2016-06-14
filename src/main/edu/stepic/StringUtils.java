package main.edu.stepic;

public class StringUtils {
    public static String normalize(String text) {
        String[] words = text.split("\\W+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            sb.append(words[i] + "_");
        }
        if (words.length > 0)
            sb.append(words[words.length - 1]);
        return sb.toString();
    }
}
