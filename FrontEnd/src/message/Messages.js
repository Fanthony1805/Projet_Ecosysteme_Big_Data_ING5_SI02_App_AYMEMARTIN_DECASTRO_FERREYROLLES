import React, { useState } from 'react';
import Message from './Message';
import "./Messages.css";

const Messages = (props) => {
	const [action, setAction] = useState(false);
	
	return(
		<ul>
			{props.messages.sort((a,b) => a.created_at > b.created_at ? 1:-1).map(message => (
				<div>
					<Message key={message.id} message={message} handleDelete={props.handleDelete} action={action} setAction={setAction} avatar={props.avatar}/>
				</div>
			))}
		</ul>
	);
};

export default Messages;
