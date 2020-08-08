package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    private UserEntity userEntity;
    private List<RsEventEntity> rsEventEntityList;

    @BeforeEach
    void beforeEach() {
        //mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
//        User userXiaoMin = new User("XiaoMin",20,"male","xm@163.com","12357439274");
//        UserController.users.add(userXiaoMin);
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
        userEntity = UserEntity.builder()
                .userName("XiaoMin")
                .age(20)
                .gender("male")
                .email("xm@163.com")
                .phone("12357439274")
                .voteNum(10)
                .build();
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


    @Test
    void shouldGetOneRsEventByIndex() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        mockMvc.perform(get("/rs/"+rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName").value(rsEventEntity.getEventName()))
                .andExpect(jsonPath("$.keyword").value(rsEventEntity.getKeyword()))
                .andExpect(jsonPath("$.userId").value(rsEventEntity.getUserId()))
                .andExpect(status().isOk());
    }

//    @Test
//    void shouldGetOneRsEvent() throws Exception {
//        UserEntity userSave = userRepository.save(userEntity);
//        RsEventEntity rsEventEntity = RsEventEntity.builder()
//                .eventName("第一事件")
//                .keyword("无分类")
//                .user(userSave)
//                .build();
//        rsEventRepositoy.save(rsEventEntity);
//        mockMvc
//                .perform(get("/rs/list"))
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyword", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void shouldGetOneRsEvent() throws Exception {
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(jsonPath("$.eventName", is("第一事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(jsonPath("$.eventName", is("第二事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(jsonPath("$.eventName", is("第三事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")))
//                .andExpect(jsonPath("$", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldGetRsEventBetween() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=2"))
//                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list?start=2&end=3"))
//                .andExpect(jsonPath("$[0].eventName", is("第二事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第三事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list?start=1&end=3"))
//                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldAddOneRsEvent() throws Exception {
//        User userDaMin = new User("DaMin",30,"male","dxm@163.com","12357466274");
//        RsEvent rsEvent = new RsEvent("第四事件","无分类", userDaMin);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestJson = objectMapper.writeValueAsString(rsEvent);
//        //String requestJson = "{\"eventName\":\"第四事件\",\"keyWord\":\"无分类\",\"user\":{\"name\":\"DaMin\",\"age\":30,\"gender\":\"male\",\"email\":\"dxm@163.com\",\"phone\":\"12357466274\"}";
//        mockMvc.perform(post("/rs/event").content(requestJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第二事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[3].eventName", is("第四事件")))
//                .andExpect(jsonPath("$[3].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[3]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldChangeOneRsEvent() throws Exception {
//        User userDaMin = new User("DaMin",30,"male","dxm@163.com","12357466274");
//        RsEvent rsEvent = new RsEvent("修改事件",null, userDaMin);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String changeJson = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/2").content(changeJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("修改事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[2].eventName", is("第三事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldDeleteOneRsEvent() throws Exception {
//        mockMvc.perform(delete("/rs/2")).andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
//                .andExpect(jsonPath("$[1].eventName", is("第三事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldNotAddSameUserInUserList() throws Exception {
//        User userXiaoMin = new User("XiaoMin",20,"male","xm@163.com","12357439274");
//        RsEvent rsEvent = new RsEvent("第四事件","无分类", userXiaoMin);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestJson = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event").content(requestJson)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        assertEquals(1, UserController.users.size());
//    }
//
//    @Test
//    void shouldThrowException400WhenOutOfRange() throws Exception {
//        mockMvc.perform(get("/rs/list?start=0&end=10"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.commonError").value("invalid request param"));
//    }
//
//    @Test
//    void shouldThrowException400WhenIndexOutOfRange() throws Exception {
//        mockMvc.perform(get("/rs/10"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.commonError").value("invalid index"));
//    }
//
//    @Test
//    void shouldThrowException400WhenRsEventInvalid() throws Exception {
//        RsEvent rsEvent = new RsEvent("测试事件", null, null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestJson = objectMapper.writeValueAsString(rsEvent);
//        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.commonError").value("invalid param"));
//    }

}
