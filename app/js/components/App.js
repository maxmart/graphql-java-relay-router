import React, { Component } from 'react';
import SideBar from './SideBar';
import Main from './Main';
import TopBar from './TopBar';
import RegistrationSidebar from './RegistrationSidebar';
import RegistrationOverview from './RegistrationOverview';
import {RouteHandler} from 'react-router';

export default class App extends Component {
  constructor() {
  	super();
  	this.state = {
  		post: null
  	}
  }

  componentDidMount() {
  	this.setState({
  		post: {
  			regNr: "1336",
  			name: "LUGI HF"
  		}
  	});
  }

  render() {
    return (
    <div>
      <TopBar></TopBar>
      <div>
      	<SideBar auto-collapse>
      		<RegistrationSidebar post={this.state.post} />
      	</SideBar>
      	<Main>
      		{this.props.children || 'Select an item on left'}
      	</Main>
      </div>
    </div>
    );
  }
}
