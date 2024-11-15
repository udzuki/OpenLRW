package org.apereo.openlrw.event.caliper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apereo.openlrw.OpenLRW;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.apereo.openlrw.caliper.Agent;
import org.apereo.openlrw.caliper.Entity;
import org.apereo.openlrw.caliper.Event;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author ggilbert
 * @author xchopin <xavier.chopin@univ-lorraine.fr>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={OpenLRW.class})
@WebAppConfiguration
public class EventTest {
  
  @Autowired private ObjectMapper mapper;
  
  @Test
  public void whenMinimallyPopulatedJsonContainsEverything() throws JsonProcessingException {
    
    Instant instant = Instant.now();
    
    Agent agent = new Agent.Builder()
      .withId("agent_id1")
      .withType("agent_type1")
      .build();
    
    Entity entity = new Entity.Builder()
      .withId("entity_id1")
      .withType("entity_type1")
      .build();
    
    Event basicEvent = new Event.Builder()
        .withAction("action1")
        .withContext("context1")
        .withType("type1")
        .withEventTime(instant)
        .withAgent(agent)
        .withObject(entity)
        .build();
    
    String result = mapper.writeValueAsString(basicEvent);
    JsonNode jsonNode = mapper.readTree(result);
    assert jsonNode.has("action");
    assert jsonNode.get("action").asText().equals("action1");
    assert jsonNode.has("@context");
    assert jsonNode.get("@context").asText().equals("context1");
    assert jsonNode.has("type");
    assert jsonNode.get("type").asText().equals("type1");
    assert jsonNode.has("eventTime");
    assert jsonNode.has("actor");
    assert jsonNode.get("actor").has("id");
    assert jsonNode.get("actor").get("id").asText().equals("agent_id1");
    assert jsonNode.get("actor").has("type");
    assert jsonNode.get("actor").get("type").asText().equals("agent_type1");
    assert jsonNode.has("object");
    assert jsonNode.get("object").has("id");
    assert jsonNode.get("object").get("id").asText().equals("entity_id1");
    assert jsonNode.get("object").has("type");
    assert jsonNode.get("object").get("type").asText().equals("entity_type1");
  }

}
