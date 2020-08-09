package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.domain.CommonError;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RsController {

  private final UserRepository userRepository;
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  VoteRepository voteRepository;

  public RsController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

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


  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventBetween(@RequestParam(required = false) Integer start,
                                   @RequestParam(required = false) Integer end) throws InvalidIndexException {
    List<RsEvent> rsEvents = rsEventRepository.findAll().stream()
            .map(it->RsEvent.builder()
            .eventName(it.getEventName())
            .keyword(it.getKeyword())
            .userId(it.getUserId())
            .build())
            .collect(Collectors.toList());
    if(start == null || end == null) {
      return ResponseEntity.ok(rsEvents);
    }
    if(start<0||end>rsEvents.size()) {
      throw new InvalidIndexException("invalid request param");
    }
    return ResponseEntity.ok(rsEvents.subList(start-1, end));
  }

  @PostMapping("/rs/event")
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


//  @Transactional
//  @PostMapping("/rs/vote/{eventId}")
//  public ResponseEntity vote(@PathVariable Integer eventId, @RequestBody @Valid Vote vote) {
//    Integer userId = vote.getUserId();
//    Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(eventId);
//    Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
//
//    if(!optionalRsEventEntity.isPresent()||!optionalUserEntity.isPresent()) {
//      return ResponseEntity.badRequest().build();
//    }
//
//    RsEventEntity rsEventEntity = optionalRsEventEntity.get();
//    UserEntity userEntity = optionalUserEntity.get();
//    if(vote.getVoteNum()>userEntity.getVoteNum()) {
//      return ResponseEntity.badRequest().build();
//    }
//    VoteEntity voteEntity = VoteEntity.builder()
//            .voteNum(vote.getVoteNum())
//            .voteTime(LocalDateTime.parse(vote.getVoteTime()))
//            .rsEventId(rsEventEntity.getId())
//            .userId(userEntity.getId())
//            .build();
//    rsEventEntity.setVotNum(rsEventEntity.getVotNum() + vote.getVoteNum());
//    userEntity.setVoteNum(userEntity.getVoteNum() - vote.getVoteNum());
//    voteRepository.save(voteEntity);
//    rsEventRepository.save(rsEventEntity);
//    userRepository.save(userEntity);
//
//    return ResponseEntity.ok().build();
//  }

  @DeleteMapping("/rs/{id}")
  public ResponseEntity deleteOneRsEvent(@PathVariable Integer id) {
    rsEventRepository.deleteById(id);
    return ResponseEntity.ok(null);
  }

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
