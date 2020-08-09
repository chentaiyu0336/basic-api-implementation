package com.thoughtworks.rslist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.api.UserController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsEvent implements Serializable {
//
//    public interface PublicView {
//    }
//    public interface PrivateView extends PublicView {
//    }


    @NotNull
    private String eventName;


    @NotNull
    private String keyword;


    @NotNull
    private int userId;

//    public RsEvent(String eventName, String keyWord, User user) {
//        this.eventName = eventName;
//        this.keyWord = keyWord;
//        this.user = user;
//    }
//
//    public String getEventName() {
//        return eventName;
//    }
//
//    public void setEventName(String eventName) {
//        this.eventName = eventName;
//    }
//
//    public String getKeyWord() {
//        return keyWord;
//    }
//
//    public void setKeyWord(String keyWord) {
//        this.keyWord = keyWord;
//    }
}
