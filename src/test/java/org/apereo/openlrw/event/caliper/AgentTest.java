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

import java.time.Instant;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author ggilbert
 * @author xchopin <xavier.chopin@univ-lorraine.fr>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={OpenLRW.class})
@WebAppConfiguration
public class AgentTest {
  @Autowired private ObjectMapper mapper;

  @Test(expected=IllegalStateException.class)
  public void whenMissingDataThrowsException() {
    new Agent.Builder()
        .build();
  }
  
  @Test
  public void whenMinimallyPopulatedJsonContainsEverything() throws JsonProcessingException {
    
    Agent agent
    = new Agent.Builder()
      .withId("id1")
      .withType("type1")
      .build();
    
    String result = mapper.writeValueAsString(agent);
    JsonNode jsonNode = mapper.readTree(result);
    assert jsonNode.has("id");
    assert jsonNode.get("id").asText().equals("id1");
    assert jsonNode.has("type");
    assert jsonNode.get("type").asText().equals("type1");
  }
  
  @Test
  public void whenFullyPopulatedJsonContainsEverything() throws JsonProcessingException {
    
    Agent agent
    = new Agent.Builder()
      .withId("id1")
      .withType("type1")
      .withName("name1")
      .withDescription("description1")
      .withExtensions(Collections.singletonMap("foo", "bar"))
      .withDateCreated(Instant.now())
      .withDateModified(Instant.now())
      .build();
    
    String result = mapper.writeValueAsString(agent);
    JsonNode jsonNode = mapper.readTree(result);
    assert jsonNode.has("id");
    assert jsonNode.get("id").asText().equals("id1");
    assert jsonNode.has("type");
    assert jsonNode.get("type").asText().equals("type1");
    assert jsonNode.has("name");
    assert jsonNode.get("name").asText().equals("name1");
    assert jsonNode.has("description");
    assert jsonNode.get("description").asText().equals("description1");
    assert jsonNode.has("extensions");
    assert jsonNode.get("extensions").has("foo");
    assert jsonNode.get("extensions").get("foo").asText().equals("bar");
    assert jsonNode.has("dateCreated");
    assert jsonNode.has("dateModified");
  }

}
