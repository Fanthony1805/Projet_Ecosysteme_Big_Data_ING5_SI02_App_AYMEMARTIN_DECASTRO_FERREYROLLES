import React, { useState, useEffect } from 'react';
import './App.css';

import Header from '../layout/Header';
import Main from '../layout/Main';
import Footer from '../layout/Footer';

import axios from 'axios';

const App = () => {
    const [user, setUser] = useState({});
    const [redirect, setRedirect] = useState("Home");
    const [channels, setChannels] = useState([]); //Initialiser à vide avant de pouvoir les récuperer

    useEffect(() => {
        if(user.token !== undefined){
            //Si nous avons un token
            try{
                axios.defaults.headers.common = {'Authorization': `bearer ${user.token}`}
                
                axios.get('http://localhost:8080/channels').then(response => {
                    const channels = response.data;
                    const userChannels = [];

                    channels.map(channel => {
                        channel.owners.map(owner => {
                            if(owner === user.name){
                                userChannels.push(channel);
                            }
                            return owner;
                        })
                        return channel;
                    });

                    //Affecte les channels de l'utilisateur à la variable channels
                    setChannels(userChannels);
                })

                //Afficher le nom de l'utilisateur
                try{
                    document.getElementById("loggedInLabel").classList.remove('hidden');
                    document.getElementById("LogOut").classList.remove('hidden');
                } catch {

                }
                document.getElementById("loggedInLabel").style.fontWeight = 'bold';
                document.getElementById("loggedInLabel").innerText = "Logged in as : " + user.name;
            } catch {
                console.log("error");
            }
        }
    },[user]);

    const redirectTo = (direction) => {
        //Redirection avec un timeout pour permettre aux requêtes faites à la database de bien s'executer
        setTimeout(setRedirect(direction),1000);
    }

    const LogOut = () => {
        //Reinitialiser l'utilisateur 
        setUser({});
        //Rediriger vers la page d'accueil
        setRedirect("Home")
        //Cacher le nom de l'utilisateur
        document.getElementById("loggedInLabel").classList.add('hidden');
        document.getElementById("LogOut").classList.add('hidden');
        //Remettre le dark mode à off
        var element = document.getElementById("appDiv");
        element.classList.remove("dark-mode");
    }

    const cancelAddition = () => {
		//Cacher la popup
		document.getElementById("ChannelPopup").style.visibility = "hidden";
	}

	const addChannel = async () => {
		//Ajout d'un channel en récupérant la valeur du textfield
		const name = document.getElementById('addChannels').value;
		var sameName = false;

		//Permet d'imposer un nom de channel unique (feature à ajouter si on le souhaite)

		/*await axios.get('http://localhost:8000/api/v1/channels').then(response => {
			const channels = response.data;
			channels.map(chl => {
				if(chl.name === name){
					sameName = true;
					window.alert('This channel name is already taken. Please choose another one.')
				}
				return chl;
			})
		});*/
		if(sameName === false){
			//Ajout du channel dans la database et affectation du owner
			await axios.post('http://localhost:8080/channels/', {
				name: name,
				owners: [user.name],
			});
			//Récupérer les channels de l'utilisateur
			await axios.get('http://localhost:8080/channels').then(response => {
				const channels = response.data;
				const userChannels = [];

				channels.map(channel => {
					channel.owners.map(owner => {
						if(owner === user.name){
							userChannels.push(channel);
						}
						return owner;
					})
					return channel;
				});

				//Associer les channels de l'utilisateur à la variable channels
				setChannels(userChannels);
			});

			//Réinitialisation des variables
			document.getElementById("addChannels").value = "";
			document.getElementById("ChannelPopup").style.visibility = "hidden";
		}
    }

    //Nous avons du mettre la popup de creation de channel ici par soucis d'affichage
    return (
        <div id="appDiv" className="app">
            <div id="ChannelPopup" className="popup">
				<span className="helper"></span>
				<div>
					<div className="popupCloseButton" onClick={cancelAddition} type="submit">&times;</div>
        			<h1>Channel Creation :</h1>
					<br/>
					<h2>Name : <label id="popupChannelName"></label></h2>
					<br/>
					<h3>Important : Only 5 people are allowed per channel !</h3>
					<br/>
					<button type="submit" onClick={cancelAddition}>Cancel</button>&nbsp;&nbsp;<button type="submit" onClick={addChannel}>Create</button>
    			</div>
			</div>
            <Header onChangeDirHome={() => redirectTo("Home")} onChangeDirAccount={() => redirectTo("Account")} onChangeDirChannel={() => redirectTo("Channel")} LogOut={LogOut}/>
            <Main direction={redirect} redirect={() => redirectTo("Channel")} user={user.name} setUser={setUser} token={user.token} email={user.email} channels={channels} setChannels={setChannels}/>
            <Footer/>
        </div>
    );
}

export default App;
