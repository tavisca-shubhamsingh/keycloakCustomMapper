
Steps:

1. build the project to get the jars for CustomProtocolMapper and the service.
2. run docker-compose up --build
  this creates three containers keycloak standalone, postgres, and our dummy service from where we get data and populate the jwt token.

3. run the main file in data-setup module
  this create the realm, users and adds our mapped to keycloak as an extension (a jar file)
  
4. now that realm and user is created, and custom mapper is in place, we can send request to get access token.

curl -d 'client_id=example-realm-client' -d 'username=jdoe' -d 'password=password' -d 'grant_type=password' 'http://localhost:11080/auth/realms/example-realm/protocol/openid-connect/token'

the idea is 
In the request to get token, we add a request header named userId which contains the userId with which we are populating the token.

e.g a user with id 1 and name "user" is added to dummy service, if a request comes to keycloak server for token, with header userId = 1,
the user object is added to the token.

# keycloakCustomMapper
This contains:
* data-setup => which uses keycloak api end points to create realm, users etc.
* custom-mapper => which is a custom mapper which helps in adding our own claims to the jwt token.
* service => a dummy user service which can save and retrieve data to user.

