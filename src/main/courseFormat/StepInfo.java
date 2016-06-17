package main.courseFormat;

import java.io.Serializable;

public class StepInfo implements Serializable {
    private String stepID;
    private String attemptID;
    private String packageName;
    private String filename;
    private String submissionID;
    private String text;

//    public static final StepInfo EMPTY = new StepInfo("");


    private void setAllEmpy(){
        stepID = "";
        attemptID = "";
        packageName = "";
        filename = "";
        submissionID = "";
        text = "";
    }

    public StepInfo() {
        setAllEmpy();
    }

    public StepInfo(String stepID) {
        setAllEmpy();
        this.stepID = stepID;
    }

    public StepInfo(String stepID, String packageName, String filename) {
        setAllEmpy();
        this.stepID = stepID;
        this.packageName = packageName;
        this.filename = filename;
    }

    public StepInfo(String stepID, String packageName, String filename,String text) {
        setAllEmpy();
        this.stepID = stepID;
        this.packageName = packageName;
        this.filename = filename;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
