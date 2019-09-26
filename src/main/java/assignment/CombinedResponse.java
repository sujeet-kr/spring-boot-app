package assignment;

public class CombinedResponse {
    private String randomName;
    private String randomChuckNorrisJoke;
    private String combinedMessage;

    public CombinedResponse(String randomName, String randomChuckNorrisJoke, String combinedMessage){
        this.randomName=randomName;
        this.randomChuckNorrisJoke=randomChuckNorrisJoke;
        this.combinedMessage=combinedMessage;
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
}