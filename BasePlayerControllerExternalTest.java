package com.scopic.javachallenge.controllers.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.scopic.javachallenge.enums.PlayerPosition;
import com.scopic.javachallenge.enums.Skill;
import com.scopic.javachallenge.models.Player;
import com.scopic.javachallenge.models.PlayerSkill;
import com.scopic.javachallenge.repositories.PlayerRepository;
import de.cronn.testutils.h2.H2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
@Import(H2Util.class)
public class BasePlayerControllerExternalTest {

    final static String PLAYER_URL = "/player/";
    final static String TEAM_URL = "/team/process";
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PlayerRepository playerRepository;

    protected Player getPlayer() {
        Player player = new Player();
        player.setName("player");
        player.setPosition(PlayerPosition.FORWARD);
        List<PlayerSkill> playerSkills = new ArrayList<>();
        PlayerSkill playerSkill = new PlayerSkill();
        playerSkill.setSkill(Skill.ATTACK);
        playerSkill.setValue(80);
        playerSkill.setPlayer(player);
        playerSkills.add(playerSkill);
        player.setPlayerSkills(playerSkills);
        return player;
    }

    protected String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected void validateResponseAsPerSchema(String json, String schemaUrl) throws IOException, ProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJsonNode = objectMapper.readTree(json);
        JsonNode schemaJsonNode = JsonLoader.fromResource(schemaUrl);
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.byDefault();
        JsonSchema jsonSchema = jsonSchemaFactory.getJsonSchema(schemaJsonNode);
        ProcessingReport validate = jsonSchema.validate(responseJsonNode);
        if (!validate.isSuccess()){
            throw  new RuntimeException();
        }
    }


    protected static Map<String, Object> getPlayerRequestMap() {
        // setting player details
        Map<String,Object> playerMap = new HashMap<>();
        playerMap.put("name","player name 2");
        playerMap.put("position","midfielder");

        // adding player skills
        List<Map<String,Object>> playerSkills = new ArrayList<>();

        // adding player value
        Map<String,Object> attackSkill = new HashMap<>();
        attackSkill.put("skill","attack");
        attackSkill.put("value",60);

        // adding player value
        Map<String,Object> speedSkill = new HashMap<>();
        speedSkill.put("skill","speed");
        speedSkill.put("value",80);

        playerSkills.add(attackSkill);
        playerSkills.add(speedSkill);

        playerMap.put("playerSkills",playerSkills);
        return playerMap;
    }

    public void clearDatabase(){
        playerRepository.deleteAll();
    }


}
