package dssd.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActividadBonita {
    @JsonProperty("displayDescription")
    private String displayDescription;

    @JsonProperty("executedBy")
    private String executedBy;

    @JsonProperty("rootContainerId")
    private String rootContainerId;

    @JsonProperty("assigned_date")
    private String assignedDate;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("executedBySubstitute")
    private String executedBySubstitute;

    @JsonProperty("dueDate")
    private String dueDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private String type;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("actorId")
    private String actorId;

    @JsonProperty("processId")
    private String processId;

    @JsonProperty("caseId")
    private String caseId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("reached_state_date")
    private String reachedStateDate;

    @JsonProperty("rootCaseId")
    private String rootCaseId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("parentCaseId")
    private String parentCaseId;

    @JsonProperty("last_update_date")
    private String lastUpdateDate;

    @JsonProperty("assigned_id")
    private String assignedId;
}
