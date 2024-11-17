package dssd.server.requests;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class RegisterGroupRequest {

    private String description;
    private String displayName;
    private String name;
}
