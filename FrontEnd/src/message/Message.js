import React, { useEffect } from 'react';
import moment from 'moment';
import "./Message.css";
import "./Avatar.css";
import axios from 'axios';

const nl2br = require('react-nl2br');

const Messages = (props) => {
	const editMessage = (message) => {
		if(props.action === false){
			//Si aucune action n'est en cours, mettre la variable à true
			props.setAction(true);
			//Afficher les champs pour les modifications
			document.getElementById('messageContent'+message.id).innerHTML = '<input type="text" id="messageText"/><button id="messageButtonCancel"/><button id="messageButtonEdit"/>'
			//Ajouter des classes et des fonctions
			document.getElementById('messageButtonEdit').classList.add('doneEdit');
			document.getElementById('messageButtonEdit').onclick = () => doneEditing(message,true);
			document.getElementById('messageButtonCancel').classList.add('cancel');
			document.getElementById('messageButtonCancel').onclick = () => doneEditing(message,false);
		}
	}

	const doneEditing = async (message,edit) => {
		//Si le bouton edit est appuyé
		if(edit){
			//Récupérer les informations
			const content = document.getElementById('messageText').value;
			const newMessage = {
				author: message.author,
				content: content,
				created_at: message.created_at,
			}

			//Updater la database
			await axios.put('http://localhost:8080/message/'+message.id,newMessage);
			document.getElementById('messageContent'+props.message.id).innerHTML = nl2br(content);
		}
		else {
			//Sinon, remettre dans l'état précedent
			document.getElementById('messageContent'+props.message.id).innerHTML = nl2br(message.content);
		}
		props.setAction(false);
	}

	const deleteMessage = async (message) => {
		//Suppression du message dans la database
		await axios.delete('http://localhost:8080/message/'+message.id);
		//Appel la fonction présente dans channel qui update les messages
		props.handleDelete();
	}

	useEffect(() => {
        axios.get('http://localhost:8000/api/v1/avatar').then(res => {
            const avatars = res.data;

            avatars.map(avatar => {
                try{
					if(avatar.user === props.message.author){
						switch(avatar.image){
							case 'GravatarSelection':
								document.getElementById(props.message.id).innerHTML = "<Gravatar email={props.email}><img id='GravatarIcon"+props.message.id+"' src='"+avatar.source+"'/></Gravatar>";
								document.getElementById('GravatarIcon'+props.message.id).classList.add("defaultAvatarSize");
								break;
							case 'FileSelection':
								document.getElementById(props.message.id).innerHTML = "<div id='userAvatar"+props.message.id+"'/>";
								document.getElementById("userAvatar"+props.message.id).style.background = 'url('+avatar.source+') no-repeat';
								document.getElementById("userAvatar"+props.message.id).style.backgroundSize = '1cm'
								document.getElementById("userAvatar"+props.message.id).classList.add("defaultAvatarSize");
								break;
							default:
								document.getElementById(props.message.id).innerHTML = "<div id='userAvatar"+props.message.id+"'/>";
								document.getElementById("userAvatar"+props.message.id).classList.add("defaultAvatarSize");
								document.getElementById("userAvatar"+props.message.id).classList.add(avatar.source);
						}
					} else {
						document.getElementById('editMessage'+props.message.id).style.visibility = 'hidden';
						document.getElementById('deleteMessage'+props.message.id).style.visibility = 'hidden';
					}
				} catch {
					
				}
                return avatar;
            });
        });
    },[props])

	return(
		<li id={'liMessage'+props.message.id} className="message">
			<p>
				<div id={props.message.id}>
					<div id={'userAvatar'+props.message.id} className="defaultAvatar defaultAvatarSize"/>
				</div>
				<span><b>{props.message.author}</b></span>
				{' - '}
				<span>{moment(props.message.created_at).format("dddd, MMMM Do YYYY, h:mm:ss a")}</span>
				<button id={'deleteMessage'+props.message.id} type='submit' className='deleteMessage' onClick={() => deleteMessage(props.message)}/>
				<button id={'editMessage'+props.message.id} type='submit' className='editMessage' onClick={() => editMessage(props.message)}/>
			</p>
			<div id={'messageContent'+props.message.id}>
				{nl2br(props.message.content)}
			</div>
		</li>
	);
};

export default Messages;
