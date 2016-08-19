package org.stepik.plugin.modules;

import java.io.Serializable;

public class StepInfo implements Serializable {
    private String stepID;
    private String attemptID;
    private String packageName;
    private String filename;
    private String submissionID;
    private String text;

//    public static final StepInfo EMPTY = new StepInfo("");


    private void setAllEmpy() {
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

    public StepInfo(String stepID, String packageName, String filename, String text) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StepInfo stepInfo = (StepInfo) o;

        if (stepID != null ? !stepID.equals(stepInfo.stepID) : stepInfo.stepID != null) return false;
        if (attemptID != null ? !attemptID.equals(stepInfo.attemptID) : stepInfo.attemptID != null) return false;
        if (packageName != null ? !packageName.equals(stepInfo.packageName) : stepInfo.packageName != null)
            return false;
        if (filename != null ? !filename.equals(stepInfo.filename) : stepInfo.filename != null) return false;
        if (submissionID != null ? !submissionID.equals(stepInfo.submissionID) : stepInfo.submissionID != null)
            return false;
        return text != null ? text.equals(stepInfo.text) : stepInfo.text == null;

    }

    @Override
    public int hashCode() {
        int result = stepID != null ? stepID.hashCode() : 0;
        result = 31 * result + (attemptID != null ? attemptID.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (submissionID != null ? submissionID.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
