package api.v1.travel_social_network_server.reponses;

import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private StatusRequestEnum status;
    private T data;
    private String message;
}

