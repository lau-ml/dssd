package dssd.server.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileRoleInRequest {

    /*
    * group_id	"1"
profile_id	"2"
role_id	"3"
    * */

    private String group_id;
    private String profile_id;
    private String role_id;
}
