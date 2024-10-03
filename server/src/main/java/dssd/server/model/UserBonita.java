package dssd.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBonita {


    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("created_by_user_id")
    private String createdByUserId;
    @JsonProperty("enabled")
    private boolean enabled;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("last_connection")
    private String lastConnection;
    @JsonProperty("password")
    private String password;
    @JsonProperty("manager_id")
    private String managerId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("job_title")
    private String jobTitle;
    @JsonProperty("last_update_date")
    private String lastUpdateDate;

    public UserBonita() {}

}
