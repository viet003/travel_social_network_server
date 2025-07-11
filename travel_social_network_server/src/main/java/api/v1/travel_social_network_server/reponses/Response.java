package api.v1.travel_social_network_server.reponses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> {
    private Integer status;
    private T data;
    private String message;
}

