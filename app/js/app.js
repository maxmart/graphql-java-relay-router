import 'babel/polyfill';
import React from 'react';
import ReactDOM from 'react-dom';

import Relay from 'react-relay';
import { Router, Route } from 'react-router';
import ReactRouterRelay from 'react-router-relay';
import { history } from 'react-router/lib/HashHistory';

import RegistrationMessagesOverview from './RegistrationMessagesOverview';
import SingleMessageView from './SingleMessageView';


const ViewerQueries = {
  // boardroom: (Component) => Relay.QL`query { boardroom }`
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

ReactDOM.render((
  <Router history={history} createElement={ReactRouterRelay.createElement}>
      <Route path="/reg/messages" 
        component={RegistrationMessagesOverview}
        renderLoading={function () {
          return <div>laddar</div>
        }}
        queries={ViewerQueries}>
      
      <Route path=":messageId" 
            component={SingleMessageView} 
            renderLoading={function () {
              return <div>laddar! </div>;
            }} 
            queries={MessageQueries}
          />
      </Route>
  </Router>),
  document.getElementById('root'));

/*
class TodoAppHomeRoute extends Relay.Route {
  static path = '/';
  static queries = {
    boardroom: (Component) => Relay.QL`
      query RootQuery {
        boardroom {
          ${Component.getFragment('boardroom')}
        },
      }
    `,
  };
  static routeName = 'TodosHomeRoute';
}
React.render(
  <Relay.RootContainer Component={RegistrationMessagesOverview}
    renderLoading={function () {
      return <div>Loading...</div>;
    }}
    route={new TodoAppHomeRoute()} />,
  document.getElementById('root')
);

*/

/*
class TestRoute extends Relay.Route {
  static path = '/';
  static queries = {
    boardroom: (Component) => Relay.QL`
      query RootQuery {
        boardroom {
          ${Component.getFragment('boardroom')},
        },
      }
    `,
  };
  static routeName = 'TestRoute';
}


React.render(
    <Relay.RootContainer Component={RegistrationMessagesOverview}
      route={new TestRoute()} />,
    document.getElementById('root')
  );
*/
