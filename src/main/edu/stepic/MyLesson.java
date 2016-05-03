package main.edu.stepic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Petr on 02.05.2016.
 */
public class MyLesson {
    int id;
    @SerializedName("steps")
    List<Integer> stepsId;

}
