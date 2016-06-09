package main.edu.stepic;

import java.util.Map;

/**
 * Created by Petr on 21.05.2016.
 */
public class SubmissionsNode {
    private String status;
    private String time;
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
}
