package api.v1.travel_social_network_server.reponses;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {
    private List<T> content;
    private long totalPages;
    private long totalElements;
}
