import React from 'react';
import './SignIn.css';
import axios from 'axios';
var sha256 = require('js-sha256');


const SignIn = () => {
    var valid = false;

    const Reset = () => {
      document.getElementById("usernameSignIn").value = "";
      document.getElementById("passwordSignIn").value = "";
      document.getElementById("confirmPasswordSignIn").value = "";
      document.getElementById("emailSignIn").value = "";
    }

    function verif_email(){
        //Blindage de l'email
        const email = document.getElementById("emailSignIn").value;
        var pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+.[a-z]/
    
        if (pattern.test(email))
        {
            valid = true;
        }
        else
        {
            window.alert('The Email is invalid !');
        }
    }

    const validPassword = () => {
        //Confirmation du mdp
        const password = document.getElementById("passwordSignIn").value;
        const confirmPassword = document.getElementById("confirmPasswordSignIn").value;
        
        if (password !== confirmPassword){
            valid = false;
            window.alert("password doesn't match");
        }
        else {
            valid = true;
        }
    }

    const Submit = async () => {
        //Vérification des paramètres
        validPassword();
        verif_email();
        var sameName = false;
        var sameEmail = false;
        //Enregistrer le hash du mdp
        var password = sha256(document.getElementById("passwordSignIn").value)

        //Si les paramètres sont valides
        if(valid){
            //Récupération des données
            const user = {
                name: document.getElementById("usernameSignIn").value,
                password: password,
                email: document.getElementById("emailSignIn").value,
                nightMode: false,
            }

            //Vérification dans la database si le nom d'utilisateur ou l'email sont déjà pris
            await axios.get('http://localhost:8080/users').then(response => {
                const users = response.data;
                users.map(user => {
                    if(user.name === document.getElementById("usernameSignIn").value){
                        sameName = true;
                    }
                    if(user.email === document.getElementById("emailSignIn").value){
                        sameEmail = true;
                    }
                    return user;
                })
            });
            
            if(sameName === true){
                window.alert('This username already exists.')
            } else {
                if(sameEmail === true){
                    window.alert('This email already exists.')
                } else {
                    //Si l'email et le user name sont uniques, créer l'utilisateur
                    await axios.post('http://localhost:8080/users',user);
                    //Réinitialisation des champs
                    Reset();

                    window.alert('User created.');
                }
            }
        }
    }

    return (
        <div className="SignIn">
            <h2 className="accountText">Create an account</h2>
            <label>Username :</label><br/>
            <input type="text" id="usernameSignIn"></input><br/><br/>
            <label>Email @:</label> <br/>
            <input type="email" id="emailSignIn"></input>
            <br/><br/>
            <div className="block">
                <div className="left">
                    <label>Password :</label><br/>
                    <input type="password" id="passwordSignIn"></input>
                </div>
                <div>
                    <label>Confirm Password:</label> <br/>
                    <span><input type="password" id="confirmPasswordSignIn"></input></span>
                </div>
            </div><br/><br/>
            <div className="block">
                <div className="left button">
                    <button type="submit" onClick={Reset}>Reset</button>
                </div>
                <div>
                    <button type="submit" onClick={Submit}>Submit</button> 
                </div>
            </div>
       </div>
    )
};

export default SignIn;