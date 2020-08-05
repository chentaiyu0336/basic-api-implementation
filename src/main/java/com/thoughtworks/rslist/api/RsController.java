package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<RsEvent>();

  {
    User userXiaoMin = new User("XiaoMin",20,"male","xm@163.com","12357439274");
    rsList.add(new RsEvent("第一事件", "无分类", userXiaoMin));
    rsList.add(new RsEvent("第二事件", "无分类", userXiaoMin));
    rsList.add(new RsEvent("第三事件", "无分类", userXiaoMin));
  }


  @GetMapping("/rs/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index) {
    return rsList.get(index-1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start,
                                   @RequestParam(required = false) Integer end) {
    if(start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start-1, end);
  }

  @PostMapping("/rs/event")
  public void addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if(rsEvent.getKeyWord()==null||rsEvent.getEventName()==null||rsEvent.getUser()==null)
      return;
    rsList.add(rsEvent);
    if(!UserController.users.contains(rsEvent.getUser())){
      UserController.users.add(rsEvent.getUser());
    }
  }

  @PostMapping("/rs/{index}")
  public void changeOneRsEvent(@PathVariable int index, @RequestBody String reString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(reString, RsEvent.class);
    RsEvent replaceRsEvent = rsList.get(index-1);
    if(rsEvent.getEventName()==null) {
      if(rsEvent.getKeyWord()==null) {
        return;
      }
      else {
        replaceRsEvent.setUser(rsEvent.getUser());
        replaceRsEvent.setKeyWord(rsEvent.getKeyWord());
      }
    }
    else {
      replaceRsEvent.setUser(rsEvent.getUser());
      replaceRsEvent.setEventName(rsEvent.getEventName());
      if(rsEvent.getKeyWord()==null) {
        return;
      }
      else {
        replaceRsEvent.setKeyWord(rsEvent.getKeyWord());
      }
    }
  }

  @DeleteMapping("/rs/{index}")
  public void deleteOneRsEvent(@PathVariable int index) {
    rsList.remove(index-1);
  }


}
