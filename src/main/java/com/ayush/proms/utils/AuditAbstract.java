package com.ayush.proms.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditAbstract {
    @CreatedBy
    private String createdBy;

    @Basic
    @CreatedDate
    private Timestamp createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @Basic
    @LastModifiedDate
    private Timestamp lastModifiedDate;
}
