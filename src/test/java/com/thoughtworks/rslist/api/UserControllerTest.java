package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        UserController.users.clear();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User user = new User("XiaoMin",20,"male","xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1, UserController.users.size());
    }

    @Test
    void nameShouldLessThan8() throws Exception {
        User user = new User("XiaoMinErro",20,"male","xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldMoreThen18() throws Exception {
        User user = new User("XiaoMin",10,"male","xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldLessThen100() throws Exception {
        User user = new User("XiaoMin",110,"male","xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void genderShouldNotBeNull() throws Exception {
        User user = new User("XiaoMin",20,null,"xm@163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emailShouldBeValid() throws Exception {
        User user = new User("XiaoMin",20,"male","xm163.com","12357439274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phoneNumShouldEqual11_StartWith1() throws Exception {
        User user = new User("XiaoMin",20,"male","xm@163.com","2235743349274");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson=objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
