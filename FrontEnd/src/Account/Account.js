import React from 'react';
import "./Account.css"
import SignIn from "./SignIn.js";
import Login from "./Login";
import AccountSettings from './AccountSetting';
import AccountIcon from './AccountIcon';
import Theme from './Theme';
import "./Theme.css";
import {
    BrowserRouter as Router,
} from "react-router-dom";

const Account = (props) => {
    if(props.user !== undefined){
        //Cas où l'utilisateur est connecté
        return(
            <div className='accountPage'>
                <Router path='/Account'>
                    <div className='AccountIcon'>
                        <AccountIcon user={props.user} email={props.email} />
                    </div>
                    <AccountSettings user={props.user} setUser={props.setUser} token={props.token}/>
                    <Theme user={props.user}/>
                </Router>
            </div>
        );
    }
    else{
        return(
            <Router path='/Account'>
                <main className="accountPage">
                    <br/>
                    <div className="LoginDiv">
                        <Login setUser={props.setUser}/>
                    </div>
                    <div className="SignInDiv">
                        <SignIn/>
                    </div>
                </main>
            </Router>
        );
    }
};

export default Account;