# HBase microservice project

This is a java microservice application we developed using Spring boot.
It's an API for an HBase database configured for the adaltas cluster. We created the api routes for a chat application we developed last year in react as a proof of concept. But as we don't have access to the Adaltas edge we couldn't use the frontend with the microservice to test it and we couldn't test as the docker containerization either.

## Usage
First clone the repo in the edge.

Generate a keytab for kerberos authentification
```bash
ipa-getkeytab -p [username] -k mykey.keytab -P
```
then, in all of the calls for `HbaseConnector.getConnectionByFile` in all the controllers classes, change the kerberos principal. 

Start the microservice without docker containerization :
```bash
./mvnw spring-boot:run
```

### Start the microservice with docker containerization

First Change the keytab Path in the dockerfile.
then, in all of the calls for `HbaseConnector.getConnectionByFile` in all the controllers classes, change comment the "without docker" section, and uncomment the "with docker" one.

Build th docker image :
```bash
docker build --tag big_data_project .
```
Run the container :
```bash
docker run big_data_project -p 8080 
```

No idea if it works.



## HBase model:

TODO

## API:


## User
### Create : 
```
POST : /user
```

put a json object in the body with a name, an email and a password

```json
    {
        "username": "user4",
        "email": "email",
        "password": "password"
    }
```
 ### Read :
 user with username :username
 ```
GET : /user/:username
 ```
user of id :id
 ```
GET : /users/:id
 ```

### Update :
 ```
PUT : /edituser/:id
 ```
put a json object in the body with properties to change the property you want

user of id :id
```json
    {
        "name": "channel"
    }
```


## Channel
### Create : 
```
POST : /channel
```

put a json object in the body with a name and an owner id

```json
    {
        "owner" : 1
        "name": "channel"
    }
```
 ### Read :
channel with name :name
 ```
GET : /channel/:channelname
 ```
channel of id :id
 ```
GET : /channel/:id
 ```
 channel of id :id owned by user :userID
 ```
GET : /user/:userID/channel/:id
 ```

### Update :
 ```
PUT : /editchannel/:id
 ```
put a json object in the body with name and owner to change the name and owner

```json
    {
        "owner": 1
        "name": "channel"
    }
```
### Add user to channel
```
GET : /channel/:id/adduser/:userID
```
### Remove user from channel
```
GET : /channel/:id/removeuser/:userID
```

### Get user from channel
```
GET : /channel/:id/getusers
```

## Message

### Create : 

a message belongs to a channel.
when you create a message you have to specify the channel you want to create it
```
POST : /message
```

put a json object in the body with a content, the id of an owner (user), the channel id, and a timestamp

```json
    {
        "content" : "content",
        "owner": "userId"
        "created_at": "timestamp"
        "channel" : "channelid"
    }
```
 ### Read :
 all the messages of th channel of id :channelid
 ```
GET : /channel/:channelid/getmessages
 ```
message of id :id
 ```
GET : api/v1/messages/:id
 ```

### Update :
 ```
PUT : /editmessages/:id
 ```
put a json object in the body with content, id of an owner (user), channel id and timestamp

channel of id :id
```json
    {
        "content" : "content",
        "owner": "userId"
        "created_at": "timestamp"
        "channel" : "channelid"
    }
```
