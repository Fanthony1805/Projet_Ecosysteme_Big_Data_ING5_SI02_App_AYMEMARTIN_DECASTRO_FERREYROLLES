import React from 'react';
import {
    BrowserRouter as Router,
    //Switch,
    //Route,
    Link
} from "react-router-dom";
import './Header.css';

const Header = (props) => {
    const LogOut = () => {
        //Appel de la fonction LogOut présente dans la classe App
        props.LogOut();
    }
    return(
        <Router>
            <header className="header">
                <Link to="/"><button type="submit" className="GoToHome" onClick={props.onChangeDirHome}/></Link>
                <Link to="/Channels"><button type="submit" className="GoToChannels" onClick={props.onChangeDirChannel}/></Link>
                <Link to="/Account"><button type="submit" className="GoToAccount" onClick={props.onChangeDirAccount}/></Link>
                <br/>
                <label id='loggedInLabel' className='hidden'></label><button id='LogOut' type="submit" className='hidden LogOutnButton' onClick={LogOut}></button>
            </header>
        </Router>
    );
};

export default Header;
