package main.edu.stepic;

import java.util.Map;
import java.util.Set;

/**
 * Created by Petr on 04.05.2016.
 */
public class CourseTree {
    static MyCourse course;

    public CourseTree(MyCourse course) {
        this.course = course;
    }

    public static void build() {
        Set<Map.Entry<Integer, MySection>> me = course.sections.entrySet();
    }
}
