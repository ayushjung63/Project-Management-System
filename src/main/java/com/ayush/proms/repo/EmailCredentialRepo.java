package com.ayush.proms.repo;

import com.ayush.proms.model.EmailCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCredentialRepo extends JpaRepository<EmailCredential,Long> {

    @Query(nativeQuery=true,value="select * from email_crendential where username= ?1")
    EmailCredential findByUsername(String username);
}
