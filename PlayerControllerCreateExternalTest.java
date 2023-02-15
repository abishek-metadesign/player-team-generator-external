package com.scopic.javachallenge.controllers.external;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PlayerControllerCreateExternalTest extends BasePlayerControllerExternalTest {

    private static TestPrinter testPrinter;


    @BeforeAll
    public  static void initAll(){
        testPrinter = new TestPrinter();
    }


    @Test
    public void shouldReturn2xx() throws Exception {
        testPrinter.print( ()->{

            Map<String, Object> playerMap = getPlayerRequestMap();
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        },12,Position.JUNIOR);
    }

    @Test
    public void shouldReturn201() throws Exception {
        testPrinter.print( ()->{

            Map<String, Object> playerMap = getPlayerRequestMap();
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isCreated());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        },12,Position.JUNIOR);
    }


    @Test
    public void shouldReturnCreatedPlayerInProperFormat() throws Exception {
        testPrinter.print( ()->{

            Map<String, Object> playerMap = getPlayerRequestMap();
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(mvcResult -> {
                    MockHttpServletResponse response = mvcResult.getResponse();
                    String contentAsString = response.getContentAsString();
                    validateResponseAsPerSchema(contentAsString, "/createPlayerResponseSchema.json");
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        },12,Position.JUNIOR);
    }
    @Test
    public void shouldAddPlayerToDatabase() throws Exception {
        testPrinter.print( ()->{
            playerRepository.deleteAll();
            Map<String, Object> playerMap = getPlayerRequestMap();
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            long count = playerRepository.count();
            Assertions.assertEquals(1,count,()->"Player should be added to api");


        },12,Position.JUNIOR);
    }
    @Test
    public void shouldReturn4xxForInvalidPosition() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            playerMap.put("position","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },12,Position.JUNIOR);
    }

    @Test
    public void shouldReturn422forInvalidPosition() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            playerMap.put("position","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },12,Position.JUNIOR);
    }

    @Test
    public void  shouldReturnCorrectErrorWhenPositionIsInvalid() throws Exception {
        testPrinter.print( ()->{

            Map<String, Object> playerMap = getPlayerRequestMap();
            playerMap.put("position","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                                post(PLAYER_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content)
                        ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                        .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("position")));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        },70,Position.JUNIOR);
    }

    @Test
    public void shouldReturn4xxWhenThereIsNoSkill() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).remove("skill");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },12,Position.JUNIOR);
    }

    @Test
    public void shouldReturn422WhenThereIsNoSkill() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).remove("skill");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },12,Position.JUNIOR);
    }
    @Test
    public void shouldReturn4xxWhenThereIsInvalidSkillName() throws Exception {
        testPrinter.print( ()->{

            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).put("skill","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },50,Position.MID);
    }

    @Test
    public void shouldReturn422WhenThereInvalidSkillName() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).put("skill","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },22,Position.JUNIOR);
    }

    @Test
    public void shouldReturnCorrectErrorWhenThereIsInvalidSkillName() throws Exception {
        testPrinter.print( ()->{
            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).put("skill","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                                post(PLAYER_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(content)
                        ).andExpect(MockMvcResultMatchers.status().is4xxClientError())
                        .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("skill")));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        },12,Position.SENIOR);
    }

    @Test
    public void shouldNotSaveThePlayerIfTheSkillIsInvalid() throws Exception {
        testPrinter.print( ()->{
            playerRepository.deleteAll();
            Map<String, Object> playerMap = getPlayerRequestMap();
            List<Map<String,Object>> playerSkills = (List<Map<String,Object>>)playerMap.get("playerSkills");
            playerSkills.get(0).put("skill","defence");
            String content = this.asJsonString(playerMap);
            try {
                mockMvc.perform(
                        post(PLAYER_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            long count = playerRepository.count();
            Assertions.assertEquals(0,count,"Should not save when skill is invalid");
        },12,Position.JUNIOR);
    }


}
