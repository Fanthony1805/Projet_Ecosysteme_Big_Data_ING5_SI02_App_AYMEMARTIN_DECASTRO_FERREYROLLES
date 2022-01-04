import React from 'react';
import "./MessageForm.css";

const MessageForm = (props) => {
	//You can improve this function with one hook (useCallback) :
	const onChange = (e) => {
		//Affecter à la variable content présente dans channel, le nouveau contenu
		props.setContent(e.target.value);
	};

	return (
		<div className="form">
            <textarea
				onChange={onChange}
				name="content"
				rows={5}
				className="content"
				value={props.content}
			/>
			<button onClick={props.addMessage} type="submit" className="send"/>
		</div>
	)
};

export default MessageForm;
