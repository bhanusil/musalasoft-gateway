package com.test.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "gateway")
public class Gateway {
    @Id
    @Column(name="serial_number")
    private String serialNumber;

    @Column(name="name")
    private String name;

    @Column(name="ip_address")
    private String ipAddress;

    @OneToMany(mappedBy = "gatewaySerialNumber")
    private List<Device> devices = new ArrayList<>();

    @CreationTimestamp
    @Column(name="created", nullable=false, updatable=false)
    private Date created;

    @UpdateTimestamp
    @Column(name="updated")
    private Date updated;
}