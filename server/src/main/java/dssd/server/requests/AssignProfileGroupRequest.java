package dssd.server.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignProfileGroupRequest {


    private String group_id;
    private String member_type;
    private String profile_id;
}
