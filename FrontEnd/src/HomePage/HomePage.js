import React, { useEffect, useState } from 'react';
import Channels from '../channel/Channels';
import Account from '../Account/Account';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from "react-router-dom";
import "./HomePage.css"

const HomePage = (props) => {
    const [user,setUser] = useState();

    useEffect(() => {
        setUser(props.user)
    },[props.user])

    if(user === undefined){
        return(
            <Router path='/'>
                <main className="homePage">
                    <h1 className="homeTitle">
                        Welcome to the main page !
                    </h1>
                    <br/>
                    <h1>Please Sign In or Login To Your <Link to='/Account'>Account</Link> ! <span role='img' aria-label='smiley'>&#128540;</span></h1>
                    <br/>
                    <Switch>
                        <Route path="/Account">
                            <Account user={user} setUser={props.setUser} email={props.email} token={props.token}/>
                        </Route>
                    </Switch>
                </main>
            </Router>
        );
    } else {
        return(
            <Router>
                <main className="homePage">
                    <h1 className="homeTitle">
                        Welcome to the main page !
                    </h1>
                    <br/><br/>
                    <h2 className="homeText">Please Select a Channel :</h2>
                    <br/><br/>
                    <Link to="/Channels">
                        <Channels handleClick={props.handleClick} redirect={props.redirect} token={props.token} user={props.user} selectedChannel={props.selectedChannel} channels={props.channels} setChannels={props.setChannels}/>
                    </Link>
                </main>
            </Router>
        );
    };
}

export default HomePage;