package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
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


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
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
    @Autowired
    VoteRepository voteRepository;

    private UserEntity userEntity;
    private List<RsEventEntity> rsEventEntityList;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        voteRepository.deleteAll();
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

//    void afterEach() {
//        rsEventEntityList.clear();
//    }

    @Test
    void shouldGetOneRsEventByIndex() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        mockMvc.perform(get("/rs/" + rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName").value(rsEventEntity.getEventName()))
                .andExpect(jsonPath("$.keyword").value(rsEventEntity.getKeyword()))
                .andExpect(jsonPath("$.userId").value(rsEventEntity.getUserId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddOneRsEventWhenUserIsRegistered() throws Exception {
        RsEvent rsEvent = new RsEvent("event name 4", "keyword 4", userEntity.getId());
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated());
        rsEventEntityList = rsEventRepository.findAll();
        RsEventEntity rsEventEntity = rsEventEntityList.get(rsEventEntityList.size() - 1);
        assertEquals(4, rsEventEntityList.size());
        assertEquals(rsEvent.getEventName(), rsEventEntity.getEventName());
        assertEquals(rsEvent.getUserId(), rsEventEntity.getUserId());
        assertEquals(rsEvent.getKeyword(), rsEventEntity.getKeyword());
    }

    @Test
    void shouldThrowException400WhenUnRegisteredUserAddRsEvent() throws Exception {
        RsEvent rsEvent = new RsEvent("event name 4", "keyword 4", 100);
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldGetRsEventBetween() throws Exception {
        RsEventEntity rsStart = rsEventEntityList.get(0);
        RsEventEntity rsEnd = rsEventEntityList.get(1);

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName").value(rsStart.getEventName()))
                .andExpect(jsonPath("$[0].keyword").value(rsStart.getKeyword()))
                .andExpect(jsonPath("$[0].userId").value(rsStart.getUserId()))
                .andExpect(jsonPath("$[1].eventName").value(rsEnd.getEventName()))
                .andExpect(jsonPath("$[1].keyword").value(rsEnd.getKeyword()))
                .andExpect(jsonPath("$[1].userId").value(rsEnd.getUserId()))
                .andExpect(status().isOk());
    }


    @Test
    void shouldUpdateRsEventNameAndKeyWordWhenBothValid() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        RsEvent rsEvent = new RsEvent("update name", "update keyword", rsEventEntity.getUserId());
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/" + rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName").value(rsEvent.getEventName()))
                .andExpect(jsonPath("$.keyword").value(rsEvent.getKeyword()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateRsEventNameOnlyWhenKeyWordIsNull() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        RsEvent rsEvent = new RsEvent("update name", null, rsEventEntity.getUserId());
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/" + rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName").value(rsEvent.getEventName()))
                .andExpect(jsonPath("$.keyword").value(rsEventEntity.getKeyword()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateKeyWorldOnlyWhenEventNameIsNull() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        RsEvent rsEvent = new RsEvent(null, "update keyword", rsEventEntity.getUserId());
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/" + rsEventEntity.getId()))
                .andExpect(jsonPath("$.eventName").value(rsEventEntity.getEventName()))
                .andExpect(jsonPath("$.keyword").value(rsEvent.getKeyword()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrow400WhenUserIdIsNotCorrect() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        RsEvent rsEvent = new RsEvent("update name", "update keyword", 1000);
        String requestJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(patch("/rs/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteOneRsEvent() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        mockMvc.perform(delete("/rs/" + rsEventEntity.getId()))
                .andExpect(status().isOk());
        assertEquals(2, rsEventRepository.findAll().size());
        assertEquals(false, rsEventRepository.findById(rsEventEntity.getId()).isPresent());
    }

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
    @Test
    void shouldThrowException400WhenOutOfRange() throws Exception {
        mockMvc.perform(get("/rs/list?start=0&end=10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.commonError").value("invalid request param"));
    }
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

    @Test
    void shouldVoteSuccess() throws Exception {
        String voteTime = LocalDateTime.now().toString();
        Vote vote = new Vote(6, userEntity.getId(), voteTime);
        String requestJson = objectMapper.writeValueAsString(vote);

        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        Integer rsEventVotNumBefore = rsEventEntity.getVotNum();
        Integer userVoteNumBefore = userEntity.getVoteNum();

        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk());
        VoteEntity voteEntity = voteRepository.findAll().get(0);
        assertEquals(6, voteEntity.getVoteNum());
        assertEquals(rsEventEntity.getId(), voteEntity.getRsEvent().getId());
        assertEquals(userEntity.getId(), voteEntity.getUser().getId());

        Integer rsEventVotNumAfter = rsEventRepository.findById(rsEventEntity.getId()).get().getVotNum();
        Integer userVoteNumAfter = userRepository.findById(userEntity.getId()).get().getVoteNum();
        assertEquals(userVoteNumBefore - 6, userVoteNumAfter);
        assertEquals(rsEventVotNumBefore + 6, rsEventVotNumAfter);
    }

    @Test
    void shouldVoteFailedWhenUserVoteNumIsNotEnough() throws Exception {
        String voteTime = LocalDateTime.now().toString();
        Vote vote = new Vote(100, userEntity.getId(), voteTime);
        String requestJson = objectMapper.writeValueAsString(vote);

        RsEventEntity rsEventEntity = rsEventEntityList.get(0);
        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId()).contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetVoteRecordsBetweenTimeRange() throws Exception {
        RsEventEntity rsEventEntity = rsEventEntityList.get(1);
        VoteEntity voteEntity1 = VoteEntity.builder()
                .rsEventId(rsEventEntity.getId())
                .userId(rsEventEntity.getUserId())
                .voteNum(5)
                .voteTime(LocalDateTime.of(2020, 8, 8, 8, 8))
                .build();
        voteEntity1 = voteRepository.save(voteEntity1);

        VoteEntity voteEntity2 = VoteEntity.builder()
                .rsEventId(rsEventEntity.getId())
                .userId(rsEventEntity.getUserId())
                .voteNum(3)
                .voteTime(LocalDateTime.of(2020, 8, 8, 8, 10))
                .build();
        voteEntity2 = voteRepository.save(voteEntity2);

        VoteEntity voteEntity3 = VoteEntity.builder()
                .rsEventId(rsEventEntity.getId())
                .userId(rsEventEntity.getUserId())
                .voteNum(1)
                .voteTime(LocalDateTime.of(2020, 8, 8, 9, 0))
                .build();
        voteEntity3 = voteRepository.save(voteEntity3);

        String startTime=voteEntity1.getVoteTime().toString();
        String endTime=voteEntity3.getVoteTime().toString();
        mockMvc.perform(get("/rs/vote?startTime="+startTime+"&endTime="+endTime))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].voteNum").value(voteEntity1.getVoteNum()))
                .andExpect((jsonPath("$[0].userId").value(voteEntity1.getUserId())))
                .andExpect(jsonPath("$[1].voteNum").value(voteEntity2.getVoteNum()))
                .andExpect((jsonPath("$[1].userId").value(voteEntity2.getUserId())))
                .andExpect(jsonPath("$[2].voteNum").value(voteEntity3.getVoteNum()))
                .andExpect((jsonPath("$[2].userId").value(voteEntity3.getUserId())))
                .andExpect(status().isOk());
    }

}
