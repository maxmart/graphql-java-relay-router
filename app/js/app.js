import 'babel/polyfill';

import React from 'react';
import ReactDOM from 'react-dom';

import Relay from 'react-relay';
import { Router, Route } from 'react-router';
import ReactRouterRelay from 'react-router-relay';
import { history } from 'react-router/lib/HashHistory';

import mui from 'material-ui-io';

import App from './components/App';
import RegistrationMessagesOverview from './components/RegistrationMessagesOverview';
import SingleMessageView from './components/SingleMessageView';
import RegistrationOverview from './components/RegistrationOverview'; 
import RegistrationEconomyOverview from './components/RegistrationEconomyOverview'; 
import TeamOverview from './components/TeamOverview'; 


const ViewerQueries = {
  boardroom: () => Relay.QL`
      query RootQuery {
        boardroom
      }
    `
};

const MessageQueries = {
  comment: () => Relay.QL`
      query RootQuery {
        comment(id: $messageId)
      }
    `
};


const loadingFunc = function () {
  return <mui.RefreshIndicator size={40} left={80} top={5} status="loading" />;
};

ReactDOM.render((
  <Router history={history} createElement={ReactRouterRelay.createElement}>
    <Route path="/reg/:id" component={App} indexRoute={{component:RegistrationOverview}}> // apparently the indexRoute is in flux for react-router 1.0 and might change
		<Route path="messages" component={RegistrationMessagesOverview} queries={ViewerQueries} renderLoading={loadingFunc} >
			<Route path=":messageId" component={SingleMessageView} queries={MessageQueries} renderLoading={loadingFunc} />
		</Route>
		<Route path="economy" component={RegistrationEconomyOverview} />
		<Route path="team/:teamId" component={TeamOverview} />
	</Route>
  
  </Router>),
  document.getElementById('root'));

