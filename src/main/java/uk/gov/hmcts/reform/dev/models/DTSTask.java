package uk.gov.hmcts.reform.dev.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.dev.enums.CaseStatus;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "DTSTrack")
public class DTSTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = true)
    private String description;

    @Column(name="caseStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private CaseStatus caseStatus;

    @Column(name="dueDateTime", nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime dueDateTime;
}
