import React from 'react';
import Relay from 'react-relay';
import AddCommentMutation from '../mutations/AddCommentMutation';
import CommentEditor from './CommentEditor';
import { Link } from 'react-router';


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

  render() {
    return (<div>
      <h3>Messages</h3>
      Comments:<br/>
      {this.props.boardroom.comments.edges.map(function (edge) {
        return (
          <Link to={`/reg/123/messages/${edge.node.id.substring(8)}`} key={edge.node.id}>
           {this.props.relay.hasOptimisticUpdate(edge.node) ? 'SAVING' : ''}
           {edge.node.author.name} wrote '{edge.node.text}'
           <br/>
          </Link>

		    );
      }.bind(this))}
      <button onClick={this.addComment.bind(this)}>Add comment</button>

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
