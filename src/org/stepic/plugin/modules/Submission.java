package org.stepic.plugin.modules;

import java.util.Map;

public class Submission {
    public int id;
    private String status;
    private String time;
    private String hint;
    private Map<String,String> reply;

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
}
