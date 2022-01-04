import React, { useEffect } from 'react';
import "./Theme.css"
import axios from 'axios';

const Theme = (props) => {
    var isChecked = false;

    useEffect(() => {
        axios.get('http://localhost:8080/users').then(response => { 
            const users = response.data;
            users.map(usr => {
                if (usr.name === props.user){
                    // eslint-disable-next-line react-hooks/exhaustive-deps
                    isChecked = usr.nightMode;
                    if(usr.nightMode === true){
                        document.getElementById("switchThemeMode").checked = true;
                    }
                }
                return usr;
            });
        });
    },[]);

    const handleChange = () => {
		isChecked = !isChecked;
        axios.get('http://localhost:8080/users').then(response => { 
            const users = response.data;
            users.map(usr => {
                if (usr.name === props.user){
                    const user = {
                        name: usr.name,
                        email: usr.email,
                        password: usr.password,
                        nightMode: isChecked,
                    }
                    axios.put('http://localhost:8080/users/'+usr.id,user);
                }
                return usr;
            });
        });
        var element = document.getElementById("appDiv");
        element.classList.toggle("dark-mode");
    }

    return(
        <div className="ThemeSettings">
            <h2>Account Settings</h2><br/><br/><br/>
            <h3>Night Mode :</h3>
            <label className="switch">
                <input id="switchThemeMode" type="checkbox" onClick={handleChange}/>
                <span className="slider round"/>
            </label>
        </div>
    );
}

export default Theme;