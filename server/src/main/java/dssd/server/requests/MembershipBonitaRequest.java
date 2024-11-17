package dssd.server.requests;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class MembershipBonitaRequest {

    private String group_id;
    private String role_id;
    private String user_id;

}
