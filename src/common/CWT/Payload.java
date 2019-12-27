package common.CWT;


import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Payload {
    private final String username;
    private final ZonedDateTime creationTime;

    public Payload(String username){
        this.username = username;
        this.creationTime = ZonedDateTime.now();
    }

    Payload(JSONObject jsonPayload){
        this.username = jsonPayload.getString("username");
        this.creationTime = ZonedDateTime.parse(jsonPayload.getString("creationTime"));
    }

    public String GetUsername(){
        return username;
    }

    public ZonedDateTime GetCreationTime(){
        return creationTime;
    }

    JSONObject toJSONObject(){
        return new JSONObject("{\"username\":\"" + username + "\", \"creationTime\":\"" + creationTime.toString() + "\"}");
    }
}
