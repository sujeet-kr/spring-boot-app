package assignment;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestSender {

    public ResponseEntity<JsonNode> getResponseForExternalRequest(String serviceURL){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(serviceURL, HttpMethod.GET, entity, JsonNode.class);
    }
}