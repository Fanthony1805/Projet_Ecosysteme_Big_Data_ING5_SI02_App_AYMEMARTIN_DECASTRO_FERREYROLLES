import React, {useCallback} from 'react';
import './Login.css';
import axios from 'axios';
var sha256 = require('js-sha256');

const Login = (props) => {
    var loggedIn = false;

    const Reset = () => {
        //Reinitialiser les champs
        document.getElementById("usernameText").value = "";
        document.getElementById("passwordText").value = "";
    }

    const Submit = async () => {
        //Recuperation des données
        const user = {
            name: document.getElementById("usernameText").value,
            password: sha256(document.getElementById("passwordText").value), //Comparer les hashs des mdp
        }
        await axios.get('http://localhost:8080/users').then(response => { 
            const users = response.data;
            users.map(usr => {
                if (usr.name === user.name){
                    if (usr.password === user.password){
                        //Recuperation des données manquantes
                        const user = {
                            name: usr.name,
                            email: usr.email,
                            password: usr.password,
                            nightMode: usr.nightMode
                        }
                        //Recuperation du token
                        axios.post('http://localhost:8080/login',user).then(response => {
                            var accessToken = response.data;
                            const user = {
                                token: accessToken.access_token,
                                name: usr.name,
                                email: usr.email,
                                password: usr.password,
                                nightMode: usr.nightMode,
                            }
                            updateUser(user) //Associer l'utilisateur avec l'ensemble de ces données à la variable utilisateur
                        })
                        loggedIn = true;
                    }
                }
                return usr;
            });
        });
        LoggedIn();
    }

    const updateUser = useCallback(
		user => props.setUser(user), //Updater l'utilisateur
		[props],
	);

    const LoggedIn = () => {
        if (!loggedIn){
            window.alert("wrong username or password"); //Popup d'alerte
        }
    }

	return (
        <div className="Login">
            <h2>Login :</h2>
            <label>Username :</label><br/>
            <input type="text" id="usernameText"></input><br/><br/>
            <label>Password :</label><br/> 
            <input type="password" id="passwordText"></input>
            <br/><br/>
            <button type="submit" onClick={Reset}>Reset</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="submit" onClick={() => Submit()}>Submit</button> 
        </div>
    )
};

export default Login;