package uk.gov.hmcts.reform.dev.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum CaseStatus {
    FILED("FILED", "Filed"),
    PENDING("PENDING", "Pending"),
    UNDER_INVESTIGATION("UNDER_INVESTIGATION", "Under Investigation"),
    PRE_TRIAL("PRE_TRIAL", "Pre-Trial"),
    ACTIVE_TRIAL("ACTIVE_TRIAL","Under Investigation"),
    ADJOURNED("ADJOURNED","Adjourned"),
    DISMISSED("DISMISSED", "Dismissed"),
    SETTLED("SETTLED", "Settled"),
    JUDGEMENT_GIVEN("JUDGEMENT_GIVEN", "Judgment Given"),
    APPEAL_FILED("APPEAL_FILED", "Appeal Filed"),
    CLOSED("CLOSED", "Case Closed");


    private String statusCode;
    private String statusDescription;

    CaseStatus(String statusCode, String statusDescription)
    {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }


    @JsonCreator
    public CaseStatus fromStatusCode(String statusCode)
    {
        return Arrays.stream(CaseStatus.values())
            .toList()
            .stream()
            .filter(cs -> {
                return cs.statusCode.equalsIgnoreCase(statusCode);
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Status code provided must match one of the valid status codes."));
    }

    @JsonValue
    public String getStatusCode()
    {
        return this.statusCode;
    }

    public String getStatusDescription()
    {
        return this.statusDescription;
    }
}
