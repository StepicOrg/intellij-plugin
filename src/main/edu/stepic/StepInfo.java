package main.edu.stepic;

import java.io.Serializable;

/**
 * Created by Petr on 14.06.2016.
 */
public class StepInfo implements Serializable {
    private String stepID;
    private String attemptID;
    private String packageName;

//    public static final StepInfo EMPTY = new StepInfo("");


    public StepInfo() {
        stepID = "";
        attemptID = "";
        packageName = "";
    }

    public StepInfo(String stepID) {
        this.stepID = stepID;
        attemptID = "";
        packageName = "";
    }

    public StepInfo(String stepID, String packageName) {
        this.stepID = stepID;
        this.packageName = packageName;
        attemptID = "";
    }

    public String getStepID() {
        return stepID;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public String getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(String attemptID) {
        this.attemptID = attemptID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "StepInfo{" +
                "stepID='" + stepID + '\'' +
                ", attemptID='" + attemptID + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
