# HBase microservice project

This is a java microservice application we developed using Spring boot.
It's an API for an HBase database configured for the adaltas cluster. We created the api routes for a chat application we developed last year in react as a proof of concept. But as we don't have access to the Adaltas edge we couldn't use the frontend with the microservice to test it and we couldn't test as the docker containerization either.

## Usage

First you can clone the repo in the edge, or you can just put the built jar in the edge

To build the jar
```bash
./mvnw package
```
the jar is in the target folder

Generate a keytab for kerberos authentification
```bash
ipa-getkeytab -p [username] -k mykey.keytab -P
```
then, in all of the calls for `HbaseConnector.getConnectionByFile` in all the controllers classes, change the kerberos principal. 

Go to the big_data_project folder

### Start the microservice without docker containerization :

To start from the cloned repo
```bash
./mvnw spring-boot:run
```
to start from a jar
```bash
java -jar [jarfile]
```

### Start the microservice with docker containerization :

First Change the keytab Path in the dockerfile.
then, in all of the calls for `HbaseConnector.getConnectionByFile` in all the controllers classes, comment the "without docker" section, and uncomment the "with docker" one.

Build th docker image :
```bash
docker build --tag big_data_project .
```
Run the container :
```bash
docker run big_data_project -p 8080 
```

No idea if it works.

### Start the React Front End

go to the FrontEnd folder
```bash
npm install 
```
```bash
npm start
```
No idea if it works


## HBase model:

In order to get all the informations for the users, messages and channels, we created several rowkeys to get all the informations.

Lets take an exemple. We can picture that we want to create a user which has the id 'u1'. We also want to create a channel for this user which id will be 'c1'. Finally, this channel will have a message written by this user and its id will be 'm1'.

In HBase we'll have the following rowkeys:
'u1', 'c1', 'm1', 'u1-name' or/and 'u1-email', 'u1_c1', 'u1_m1', 'c1_u1', 'u1_c1_m1', 'counter'.

Let's take some exemples:
* By creating the rowkey 'u1', we can retrieve all the informations for the user 'u1' such as its name, its email or even its password if needed. Same for 'c1' and 'm1'.
* By creating the rowkey 'u1-name' or 'u1-email', we can easily do a user authentication by returning the password or its hash and compare it to the login credentials.
* By creating the rowkey 'c1_u1', we can determine the membership of a user to a specific channel.
* By creating the rowkey 'counter', we are able to create as many users, channels and messages by incrementing the numbers of each column's ('nb_user', 'nb_channel', 'nb_message') values

We decided to create 3 column families:
'user', 'channel', 'message'.

Each user has a user ID, a name, an email and a password.
Each channel has a channel ID, a name and an owner.
Each message has a message ID, a channel ID, a user ID (author), a content and a date of creation (created_at).
All these criterias are all shown in different columns.


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
GET : /user/:id
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
