package dssd.server.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleSuperUserRequest {
    /*member_type	"ROLE"
profile_id	"2"
role_id	"3"*/

    private String member_type;
    private String profile_id;
    private String role_id;

}
