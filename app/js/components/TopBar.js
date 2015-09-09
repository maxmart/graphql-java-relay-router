import React, { Component } from 'react';
import mui from 'material-ui-io';

export default class TopBar extends Component {
	constructor() {
		super();
	}

	render() {
		return (
			<div>
				<mui.AppBar
				  title="Registration"
				  iconClassNameRight="muidocs-icon-navigation-expand-more">
					Anmälan   Inställnignar Logga ut
				</mui.AppBar>

				
			</div>);
	}
}