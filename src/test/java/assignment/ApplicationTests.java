package assignment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import assignment.CombinedResponse;
import assignment.ConcatRequestsController;
import assignment.RequestSender;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ApplicationTests {

    @InjectMocks
    private ConcatRequestsController concatRequestsController;

    @Mock
    private RequestSender requestSenderMock;

    String serviceURLForName;
    String serviceURLForJoke;
    String nameToReplace;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        serviceURLForName = "http://mocked-random-name-url";
        serviceURLForJoke = "http://mocked-random-joke-url";
        nameToReplace = "John Doe";


        ReflectionTestUtils.setField(concatRequestsController, "serviceURLForName", serviceURLForName);   
        ReflectionTestUtils.setField(concatRequestsController, "serviceURLForJoke", serviceURLForJoke);
        ReflectionTestUtils.setField(concatRequestsController, "nameToReplace", nameToReplace);
    }

    @Test
    public void testRequestCombinationWithAPIOK(){
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", "Sujeet").put("surname", "Kumar").toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", "John Doe is so sorry")).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals("Sujeet Kumar is so sorry",combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequestCombinationWithAPIException() {
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", "Sujeet").put("surname", "Kumar").toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", "No such categories=nonnerdy").toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals("External API call failed",combinedResponse.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequestCombinationWithExternalAPIBadGateway(){
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("error", "BAD GATEWAY").toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", "John Doe is so sorry")).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals("502 BAD_GATEWAY",combinedResponse.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationForSpecialCharactersInRandomName(){
        String name = "#@$%^";
        String surname = "Kumar";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationForNumbersInRandomName(){
        String name = "1247924";
        String surname = "Kumar";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationWhenJohnDoeNotInJokeMessage(){
        String name = "1247924";
        String surname = "Kumar";
        String joke = "Tom and Jerry so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationWhenJokeMessageIsBlank(){
        String name = "1247924";
        String surname = "Kumar";
        String joke = "";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationForBlankInRandomName(){
        String name = "";
        String surname = "Kumar";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationForBlankInRandomNameAndSurname(){
        String name = "";
        String surname = "";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcatenationForNonEnglishInRandomName(){
        String name = "Παναγιώτης";
        String surname = "Γλυκύς";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals(joke.replace("John Doe", name + " " + surname),combinedResponse.getCombinedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testConcatenationForNullInRandomName(){
        String name = null;
        String surname = "Kumar";
        String joke = "Tom and Jerry is John Doe and so sorry";
        try {
            JsonNode resNameBody = new ObjectMapper().readTree(new JSONObject().put("name", name).put("surname", surname).toString());
            JsonNode resJokeBody = new ObjectMapper().readTree(new JSONObject().put("value", new JSONObject().put("joke", joke)).toString());
            
            ResponseEntity<JsonNode> resName = ResponseEntity.status(HttpStatus.OK).body(resNameBody);
            ResponseEntity<JsonNode> resJoke = ResponseEntity.status(HttpStatus.OK).body(resJokeBody);

            when(requestSenderMock.getResponseForExternalRequest(serviceURLForName)).thenReturn(resName);
            when(requestSenderMock.getResponseForExternalRequest(serviceURLForJoke)).thenReturn(resJoke);

            CombinedResponse combinedResponse = concatRequestsController.concatRequests();
            assertEquals("External API call failed",combinedResponse.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}