import React,{useEffect, useState} from 'react';
import Channels from '../channel/Channels';
import Channel from '../channel/Channel';
import Account from '../Account/Account';
import HomePage from '../HomePage/HomePage';
import './Main.css';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import axios from "axios";

const Main = (props) => {
	const [selectedChannel, setSelectedChannel] = useState();

    useEffect(() => {
        //Récupération du user pour savoir si le dark mode est activé ou pas
        axios.get('http://localhost:8080/user').then(response => { 
            const users = response.data;
            users.map(usr => {
                if (usr.name === props.user){
                    if(usr.nightMode === true){
                        var element = document.getElementById("appDiv");
                        element.classList.remove("dark-mode"); //Evite d'avoir deux fois la même classe
                        element.classList.add("dark-mode");
                    }
                }
                return usr;
            });
        });
    },[props.user])
    
    if(props.direction === "Home"){
        //La direction est Home
        if(selectedChannel !== undefined){
            setSelectedChannel(undefined); //Aucun channel n'est selectionné
        }
        return(
            <HomePage handleClick={setSelectedChannel} redirect={props.redirect} user={props.user} token={props.token} setUser={props.setUser} email={props.email} selectedChannel={selectedChannel} channels={props.channels} setChannels={props.setChannels}/>
        )
    }
    else {
        if(props.direction === "Account"){
            //La direction est Account
            if(selectedChannel !== undefined)setSelectedChannel(undefined) //Aucun channel n'est selectionné
            return(
                <main>
                    <div>
                        <h1>Account Page</h1>
                        <Account user={props.user} setUser={props.setUser} email={props.email} token={props.token}/>
                    </div>
                </main>
            );
        } else
        {
            //La direction est Channels
            if(props.user === undefined) {
                //Demander de s'identifier
                return(
                    <Router>
                        <main>
                            <div >
                                <h1>Please Sign In or Login To Your <Link to='/Account'>Account</Link> ! <span role='img' aria-label='smiley'>&#128540;</span></h1>
                            </div>
                            <Switch>
                                <Route path="/Account">
                                    <Account user={props.user} setUser={props.setUser} email={props.email} token={props.token}/>
                                </Route>
                            </Switch>
                        </main>
                    </Router>
                );
            } else {
                //Si identifié, afficher les channels de l'utilisateur
                return(
                    <main className="main">
                        <div>
                            <h3>
                                Channels list :
                            </h3>
                            <br/>
                            <Channels handleClick={setSelectedChannel} redirect={props.redirect} token={props.token} user={props.user} selectedChannel={selectedChannel} channels={props.channels} setChannels={props.setChannels}/>
                        </div>
                        <Channel channel={selectedChannel} setSelectedChannel={setSelectedChannel} direction={props.direction} user={props.user} token={props.token}/>
                    </main>
                );
            }
            
        }
    }
}

export default Main;