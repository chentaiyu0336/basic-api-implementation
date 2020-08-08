package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.domain.CommonError;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class RsController {
  @Autowired
  UserRepository userRepository;
  @Autowired
  RsEventRepository rsEventRepository;

  @GetMapping("/rs/{id}")
  public ResponseEntity<RsEvent> getOneRsEventById(@PathVariable Integer id) throws InvalidIndexException {
    Optional<RsEventEntity> rsEntityOptional = rsEventRepository.findById(id);
    if (rsEntityOptional.isPresent()) {
      RsEventEntity entity = rsEntityOptional.get();
      RsEvent rsEvent = RsEvent.builder()
              .eventName(entity.getEventName())
              .keyword(entity.getKeyword())
              .userId(entity.getUserId())
              .build();
      return ResponseEntity.ok(rsEvent);
    }
    else {
      return ResponseEntity.ok(null);
    }
  }

//  private List<RsEvent> rsList = new ArrayList<RsEvent>();
//
//  {
//    User userXiaoMin = new User("XiaoMin",20,"male","xm@163.com","12357439274");
//    rsList.add(new RsEvent("第一事件", "无分类", userXiaoMin));
//    rsList.add(new RsEvent("第二事件", "无分类", userXiaoMin));
//    rsList.add(new RsEvent("第三事件", "无分类", userXiaoMin));
//  }
//
//
//  @GetMapping("/rs/{index}")
//  @JsonView(RsEvent.PublicView.class)
//  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws InvalidIndexException{
//    if(index<0||index>rsList.size()) {
//      throw new InvalidIndexException("invalid index");
//    }
//    return ResponseEntity.ok(rsList.get(index-1));
//  }
//
//  @GetMapping("/rs/list")
//  @JsonView(RsEvent.PublicView.class)
//  public ResponseEntity<List<RsEvent>> getRsEventBetween(@RequestParam(required = false) Integer start,
//                                   @RequestParam(required = false) Integer end) throws InvalidIndexException {
//    if(start == null || end == null) {
//      return ResponseEntity.ok(rsList);
//    }
//    if(start<0||end>rsList.size()) {
//      throw new InvalidIndexException("invalid request param");
//    }
//    return ResponseEntity.ok(rsList.subList(start-1, end));
//  }
//
  @PostMapping("/rs/event")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
    if (!hasRegistered(rsEvent.getUserId())) {
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity eventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .userId(rsEvent.getUserId())
            .votNum(0)
            .build();
    rsEventRepository.save(eventEntity);
    return ResponseEntity.created(null).build();
  }


  @PatchMapping("/rs/{index}")
  @JsonView(RsEvent.PublicView.class)
  public ResponseEntity updateOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(index);
    if(optionalRsEventEntity.isPresent() &&optionalRsEventEntity.get().getUserId().equals(rsEvent.getUserId())) {
      RsEventEntity rsEventEntity = optionalRsEventEntity.get();
      if(rsEvent.getEventName()!=null) {
        rsEventEntity.setEventName(rsEvent.getEventName());
      }
      if(rsEvent.getKeyword()!=null) {
        rsEventEntity.setKeyword(rsEvent.getKeyword());
      }
      rsEventRepository.save(rsEventEntity);
      return ResponseEntity.ok().build();
    }
    else
      return ResponseEntity.badRequest().build();
  }

//  @DeleteMapping("/rs/{index}")
//  @JsonView(RsEvent.PublicView.class)
//  public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
//    rsList.remove(index-1);
//    Integer index_Integer=index;
//    return ResponseEntity.ok(null);
//  }

  public boolean hasRegistered(Integer id) {
    return userRepository.findById(id).isPresent();
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
