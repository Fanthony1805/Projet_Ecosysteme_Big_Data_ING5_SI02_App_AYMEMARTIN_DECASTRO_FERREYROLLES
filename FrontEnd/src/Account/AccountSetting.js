import React from 'react';
import './AccountSetting.css';
import axios from 'axios';

var sha256 = require('js-sha256');


const AccountSetting = (props) => {
    var username = props.user;
    var userID = '';
    var userPassword = '';
    var userEmail = '';
    var finished = false;

    const changeSettings = async () => { 
        //recupere l'id de l'utilisateur 
        await axios.get('http://localhost:8080/user').then(response => {
            const users = response.data;

            users.map(user => {
                if(user.name === username){
                    userID = user.id;
                    userPassword = user.password;
                    userEmail = user.email;
                    finished = true;
                }
                return user;
            });
        });
        setTimeout(applyChanges(),1000);
    }
   
    const Reset = () => {
        //Reinitialiser les champs
        document.getElementById("usernameSignIn").value = "";
        document.getElementById("passwordSignIn").value = "";
        document.getElementById("emailSignIn").value = "";
    }

    const applyChanges = async () => {
        if(finished === true){
            finished = false; //Dire qu'une action est en cours

            if(document.getElementById("usernameSignIn").value !== ''){
                username = document.getElementById("usernameSignIn").value;
                ChangeUsername(username);
            }
            if(document.getElementById("passwordSignIn").value !== ''){
                userPassword = sha256(document.getElementById("passwordSignIn").value); //Hash du mdp
            }
            if(document.getElementById("emailSignIn").value !== ''){
                userEmail = document.getElementById("emailSignIn").value;
            }

            await axios.get('http://localhost:8080/user/'+userID).then(async res => {
                const usr = res.data;

                const user = {
                    name: username,
                    password: userPassword,
                    email: userEmail,
                    token: props.token,
                    nightMode: usr.nightMode,
                }

                //Updater les informations de l'utilisateur
                await axios.put('http://localhost:8000/user/'+userID,user);

                props.setUser(user); //Réassocier l'utilisateur
            });

            //Reinitialiser les champs
            document.getElementById("usernameSignIn").value = "";
            document.getElementById("passwordSignIn").value = "";
            document.getElementById("emailSignIn").value = "";
        }
    }

    const ChangeUsername = async (newUsername) => {
        //Comme le username est présent dans toutes les bdd, nous devons updater toutes les bdd

        await axios.get('http://localhost:8000/channel').then(response => {
            const channels = response.data;

            //Récupérer tous les channels
            channels.map(async channel => {
                const updatedOwners = [];
                const owners = channel.owners;
                var channelModified = false;

                try{
                    //Récupérer la liste des owners tout en remplaçant l'ancien username par le nouveau
                    owners.map(owner => {
                        if(owner === props.user){
                            updatedOwners.push(newUsername);
                            channelModified = true;
                        } else {
                            try {
                                var alreadyAdded = false;
                                updatedOwners.map(user => {
                                    if(owner === user){
                                        alreadyAdded = true;
                                    }
                                    return user;
                                });
                                if(!alreadyAdded){
                                    updatedOwners.push(owner);
                                }
                            } catch {
                                updatedOwners.push(owner);
                            }
                        }

                        return owner;
                    }).then(async res => {
                        if(channelModified){
                            //Updater le channel
                            await axios.put('http://localhost:8080/channel/'+channel.id,{
                                name: channel.name,
                                owners: updatedOwners,
                            });
                        }
                    });
                } catch {
                    //Updater le channel
                    await axios.put('http://localhost:8080/channel/'+channel.id,{
                        name: channel.name,
                        owners: updatedOwners,
                    });
                }

                return channel;
            });
        });

        //De même pour les messages
        await axios.get('http://localhost:8080/message').then(response => {
            const messages = response.data;

            try{
                messages.map(async message => {
                    if(message.author === props.user){
                        await axios.put('http://localhost:8080/message/'+message.id,{
                            author: newUsername,
                            content: message.content,
                            created_at: message.created_at,
                        });
                    }
                    return message;
                });
            } catch {
                
            }
        });

        //De même pour les avatars
        axios.get('http://localhost:8080/api/v1/avatar').then(res => {
            const avatars = res.data;

            try{
                avatars.map(avatar => {
                    try{
                        if(avatar.user === props.user){
                            axios.put('http://localhost:8080/api/v1/avatar/'+avatar.id,{
                                image: avatar.image,
                                user : newUsername,
                                source: avatar.source
                            })
                        }
                    } catch {

                    }
                    return avatar;
                });
            } catch {
                
            }
        });
    }
    
    return (
        <div className="AccountSettings">
            <h2 className="accountText">Account Settings</h2><br/><br/>
            <label>New Username :</label><br/>
            <input type="text" id="usernameSignIn"/><br/><br/>
            <label>New Email @:</label> <br/>
            <input type="email" id="emailSignIn"/>
            <br/><br/>
            <div className="block">
                <div className="left">
                    <label>New Password :</label><br/>
                    <input type="password" id="passwordSignIn"/>
                </div>
            </div><br/><br/>
            <button type="submit" onClick={Reset}>Reset</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="submit" onClick={changeSettings}>Change</button> 
        </div>
    );
}
export default AccountSetting;