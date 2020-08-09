package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.entity.RsEventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class User {
    public User(String name, int age, String gender, String email, String phone) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    @Size(max = 8)
    @NotNull
    private String name;

    @Min(18)
    @Max(100)
    @NotNull
    private int age;

    @NotNull
    private String gender;

    @Email
    private String email;

    @Pattern(regexp = "1\\d{10}")
    @NotNull
    private String phone;

    //private int vote=10;

}
