package uk.gov.hmcts.reform.dev.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.dev.models.DTSTask;

import java.math.BigInteger;

@Repository
/*
JPA Repository that performs the queries used in the service layer.
This repository also performs the updates, insertion and deletion of instances of the DTSTask.
Queries have been written using JPQL similar to HQL
 */
public interface IDTSTaskRepository extends JpaRepository<DTSTask, Long>{

    @Query("SELECT dts FROM DTSTask dts WHERE dts.deletedAt IS NULL AND dts.title = :title")
    DTSTask getDTSTaskByTitle(String title);


    @Query("SELECT dts FROM DTSTask dts WHERE dts.deletedAt IS NULL AND dts.title = :title AND dts.id != :taskId")
    DTSTask getDTSTaskByTitleForUpdate(String title, Long taskId);

    @Modifying
    @Query(value = "TRUNCATE TABLE DTSTrack", nativeQuery = true)
    void truncateTable();

}
