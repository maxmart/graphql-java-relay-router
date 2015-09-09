import React, { Component } from 'react';
import {Link} from 'react-router';

export default class RegistrationSidebar extends Component {
	constructor(props) {
		super();
		this.props = props;
	}

	render() {
		return (
			<div style={{float:'left', width: 150, height: 300, background: '#ccc'}}>

				{this.props.post && this.props.post.name} <br/>
				#{this.props.post && this.props.post.regNr}

				<br/>
				<button>Ändra namn</button>

				<hr/>
				<Link to="/reg/123">ÖVERSIKT</Link> <br/>
				<Link to="/reg/123/messages">MEDDELANDEN</Link> <br/>
				<Link to="/reg/123/economy">EKONOMI</Link> <br/>
				<hr/>
				<Link to="/reg/123/team/1231">P13 1</Link> <br/>
			</div>);
	}
}