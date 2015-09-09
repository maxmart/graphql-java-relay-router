import React, { Component } from 'react';

export default class Main extends Component {
	constructor() {
		super();
	}

	render() {
		return (<div style={{marginLeft: 270, marginTop: 10}}>{this.props.children}</div>);
	}
}