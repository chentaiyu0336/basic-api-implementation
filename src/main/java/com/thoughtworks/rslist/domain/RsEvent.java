package com.thoughtworks.rslist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.api.UserController;

import javax.validation.Valid;

public class RsEvent {
    public RsEvent() {}

    public interface PublicView {
    }
    public interface PrivateView extends PublicView {
    }

    @JsonView(PublicView.class)
    private String eventName;

    @JsonView(PublicView.class)
    private String keyWord;

    @JsonView(PrivateView.class)
    private @Valid User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user = user;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
