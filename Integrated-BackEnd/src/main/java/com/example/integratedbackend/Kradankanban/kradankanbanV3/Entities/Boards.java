package com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "board", schema = "kradankanbanV3")
public class Boards {

    @Id
    @Column(name = "boardId", nullable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "visibilities", nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibilities visibility = Visibilities.PRIVATE;

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    private Users users;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Collab> collab;
}
