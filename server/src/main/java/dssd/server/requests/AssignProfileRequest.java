package dssd.server.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignProfileRequest {

    private String user_id;
    private String member_type;
    private String profile_id;
}
