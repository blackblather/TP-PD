package common.model;

public class ServerResponse {
    public final boolean success;
    public final String type, description;
    public final Object ref;

    public ServerResponse(boolean success, String type, String description, Object ref){
        this.success = success;
        this.type = type;
        this.description = description;
        this.ref = ref;
    }
}