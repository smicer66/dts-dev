package uk.gov.hmcts.reform.dev.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.dev.models.DTSTask;

import java.math.BigInteger;

@Repository
public interface IDTSTaskRepository extends JpaRepository<DTSTask, BigInteger>{

}
