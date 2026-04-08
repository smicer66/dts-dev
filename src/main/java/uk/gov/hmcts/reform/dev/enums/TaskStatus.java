package uk.gov.hmcts.reform.dev.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;


//These status have been fetched from online in relation to court cases
public enum TaskStatus {
    FILED("FILED", "Filed"),
    PENDING("PENDING", "Pending"),
    PRE_TRIAL("PRE_TRIAL", "Pre-Trial"),
    ACTIVE_TRIAL("ACTIVE_TRIAL","Under Investigation"),
    ADJOURNED("ADJOURNED","Adjourned"),
    DISMISSED("DISMISSED", "Dismissed"),
    SETTLED("SETTLED", "Settled"),
    JUDGEMENT_GIVEN("JUDGEMENT_GIVEN", "Judgment Given"),
    APPEAL_FILED("APPEAL_FILED", "Appeal Filed"),
    CLOSED("CLOSED", "Case Closed");


    private final String code;
    private final String value;

    TaskStatus(String code, String value)
    {
        this.code = code;
        this.value = value;
    }


//    @JsonCreator
//    public TaskStatus fromStatusCode(String statusCode)
//    {
//        return Arrays.stream(TaskStatus.values())
//            .toList()
//            .stream()
//            .filter(cs -> {
//                return cs.statusCode.equalsIgnoreCase(statusCode);
//            })
//            .findFirst()
//            .orElseThrow(() -> new IllegalArgumentException("Status code provided must match one of the valid status codes."));
//    }

    //@JsonValue
    public String getCode()
    {
        return this.code;
    }

    public String getValue()
    {
        return this.value;
    }

    public static boolean isValidCode(String code) {
        for (TaskStatus s : values()) {
            if (s.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
