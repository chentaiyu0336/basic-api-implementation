package com.thoughtworks.rslist.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rsEvent")
public class RsEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String eventName;
    private String keyword;
    private Integer votNum;
    @Column(name = "user_id")
    private Integer userId;

    @JoinColumn(name="user_id",insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
