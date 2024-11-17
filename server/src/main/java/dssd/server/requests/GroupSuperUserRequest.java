package dssd.server.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupSuperUserRequest {
    /*
    *
    * group_id	"1"
member_type	"GROUP"
profile_id	"2"*/

    private String group_id;
    private String member_type;
    private String profile_id;


}
