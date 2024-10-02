package dssd.server.helpers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBonita {


    private String firstname;
    private String icon;
    private String creationDate;
    private String userName;
    private String title;
    private String createdByUserId;
    private boolean enabled;
    private String lastname;
    private String lastConnection;
    private String password;
    private String managerId;
    private String id;
    private String jobTitle;
    private String lastUpdateDate;

    public UserBonita() {}

}
