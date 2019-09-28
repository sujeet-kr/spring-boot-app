## CONTAINERIZED SPRING BOOT APPLICATION WITH REST ENDPOINT

**Description:**

The API endpoint /concat provided by the application, calls External APIs for Random Name and Random Jokes. It returns a response with the Actual Random Name, Random Joke and the Converted Joke fields. The Converted Joke has 'John Doe' in the returned Random Joke replaced by the Random Name returned by the Random Name API.
Unit Test details are provided below.

**Requirements:**
- Java installed 1.8+
- Gradle installed 4.10+
- Docker

**To build docker image and Start the Webservice application along with Running Unit Tests:**
- clone the project  `git clone https://github.com/sujeet-kr/spring-boot-app.git`
- cd to the root of the project
- Execute the executable shell file -  `./run.sh`

**To run an already built image from DockerHub**
- `docker pull sujeetk24/spring-boot`
- `docker run -p 5000:8080 --rm sujeetk24/spring-boot`

**What Does ./run.sh do?**
- Cleans the build directory
- Executes the gradle tasks -  build and docker
- Gradle task 'build' - Builds the Springboot project
- Gradle task 'docker' - Creates the docker image from the build artifact
- Creates and starts the docker container with host port 5000 -> container port 8080

**Framework Used:**
- Spring Boot
- Junit
- Mokito

**Endpoint Available:**
- /concat
- Port mapping with docker 5000:8080

**Unit Tests are located at src/test/java/assignment**
- Tests uses Mokito to mock the dependencies
- Annotated as SpringBootTests
- The @Before hook sets up the mock dependencies
- The @Test functions are the unit tests
- testRequestCombinationWithAPIOK : Tests if the Name and Joke returned by the apis are properly combined
- testRequestCombinationWithAPIException : Test to check if one of the Random apis are down, the Concatenation API gracefully handles the condition
- testRequestCombinationWithExternalAPIBadGateway : Test to check if one of the Random apis have a BAD GATEWAY error, the Concatenation API gracefully handles the condition
- testConcatenationForSpecialCharactersInRandomName : Test to check if Concatenation API can handle the condition when, the Name returned by the Random api is only non alphanumeric characters. 
- testConcatenationForNumbersInRandomName : Test to check Concatenation API functionality, with the Name as numeric
- testConcatenationWhenJohnDoeNotInJokeMessage : Test to check when the Random joke does not have John Doe
- testConcatenationWhenJokeMessageIsBlank : Test to check when Random Joke is blank
- testConcatenationForBlankInRandomName : Test to check when Random first name is blank
- testConcatenationForBlankInRandomNameAndSurname : Test to check when Random first name and last name are both blank
- testConcatenationForNonEnglishInRandomName : Test to check when the Random first name and last name are non english
- testConcatenationForNullInRandomName : Test to check when Random first name is null
