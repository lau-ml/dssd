package dssd.server.requests;

import lombok.*;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRoleRequest {
    private String description;
    private String displayName;
    private String name;
}
