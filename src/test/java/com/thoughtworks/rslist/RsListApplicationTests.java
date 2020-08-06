package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

//    @Test
//    void contextLoads() {
//    }

    @Test
    void shouldAddUser() throws Exception {
        User user = new User("XiaoMin",20,"male","xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk());
        List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(1,userEntityList.size());
        assertEquals("XiaoMin",userEntityList.get(0).getUserName());
    }

    @Test
    void shouldGetUser() throws Exception {
        UserEntity userEntity = addOneUser();
        mockMvc.perform(get("/user/" + userEntity.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value(userEntity.getUserName()))
                .andExpect(jsonPath("age").value(userEntity.getAge()))
                .andExpect(jsonPath("gender").value(userEntity.getGender()))
                .andExpect(jsonPath("email").value(userEntity.getEmail()))
                .andExpect(jsonPath("phone").value(userEntity.getPhone()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UserEntity userEntity = addOneUser();
        mockMvc.perform(delete("/user/" + userEntity.getId()))
                .andExpect(status().isOk());
        assertEquals(false, userRepository.findById(userEntity.getId()).isPresent());

    }

    private UserEntity addOneUser() {
        UserEntity userEntity = UserEntity.builder()
                .userName("Lin")
                .age(23)
                .gender("female")
                .email("li@163.com")
                .phone("12345678909")
                .build();
        userEntity = userRepository.save(userEntity);
        return userEntity;
    }

}
