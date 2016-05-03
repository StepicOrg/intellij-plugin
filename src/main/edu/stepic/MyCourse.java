package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getSection;

/**
 * Implementation of class which contains information to be shawn in course description in tool window
 * and when project is being created
 */
public class MyCourse {
    public int id;
    public String title;
    public String summary;

    @SerializedName("sections")
    public List<Integer> sectionsId;
//    @SerializedName("authors")
//    public List<Integer> authorsId;


    transient Map<Integer, MySection> sections = new HashMap<>();
//    Map<Integer,Authors> authors = new HashMap<>();


    public static MyCourse INVALID_COURSE = new MyCourse();

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "MyCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sectionsId=" + sectionsId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyCourse that = (MyCourse) o;
        if (that.id == this.id) return true;
        return false;
    }

//    @Override
//    public int hashCode() {
//        int result = myName != null ? myName.hashCode() : 0;
//        result = 31 * result + (myDescription != null ? myDescription.hashCode() : 0);
//        return result;
//    }

    public void build() throws UnirestException {
        for (Integer sectionId : sectionsId) {
            MySection section = getSection(Integer.toString(sectionId));
            sections.put(sectionId, section);
            section.build();
        }
    }
}
