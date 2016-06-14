package main.edu.stepic;

import java.io.Serializable;

public class StepInfo implements Serializable {
    private String stepID;
    private String attemptID;
    private String packageName;
    private String filename;

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

    public StepInfo(String stepID, String packageName, String filename) {
        this.stepID = stepID;
        this.packageName = packageName;
        this.attemptID = "";
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
