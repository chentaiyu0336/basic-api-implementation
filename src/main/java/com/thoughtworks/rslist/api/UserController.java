package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.domain.CommonError;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static List<User> users = new ArrayList<>();


    @PostMapping("/user")
    public void register(@RequestBody @Valid User user) {
//        if(users.contains(user))
//            return;
        users.add(user);
        UserEntity userEntity = UserEntity.builder()
                .userName(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                //.voteNum(user.getVote())
                .build();
        userRepository.save(userEntity);
    }

    @GetMapping("/user/{index}")
    public User getUser(@PathVariable Integer index) {
        UserEntity userEntity = userRepository.getUserById(index);
        User user = User.builder()
                .name(userEntity.getUserName())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();
        return user;
    }

    @DeleteMapping("/user/{index}")
    public void deleteUser(@PathVariable Integer index) {
        userRepository.deleteById(index);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonError> handleException(MethodArgumentNotValidException ex) {
        CommonError commonError = new CommonError("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }

}
