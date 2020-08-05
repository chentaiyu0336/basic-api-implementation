package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$",hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventBetween() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第二事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第三事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRsEvent() throws Exception {
        User userDaMin = new User("DaMin",30,"male","dxm@163.com","12357466274");
        RsEvent rsEvent = new RsEvent("第四事件","无分类", userDaMin);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(jsonPath("$[3].eventName", is("第四事件")))
                .andExpect(jsonPath("$[3].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3]", hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldChangeOneRsEvent() throws Exception {
        User userDaMin = new User("DaMin",30,"male","dxm@163.com","12357466274");
        RsEvent rsEvent = new RsEvent("修改事件",null, userDaMin);
        ObjectMapper objectMapper = new ObjectMapper();
        String changeJson = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/2").content(changeJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("修改事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", hasKey("user")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteOneRsEvent() throws Exception {
        mockMvc.perform(delete("/rs/2")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", hasKey("user")))
                .andExpect(jsonPath("$[1].eventName", is("第三事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", hasKey("user")))
                .andExpect(status().isOk());
    }
}
