package dssd.server.helpers;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessBonita {
    private String displayDescription;
    private String deploymentDate;
    private String displayName;
    private String name;
    private String description;
    private String deployedBy;
    private String id;
    private String activationState;
    private String version;
    private String configurationState;
    private String lastUpdateDate;
    private String actorInitiatorId;


    public ProcessBonita() {}

}
