package org.stepik.plugin.modules;

import java.util.Map;

public class Submission {
    public int id;
    private String status;
    private String time;
    private String hint;
    private Map<String, String> reply;

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public Map<String, String> getReply() {
        return reply;
    }

    public String getCode() {
        return reply.get("code");
    }

    public String getHint() {
        return hint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Submission that = (Submission) o;

        if (id != that.id) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (hint != null ? !hint.equals(that.hint) : that.hint != null) return false;
        return reply != null ? reply.equals(that.reply) : that.reply == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (hint != null ? hint.hashCode() : 0);
        result = 31 * result + (reply != null ? reply.hashCode() : 0);
        return result;
    }
}
