package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    private UserEntity userEntity;
    private List<RsEventEntity> rsEventEntityList;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        userEntity = addOneUser();
        userEntity = userRepository.save(userEntity);
        rsEventEntityList = Stream.of("1", "2", "3")
                .map(i -> RsEventEntity.builder()
                        .eventName("event name " + i)
                        .keyword("keyword " + i)
                        .userId(userEntity.getId())
                        .votNum(0)
                        .build())
                .collect(Collectors.toList());
        rsEventRepository.saveAll(rsEventEntityList);
    }

//    @Test
//    void shouldRegisterUser() throws Exception {
//        User user = new User("XiaoMin",20,"male","xm@163.com","12357439274");
//        String userJson=objectMapper.writeValueAsString(user);
//        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        assertEquals(1, UserController.users.size());
//    }

    @Test
    void nameShouldLessThan8() throws Exception {
        User user = new User("XiaoMinErro",20,"male","xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldMoreThen18() throws Exception {
        User user = new User("XiaoMin",10,"male","xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldLessThen100() throws Exception {
        User user = new User("XiaoMin",110,"male","xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genderShouldNotBeNull() throws Exception {
        User user = new User("XiaoMin",20,null,"xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emailShouldBeValid() throws Exception {
        User user = new User("XiaoMin",20,"male","xm163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phoneNumShouldEqual11_StartWith1() throws Exception {
        User user = new User("XiaoMin",20,"male","xm@163.com","2235743349274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowException400WhenUserInvalid() throws Exception {
        User user = new User("XiaoMin",20,null,"xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.commonError").value("invalid user"));
    }

    @Test
    void shouldAddUser() throws Exception {
        User user = new User("XiaoMin",20,"male","xm@163.com","12357439274");
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk());
        List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(2,userEntityList.size());
        assertEquals("XiaoMin",userEntityList.get(1).getUserName());
    }

    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/user/" + userEntity.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value(userEntity.getUserName()))
                .andExpect(jsonPath("age").value(userEntity.getAge()))
                .andExpect(jsonPath("gender").value(userEntity.getGender()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        mockMvc.perform(delete("/user/" + userEntity.getId()))
                .andExpect(status().isOk());
        assertEquals(false, userRepository.findById(userEntity.getId()).isPresent());

    }

    @Test
    void shouldDeleteAllEventsWhenDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/"+userEntity.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(0,rsEventRepository.findAll().size());
        assertEquals(0,userRepository.findAll().size());
    }

    private UserEntity addOneUser() {
        UserEntity userEntity = UserEntity.builder()
                .userName("Lin")
                .age(23)
                .gender("female")
                .email("li@163.com")
                .phone("12345678909")
                .voteNum(10)
                .build();
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

}
