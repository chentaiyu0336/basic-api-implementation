package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = new ArrayList<RsEvent>();

  {
    rsList.add(new RsEvent("第一事件", "无分类"));
    rsList.add(new RsEvent("第二事件", "无分类"));
    rsList.add(new RsEvent("第三事件", "无分类"));
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
  public void addOneRsEvent(@RequestBody String rsEventString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventString, RsEvent.class);
    rsList.add(rsEvent);
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
        replaceRsEvent.setKeyWord(rsEvent.getKeyWord());
      }
    }
    else {
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
