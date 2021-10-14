package com.test.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "device")
public class Device{
    @Id
    @Column(name="uid")
    private String uid;

    @Column(name="vendor")
    private String vendor;

    @Column(name="status")
    private String status;

    @Column(name="gateway_serial_number")
    private String gatewaySerialNumber;

    @CreationTimestamp
    @Column(name="created", nullable=false, updatable=false)
    private Date created;

    @UpdateTimestamp
    @Column(name="updated")
    private Date updated;
}