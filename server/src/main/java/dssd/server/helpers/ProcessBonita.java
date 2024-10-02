package dssd.server.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessBonita {
    @JsonProperty("displayDescription")
    private String displayDescription;
    @JsonProperty("deploymentDate")
    private String deploymentDate;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("deployedBy")
    private String deployedBy;
    @JsonProperty("id")
    private String id;
    @JsonProperty("activationState")
    private String activationState;
    @JsonProperty("version")
    private String version;
    @JsonProperty("configurationState")
    private String configurationState;
    @JsonProperty("last_update_date")
    private String lastUpdateDate;
    @JsonProperty("actorinitiatorid")
    private String actorInitiatorId;


    public ProcessBonita() {}

}
