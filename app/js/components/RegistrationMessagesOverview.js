import React from 'react';
import Relay from 'react-relay';
import AddCommentMutation from '../mutations/AddCommentMutation';
import CommentEditor from './CommentEditor';
import { Link } from 'react-router';

import mui from 'material-ui-io';

class RegistrationMessagesOverview extends React.Component {
  constructor(props) {
    super();
    this.props = props;
  }

  addComment() {
    console.log(this.props.boardroom);
    Relay.Store.update(
        new AddCommentMutation({text: 'Den nya kommentaren!',
          boardroom: this.props.boardroom} )
    );
  }
  
  commentClicked(edge) {
	  window.location.hash = `/reg/123/messages/${edge.node.id.substring(8)}`;
  }
  
  render() {
    return (<div>
      <h3>Messages</h3>
      
      <mui.List subheader="Comments">
      {this.props.boardroom.comments.edges.map(function (edge) {
	      return (<div>
	      <mui.ListItem
	      	onClick={() => this.commentClicked(edge)}
	        primaryText={edge.node.author.name}
	        secondaryText={
	          <p>
	            {edge.node.text}
	          </p>
	        }
	        secondaryTextLines={1} />
	      <mui.ListDivider inset={true} />
  		</div>);
      }.bind(this))}
      </mui.List>

     <mui.RaisedButton label="Add Comment" primary={true}
      	disabled={this.props.relay.hasOptimisticUpdate(this.props.boardroom)}
      	onClick={this.addComment.bind(this)} />

      <hr/>
      
     	{this.props.children || 'No comment selected'}

      </div>);
  }
}


export default Relay.createContainer(RegistrationMessagesOverview, {
  fragments: {
    boardroom: () => Relay.QL`
      fragment on BoardRoom {
        comments(first:1000) {
          edges {
            node {
              id,
              author{name},
              text
            }
          }
        },
        ${AddCommentMutation.getFragment('boardroom')}
      }
    `
  }
});
/*
export default Relay.createContainer(RegistrationMessagesOverview, {
  fragments: {
    boardroom: () => Relay.QL`
			fragment on BoardRoom {
				comments(first:1000) {
					edges {
						node {
							id,
							${CommentEditor.getFragment('comment')}
						}
					}
				},
				${AddCommentMutation.getFragment('boardroom')}
			}
		`
  }
});
*/
