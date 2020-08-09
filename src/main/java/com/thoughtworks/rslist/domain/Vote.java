package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Min(0)
    @Max(10)
    @NotNull
    private Integer voteNum;
    @NotNull
    private Integer userId;
    @NotNull
    private String voteTime;
}
