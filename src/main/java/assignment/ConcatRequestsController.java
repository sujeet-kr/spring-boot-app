package assignment;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcatRequestsController{

    @Autowired
    RequestSender requestSender;

    @Value("${RandomNameServiceURL}")
    private String serviceURLForName;

    @Value("${RandomMessageServiceURL}")
    private String serviceURLForJoke;

    @RequestMapping(value="/concat", method=RequestMethod.GET)
    public CombinedResponse concatRequests(){
        ResponseEntity<JsonNode> resName = null;
        ResponseEntity<JsonNode> resJoke = null;
        String firstName = null;
        String surName = null;
        String joke = null;
        String concatenatedMsg = null;
        
        try {
                resName = requestSender.getResponseForExternalRequest(serviceURLForName);
                resJoke = requestSender.getResponseForExternalRequest(serviceURLForJoke);
                if(resName.getStatusCodeValue()==200 && resJoke.getStatusCodeValue() == 200){
                    firstName = resName.getBody().get("name").textValue();
                    surName = resName.getBody().get("surname").textValue();
                    joke = resJoke.getBody().get("value").get("joke").textValue();
                    concatenatedMsg = firstName + " " + surName + " " + joke;
                } else {
                    if(resName.getStatusCodeValue()==200){
                        throw new IllegalStateException(resJoke.getStatusCode().toString());
                    } else {
                        throw new IllegalStateException(resName.getStatusCode().toString());
                    }
                }    
        } catch (Exception error) {
            return new CombinedResponse("External API call failed", error.getMessage());
        }
        return new CombinedResponse(firstName + " " + surName, joke, concatenatedMsg);
    }
}