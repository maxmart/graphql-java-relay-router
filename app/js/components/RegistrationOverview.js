import React, { Component } from 'react';
import ShirtColorPart from './parts/ShirtColorPart'
import ArrivalInfoPart from './parts/ArrivalInfoPart'

export default class RegistrationOverview extends Component {
	constructor(props) {
		super();
		this.props = props;
	}

	render() {
		return (<div>
			<ShirtColorPart post={this.props.post} />
			<ArrivalInfoPart post={this.props.post} />
		</div>);
	}
}