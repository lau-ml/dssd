package dssd.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Case {
    @JsonProperty("caseId")
    String caseId;
}
