package assignment;

public class CombinedResponse {
    private String randomName;
    private String randomChuckNorrisJoke;
    private String combinedMessage;
    private String errorMessage;
    private String errorCode;

    public CombinedResponse(String randomName, String randomChuckNorrisJoke, String combinedMessage){
        this.randomName=randomName;
        this.randomChuckNorrisJoke=randomChuckNorrisJoke;
        this.combinedMessage=combinedMessage;
    }

    public CombinedResponse(String errorMessage, String errorCode){
        this.errorMessage=errorMessage;
        this.errorCode=errorCode;
    }

    public String getRandomName(){
        return randomName;
    }

    public String getRandomChuckNorrisJoke(){
        return randomChuckNorrisJoke;
    }

    public String getCombinedMessage(){
        return combinedMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public String getErrorCode(){
        return errorCode;
    }
}