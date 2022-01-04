import React, {useState, useEffect} from 'react';
import Messages from '../message/Messages';
import MessageForm from '../message/MessageForm';
import './Channel.css';
import axios from 'axios';

const Channel = (props) => {
	const [messages, setMessages] = useState([]);
	const [content, setContent] = useState('');

	const addMessage = async () => {
		//Ajouter un message à la database
		await axios.post('http://localhost:8000/api/v1/channels/'+props.channel.id+'/messages', {
			author:props.user,
			content: content,
			created_at: Date.now()
		})

		//Récupérer les messages du channel
		await axios.get('http://localhost:8000/api/v1/channels/'+props.channel.id+'/messages').then(response => {
			setMessages(response.data);
		})

		//Réinitialiser le contenue du textfield
		setContent('');
	};

	useEffect(() => {
		if(props.channel !== undefined){
			//Récupération du token
			axios.defaults.headers.common = {'Authorization': `bearer ${props.token}`}
			//Récupération des messages du channel
            axios.get('http://localhost:8000/api/v1/channels/'+props.channel.id+'/messages').then(response => {
			    setMessages(response.data);
		    })
        }
	},[props]);

	const onMessageDeleted = async () => {
		//Récupérer les messages updatés
		await axios.get('http://localhost:8000/api/v1/channels/'+props.channel.id+'/messages').then(response => {
			setMessages(response.data);
		})
	}

	if(props.channel === undefined){
		return (
			<div className="channel">
				<br/><br/><br/><br/>
				<h1>Please Select A Channel ! <span role='img' aria-label='smiley'>&#128540;</span></h1>
			</div>
		);
	}
	else {
		return (
			<div className="channel">
				<h1>Messages for {props.channel.name}</h1>
				<div className="messages">
					<Messages messages={messages} handleDelete={onMessageDeleted} avatar={props.avatar} avatarID={props.avatarID}/>
				</div>
				<MessageForm content={content} setContent={setContent} addMessage={addMessage} author={props.user}/>
			</div>
		);
	}
};

export default Channel;
