import React, { useEffect,useCallback } from 'react';
import {
    BrowserRouter as Router,
    Link
} from "react-router-dom";
import "./Channels.css"
import axios from 'axios';

const Channels = (props) => {
	var sameName= false;
	var action = false;
	var popupVisible = false;

	const onSelectChannel = useCallback(
		//Affecte à la variable selected channel présente dans le main le channel selectionné
		channel => props.handleClick(channel),
		[props],
	);

	useEffect(() => {

	},[props]);

	const handleClickDelete = async (channelId) => {
		//Suppression du channel de la database
		await axios.delete('http://localhost:8080/channel/'+channelId);
		//Récupération des channels restants
		await axios.get('http://localhost:8080/channel').then(response => {
			const channels = response.data;
			const userChannels = [];

			channels.map(channel => {
				channel.owners.map(owner => {
					if(owner === props.user){
						userChannels.push(channel);
					}
					return owner;
				})
				return channel;
			});

			//Affectation des channels de l'utilisateur à la variable channels
			props.setChannels(userChannels);
		})

		//Affecte à la variable selected channel présente dans le main aucun channel
		onSelectChannel(undefined)
	}
	
	const handleClickEdit = (channel) => {
		//Si aucune action est en cours
		if(action === false){
			//Dire que l'action a débuté
			action = true;
			const channelId = channel.id;
			//Remplacement des champs pour pouvoir éditer notre channnel
			document.getElementById(channelId).innerHTML = '<input type="text" id="textEditChannel"/><button id="buttonCancelEdit" type="submit"/><button id="buttonEditChannelFinish" type="submit"/>';
			//Association de fonctions et classes à ces nouveaux champs
			document.getElementById("buttonEditChannelFinish").onclick = () => finishEditing(channel,true);
			document.getElementById("buttonEditChannelFinish").classList.add("done");
			document.getElementById("buttonCancelEdit").onclick = () => finishEditing(channel,false);
			document.getElementById("buttonCancelEdit").classList.add("cancel");
		}
	}

	const finishEditing = async (channel, edit) => {
		//Récupération de la data
		const newName = document.getElementById("textEditChannel").value;
		const channelId = channel.id;
		sameName = false;

		//Vérification si le nom est déjà pris (facultatif)
		/*if(edit === true){
			await axios.get('http://localhost:8080/channels').then(response => {
				const channels = response.data;
				channels.map(chl => {
					if(chl.name === newName){
						sameName = true;
						window.alert('This channel name is already taken. Please choose another one.');
					}
					return chl;
				})
			});
		}*/
		
		if(sameName === false){
			//Si on a appuyé sur le bouton valider pour faire des modifications
			if(edit === true){
				//Updater la database avec les nouvelles valeurs
				await axios.put('http://localhost:8080/channel/'+channelId,{
					name: newName,
					owners: [props.user],
				});

				const channel = {
					id: channelId,
					name: newName,
				}
				//Reaffectation des champs précédents avec les nouvelles modifications
				document.getElementById(channelId).innerHTML = '<button key="channel'+channel.id+'" id="channel'+channelId+'" type="submit">'+newName+'</button><button id="addPeople" type="submit"/><button id="buttonModify" type="submit"/><button id="buttonDelete" type="submit"/><br/>';
				
				//Affecte à la variable selected channel présente dans le main le channel avec son nouveau nom
				onSelectChannel(channel);


				//Récupération des channels restants
				await axios.get('http://localhost:8080/channel').then(response => {
					const channels = response.data;
					const userChannels = [];

					channels.map(channel => {
						channel.owners.map(owner => {
							if(owner === props.user){
								userChannels.push(channel);
							}
							return owner;
						})
						return channel;
					});

					//Affectation des channels de l'utilisateur à la variable channels
					props.setChannels(userChannels);
				})

				//Termine l'action
				action = false;
			}
			else {
				action = false;
				//Reaffectation des champs précédents sans modifications
				document.getElementById(channelId).innerHTML = '<button key="channel'+channel.id+'" id="channel'+channelId+'" type="submit">'+channel.name+'</button><button id="addPeople" type="submit"/><button id="buttonModify" type="submit"/><button id="buttonDelete" type="submit"/><br/>';
			}
			const newChannel = {
				id: channel.id,
				name: newName,
				owners: channel.owners,
			}

			//Association de fonctions et classes aux champs
			document.getElementById("channel"+channelId).onclick = function(){onSelectChannel(newChannel); props.redirect()};
			document.getElementById("addPeople").onclick = () => handleClickAddPeople(newChannel);
			document.getElementById("buttonModify").onclick = () => handleClickEdit(newChannel);
			document.getElementById("buttonDelete").onclick = () => handleClickDelete(channelId);
			document.getElementById("channel"+channelId).classList.add("channelButton");
			document.getElementById("addPeople").classList.add("addPeople");
			document.getElementById("buttonModify").classList.add("edit");
			document.getElementById("buttonDelete").classList.add("delete");
		}
	}

	const confirmAddition = () => {
		//Si la popup est cachée
		if(!popupVisible){
			//Récupération de la valeur du textfield
			const name = document.getElementById('addChannels').value;
			//Si la valeur est différentes de ces cas
			if(name !== '' & name !== ' ' & name !== null & name !== undefined){
				//Rendre la popup visible et y associer le nom du channel au champ popupChannelName
				document.getElementById("ChannelPopup").style.visibility = "visible";
				document.getElementById("popupChannelName").innerHTML = name;
				//Mettre la variable à true
				popupVisible = !popupVisible;
			} 
		} else {
			//Sinon mettre la variable à false
			popupVisible = !popupVisible;
		}
	}

	const handleClickAddPeople = (id) => {
		//Si aucune action est en cours
		if(action === false){
			//Afficher la popup et signaler qu'une action est en cours
			document.getElementById("popupForm"+id).style.display = "block";
			action = true;
		} else {
			//sinon, faire disparaitre la popup
			if(document.getElementById("popupForm"+id).style.display === "block"){
				document.getElementById("username"+id).value = "";
				document.getElementById("popupForm"+id).style.display = "none";
				action = false;
			}
		}
	}
	  
	const closeForm = (id) => {
		//Faire disparaitre la popup d'ajout de personnes
		document.getElementById("username"+id).value = "";
		document.getElementById("popupForm"+id).style.display = "none";
		action = false;
	}

	const submitForm = async (channel) => {
		//Récupération des users et vérifier que l'utilisateur existe
		const username = document.getElementById("username"+channel.id).value;
		await axios.get('http://localhost:8080/user').then(response => {
			const users = response.data;
			users.map(user => {
				if(user.name === username){
					sameName = true;
				}
				return user;
			})
		});
		if(sameName === true){
			//Si il existe
			var owners = [];
			var alreadyAdded = false;
			var max = 0;

			//Récupérer les infos du channel selectionné
			await axios.get('http://localhost:8080/channel/'+channel.id).then(res => {
				const channel = res.data;
				owners = channel.owners;
			});

			//Vérifier si l'utilisateur fait déjà parti du channel
			owners.map(owner => {
				if(owner === username){
					alreadyAdded = true;
				}
				max += 1; 
				return owner;
			});

			if(!alreadyAdded){
				//Si non
				if(max !== 5){
					//Vérifier si il y a déjà 5 utilisateurs
					owners.push(username);

					//Si non, l'ajouter au channel
					await axios.put('http://localhost:8080/channel/'+channel.id,{
						name: channel.name,
						owners: owners,
					});
					window.alert('User has been added.');
					document.getElementById("username"+channel.id).value = "";
					action = false;
				} else {
					window.alert("This channel already has too much people.");
				}
			} else {
				window.alert("This user has already been added.");
			}
		} else {
			window.alert("This user don't exist.");
		}
	}

	const handleClickInfo = async (id) => {
		//Si aucune action est en cours
		if(action === false){
			//Afficher la popup et signaler qu'une action est en cours
			document.getElementById("popupFormInfo"+id).style.display = "block";
			action = true;
			var li = "";

			//Récupérer les infos du channel selectionné
			await axios.get('http://localhost:8080/channel/'+id).then(res => {
				const channel = res.data;
				const owners = channel.owners;

				owners.map(owner => {
					li += "<li><p>" + owner + "</p></li>" 
					return li;
				});
			}).then(res => {
				document.getElementById("ownersUL"+id).innerHTML = li
			});
			
		} else {
			//sinon, faire disparaitre la popup
			if(document.getElementById("popupFormInfo"+id).style.display === "block"){
				document.getElementById("popupFormInfo"+id).style.display = "none";
				action = false;
			}
		}
	}

	const closeFormInfo = (id) => {
		//Faire disparaitre la popup d'ajout de personnes
		document.getElementById("popupFormInfo"+id).style.display = "none";
		action = false;
	}

	return (
		<Router>
			<div>
				<div className="channels">
					{props.channels.sort((a,b) => a.name > b.name ? 1:-1).map(channel => (
						<div>
							<Link to={"/Channels/"+channel.id}>
								<div id={channel.id}>
									<button key={channel.id} onClick={function(){onSelectChannel(channel); props.redirect()}} className="channelButton">
										{channel.name}
									</button>
									<button className='info' type="submit" onClick={() => handleClickInfo(channel.id)}/>
									<button id="addPeople" className='addPeople' type="submit" onClick={() => handleClickAddPeople(channel.id)}/>
									<button className='edit' type="submit" onClick={() => handleClickEdit(channel)}/>
									<button className='delete' type="submit" onClick={() => handleClickDelete(channel.id)}/>
								</div>
								<div className="form-container" id={"popupForm"+channel.id}>
									<h1>Enter Username to add to the channel:</h1>

									<label htmlFor="username"><b>Username</b></label>
									<input type="text"  id={"username"+channel.id} placeholder="Enter Username" required/>

									<button type="submit" className="btn" onClick={() => submitForm(channel)}>Submit</button>
									<button type="submit" className="btn Cancel" onClick={() => closeForm(channel.id)}>Close</button>
								</div>
								<div className="form-container" id={"popupFormInfo"+channel.id}>
									<h1>Channel: {channel.name}</h1>
									<br/>

									<h2>Users</h2><br/>
									<ul id={"ownersUL"+channel.id}>
									</ul>
									<br/><br/>

									<button type="submit" className="btn Cancel" onClick={() => closeFormInfo(channel.id)}>Close</button>
								</div>
							</Link>
						</div>
					))}
				</div>
				<br/>
                <input type="text" id='addChannels' className="addChannelText"/><button type="submit" onClick={confirmAddition} className="addChannelsButton"></button>
			</div>
		</Router>
	);
}

export default Channels;
