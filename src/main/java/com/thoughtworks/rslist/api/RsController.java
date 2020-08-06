package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.CommonError;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws InvalidIndexException{
    if(index<0||index>rsList.size()) {
      throw new InvalidIndexException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
  }

  @GetMapping("/rs/list")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity<List<RsEvent>> getRsEventBetween(@RequestParam(required = false) Integer start,
                                   @RequestParam(required = false) Integer end) throws InvalidIndexException {
    if(start == null || end == null) {
      return ResponseEntity.ok(rsList);
    }
    if(start<0||end>rsList.size()) {
      throw new InvalidIndexException("invalid request param");
    }
    return ResponseEntity.ok(rsList.subList(start-1, end));
  }

  @PostMapping("/rs/event")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    Integer index;
    rsList.add(rsEvent);
    index = rsList.size();
    if (!UserController.users.contains(rsEvent.getUser())) {
      UserController.users.add(rsEvent.getUser());
    }
    return ResponseEntity.created(null).header("index", index.toString()).build();
  }

  @PostMapping("/rs/{index}")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity changeOneRsEvent(@PathVariable int index, @RequestBody String reString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(reString, RsEvent.class);
    RsEvent replaceRsEvent = rsList.get(index-1);
    Integer index_Integer=index;
    if(rsEvent.getEventName()==null) {
      if(rsEvent.getKeyWord()==null) {
        return ResponseEntity.created(null).header("index",index_Integer.toString()).build();
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
        return ResponseEntity.created(null).header("index",index_Integer.toString()).build();
      }
      else {
        replaceRsEvent.setKeyWord(rsEvent.getKeyWord());
      }
    }
    return ResponseEntity.created(null).header("index",index_Integer.toString()).build();
  }

  @DeleteMapping("/rs/{index}")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
    rsList.remove(index-1);
    Integer index_Integer=index;
    return ResponseEntity.ok(null);
  }

  @ExceptionHandler({InvalidIndexException.class, MethodArgumentNotValidException.class})
  public ResponseEntity<CommonError> handleException(Exception ex) {
    String errorMessage;
    if(ex instanceof MethodArgumentNotValidException) {
      errorMessage="invalid param";
    }
    else {
      errorMessage=ex.getMessage();
    }
    CommonError commonError = new CommonError(errorMessage);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
  }

}
