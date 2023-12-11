
## Description:
The application fetches artist information for a given MusicBrainz Identifier, i.e. mbid.

## Prerequisites:
- Java 
- Maven 

## Use maven to build a jar file:
mvn clean install

## Run the application:
java -jar artistinfo-0.0.1-SNAPSHOT.jar

It will be accessible on the configured IP address and port as specified in the application.properties file.

## Url to access the api on localhost:
http://localhost:8080/api/artistinfo/{mbid} <br/>

Example:<br/>
http://localhost:8080/api/artistinfo/5b11f4ce-a62d-471e-81fc-a69a8278c7da

## Observations:
- Implemented caching, connection pool and async to improve performance.
- Logging was not implemented. The exceptions should be logged if logging is implemented. CoverArtNotFoundException was not used, but it could be used to log exceptions in the CoverArtService.
- Not all errors are handled properly, for example if the underlying APIs are unavailable.
- Tests were not implemented.