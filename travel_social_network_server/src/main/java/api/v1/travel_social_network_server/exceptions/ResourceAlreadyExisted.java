package api.v1.travel_social_network_server.exceptions;

public class ResourceAlreadyExisted extends RuntimeException {
    public ResourceAlreadyExisted(String message) {
        super(message);
    }
}