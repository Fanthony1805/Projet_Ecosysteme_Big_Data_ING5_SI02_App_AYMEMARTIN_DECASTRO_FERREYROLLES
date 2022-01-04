import React, { useEffect } from 'react';
import './AccountIcon.css';
import Gravatar from 'react-awesome-gravatar';
import axios from 'axios';


const AccountIcon = (props) => {
    useEffect(() => {
        //Récupérer les avatars
        axios.get('http://localhost:8000/api/v1/avatar').then(res => {
            const avatars = res.data;

            avatars.map(avatar => {
                if(avatar.user === props.user){ //Voir si un avatar est déjà associé au compte du l'utilisateur
                    switch(avatar.image){ //Si oui, afficher l'avatar
                        case 'GravatarSelection':
                            document.getElementById("userAvatar").innerHTML = "<Gravatar email={props.email}><img id='GravatarIconImage' src='"+avatar.source+"'/></Gravatar>";
                            document.getElementById('GravatarIconImage').classList.add("defaultSize");
                            break;
                        case 'FileSelection':
                            document.getElementById("userAvatar").innerHTML = "<div id='userAvatar"+props.user+"Icon'/>";
                            document.getElementById("userAvatar"+props.user+"Icon").style.background = 'url('+avatar.source+') no-repeat';
                            document.getElementById("userAvatar"+props.user+"Icon").style.backgroundSize = '4cm'
                            document.getElementById("userAvatar"+props.user+"Icon").classList.add("defaultSize");
                            break;
                        default:
                            document.getElementById("userAvatar").innerHTML = "<div id='userAvatar"+props.user+"Icon'/>";
                            document.getElementById("userAvatar"+props.user+"Icon").classList.add("defaultSize");
                            document.getElementById("userAvatar"+props.user+"Icon").classList.add(avatar.source);
                            break;
                    }
                }
                return avatar;
            });
        });
    },[props.user])

    const changeAvatar = async (avatar) => {
        //Afficher l'avatar
        document.getElementById("userAvatar").innerHTML = "<div id='userAvatar"+props.user+"Icon'/>";
        document.getElementById("userAvatar"+props.user+"Icon").classList.add("defaultSize");
        document.getElementById("userAvatar"+props.user+"Icon").classList.add(avatar);

        //Récupérer les avatars
        await axios.get('http://localhost:8000/api/v1/avatar').then(async res => {
            const avatars = res.data;
            var exist =  false;
            var id = 0;
            var finished = false;

            try{
                await avatars.map(avatar => {
                    if(avatar.user === props.user){
                        exist = true; //Si l'utilisateur a déjà configuré son avatar, récupérer l'id
                        id = avatar.id;
                    }
                    return avatar;
                }).then(finished = true);
            } catch {
                finished = true;
            }
            if(finished){
                if(!exist){ //Associer l'avatar au compte utilisateur
                    axios.post('http://localhost:8000/api/v1/avatar',{
                        user: props.user,
                        image: 'AvatarSelection',
                        source: avatar,
                    });
                } else { //Sinon updater son avatar
                    axios.put('http://localhost:8000/api/v1/avatar/'+id,{
                        user: props.user,
                        image: 'AvatarSelection',
                        source: avatar,
                    });
                }
            }
        });
    }

    const FileUploadHandler = async (e) => {
        if (e.target.files) {
            let file = e.target.files[0]; //Récupérer l'image
            if (file) {
                var image = document.createElement('img');
                image.src = window.URL.createObjectURL(file); //Création d'un url

                //Affichage de l'image
                document.getElementById("userAvatar").innerHTML = "<div id='userAvatar"+props.user+"Icon'/>";
                document.getElementById("userAvatar"+props.user+"Icon").style.background = 'url('+image.src+') no-repeat';
                document.getElementById("userAvatar"+props.user+"Icon").style.backgroundSize = '4cm'
                document.getElementById("userAvatar"+props.user+"Icon").classList.add("defaultSize");

                //Récupération des avatars
                await axios.get('http://localhost:8000/api/v1/avatar').then(async res => {
                    const avatars = res.data;
                    var exist =  false;
                    var id = 0;
                    var finished = false;

                    try{
                        await avatars.map(avatar => {
                            if(avatar.user === props.user){
                                exist = true;
                                id = avatar.id;
                            }
                            return avatar;
                        }).then(finished = true);
                    } catch {
                        finished = true;
                    }
                    if(finished){
                        if(!exist){ //Associer l'avatar au compte utilisateur si pas déjà fait
                            axios.post('http://localhost:8000/api/v1/avatar',{
                                user: props.user,
                                image: 'FileSelection',
                                source: image.src,
                            });
                        } else { //Sinon updater son avatar
                            axios.put('http://localhost:8000/api/v1/avatar/'+id,{
                                user: props.user,
                                image: 'FileSelection',
                                source: image.src,
                            });
                        }
                    }
                });
            }
        } else {
          console.log("Problem occured while downloading the image.");
        }
    };

    const GravatarIcon = async (url) => {
        //Affichage de l'image
        document.getElementById("userAvatar").innerHTML = "<Gravatar email={props.email}><img id='GravatarIconImage' src='"+url+"'/></Gravatar>";
        document.getElementById('GravatarIconImage').classList.add("defaultSize");

        //Récupération des avatars
        await axios.get('http://localhost:8000/api/v1/avatar').then(async res => {
            const avatars = res.data;
            var exist =  false;
            var id = 0;
            var finished = false;

            try{
                await avatars.map(avatar => {
                    if(avatar.user === props.user){
                        exist = true;
                        id = avatar.id;
                    }
                    return avatar;
                }).then(finished = true);
            } catch {
                finished = true;
            }
            if(finished){
                if(!exist){ //Associer l'avatar au compte utilisateur si pas déjà fait
                    axios.post('http://localhost:8000/api/v1/avatar',{
                        user: props.user,
                        image: 'GravatarSelection',
                        source: url,
                    });
                } else { //Sinon updater son avatar
                    axios.put('http://localhost:8000/api/v1/avatar/'+id,{
                        user: props.user,
                        image: 'GravatarSelection',
                        source: url,
                    });
                }
            }
        });
    }

    return(
        <div>
            <div id="userAvatar" className="userAvatar">
                <div id={"userAvatar"+props.user+"Icon"} className="defaultSize default"/>
            </div>
            <div className="avatars">
                <button id="man" type="submit" className="man" onClick={() => changeAvatar("man")}/><button id="woman" type="submit" className="woman" onClick={() => changeAvatar("woman")}/><button id="princess" type="submit" className="princess" onClick={() => changeAvatar("princess")}/><button id="hippie" type="submit" className="hippie" onClick={() => changeAvatar("hippie")}/><button id="queen" type="submit" className="queen" onClick={() => changeAvatar("queen")}/><br/>
                <button id="dancing" type="submit" className="dancing" onClick={() => changeAvatar("dancing")}/><button id="dancer" type="submit" className="dancer" onClick={() => changeAvatar("dancer")}/><button id="elf" type="submit" className="elf" onClick={() => changeAvatar("elf")}/><button id="ninja" type="submit" className="ninja" onClick={() => changeAvatar("ninja")}/><button id="vampire" type="submit" className="vampire" onClick={() => changeAvatar("vampire")}/><br/>
                <button id="clown" type="submit" className="clown" onClick={() => changeAvatar("clown")}/><button id="cat" type="submit" className="cat" onClick={() => changeAvatar("cat")}/><button id="dog" type="submit" className="dog" onClick={() => changeAvatar("dog")}/><button id="fox" type="submit" className="fox" onClick={() => changeAvatar("fox")}/><button id="owl" type="submit" className="owl" onClick={() => changeAvatar("owl")}/><br/>
                <button id="turtle" type="submit" className="turtle" onClick={() => changeAvatar("turtle")}/><button id="koala" type="submit" className="koala" onClick={() => changeAvatar("koala")}/><button id="elephant" type="submit" className="elephant" onClick={() => changeAvatar("elephant")}/><button id="diplodocus" type="submit" className="diplodocus" onClick={() => changeAvatar("diplodocus")}/><button id="tyrannosaurus" type="submit" className="tyrannosaurus" onClick={() => changeAvatar("tyrannosaurus")}/><br/>
            </div>
            <br/>
            <label>Upload your own image :</label><br/><br/>
            <input type="file" id="FormControlFile" onChange={(e) => FileUploadHandler(e)} accept="image/png, image/jpeg"/>
            <div>
                <label>Or use your gravatar :</label>
                <Gravatar email={props.email}>
                    { url => (<img src={url} className="smallSize" alt='Error occured when uploading' onClick={() => GravatarIcon(url)}/>) }
                </Gravatar>
            </div>
            <div id={"try"} className="defaultSize"/>
        </div>
    );
};

export default AccountIcon;
