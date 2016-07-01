package org.stepic.plugin.modules;

public class Attempt {
    public int id;
    public String status;
    public String step;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attempt attempt = (Attempt) o;

        if (id != attempt.id) return false;
        if (status != null ? !status.equals(attempt.status) : attempt.status != null) return false;
        return step != null ? step.equals(attempt.step) : attempt.step == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (step != null ? step.hashCode() : 0);
        return result;
    }
}
