package main.edu.stepic;

/**
 * Created by Petr on 03.06.2016.
 */
public class StringUtils {
    public static String normilize(String text){
        String[] words = text.split("\\W+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(words[i] + "_");
        }
        return sb.toString();
    }
}
