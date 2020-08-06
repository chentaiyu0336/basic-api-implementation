package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.CommonError;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static List<User> users = new ArrayList<>();


    @PostMapping("/user")
    public void register(@RequestBody @Valid User user) {
        if(users.contains(user))
            return;
        users.add(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonError> handleException(MethodArgumentNotValidException ex) {
        CommonError commonError = new CommonError("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }

}
