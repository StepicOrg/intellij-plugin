package main.courseFormat;

import java.io.Serializable;

public class StepInfo implements Serializable {
    private String stepID;
    private String attemptID;
    private String packageName;
    private String filename;
    private String submissionID;

//    public static final StepInfo EMPTY = new StepInfo("");


    public StepInfo() {
        stepID = "";
        attemptID = "";
        packageName = "";
        filename = "";
        submissionID = "";
    }

    public StepInfo(String stepID) {
        this.stepID = stepID;
        attemptID = "";
        packageName = "";
        filename = "";
        submissionID = "";
    }

    public StepInfo(String stepID, String packageName, String filename) {
        this.stepID = stepID;
        this.packageName = packageName;
        this.filename = filename;
        this.attemptID = "";
        submissionID = "";
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

    public String getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(String submissionID) {
        this.submissionID = submissionID;
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
