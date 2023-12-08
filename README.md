The fields attributes, attribute-ids, attribute-values were ignored in the class Relation.
The fields secondary-types, secondary-type-ids were ignored in the class ReleaseGroup.

In order to improve performance implemented caching, connection pool for the application and async processing for the coverart request.
The creation of caches can be verified with http://localhost:8080/actuator/caches

Logging features are not implemented.

I have to describe instructions for deployment in production here.
mvn install...
java -jar artistinfo.jar