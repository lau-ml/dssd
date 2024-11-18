package dssd.server.requests;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class RegisterBonitaRequest {
    private String enabled;
    private String firstname;
    private String lastname;
    private String password;
    private String password_confirm;
    private String userName;
}
