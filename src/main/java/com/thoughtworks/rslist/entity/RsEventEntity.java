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
    @GeneratedValue
    private Integer id;
    private String eventName;
    private String keyword;
    private Integer votNum;
    private Integer userId;

}
