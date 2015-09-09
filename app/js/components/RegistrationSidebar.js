import React, { Component } from 'react';
import {Link} from 'react-router';
import mui from 'material-ui-io';

export default class RegistrationSidebar extends Component {
	constructor(props) {
		super();
		this.props = props;
	}
	
	onChange(a, b, c) {
		window.location.hash = c.route; 
	}

	render() {
		var menuItems = [  { route: '/reg/123', text: 'Overview' },
		                   { route: '/reg/123/messages', text: 'Messages' },
		                   { route: '/reg/123/economy', text: 'Economy' },
		                   { type: mui.MenuItem.Types.SUBHEADER, text: 'Teams' },
		                   {
		                      route: '/reg/123/team/1231',
		                      text: 'Boys 12'
		                   }
		                   ];
		return (
			<mui.LeftNav ref="leftNav" menuItems={menuItems} onChange={this.onChange.bind(this)} style={{position:'absolute', top:65}}>
				<Link to="/hejsan">Hallå</Link>
			</mui.LeftNav>
/*
			<div style={{float:'left', width: 150, height: 300, background: '#ccc'}}>

				{this.props.post && this.props.post.name} <br/>
				#{this.props.post && this.props.post.regNr}

				<br/>
				<mui.RaisedButton label="Ändra namn" />
 
				<hr/>
				<Link to="/reg/123">ÖVERSIKT</Link> <br/>
				<Link to="/reg/123/messages">MEDDELANDEN</Link> <br/>
				<Link to="/reg/123/economy">EKONOMI</Link> <br/>
				<hr/>
				<Link to="/reg/123/team/1231">P13 1</Link> <br/>
			</div>*/);
	}
}