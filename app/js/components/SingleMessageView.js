import React from 'react';
import Relay from 'react-relay';
import CommentEditor from './CommentEditor';

class SingleMessageView extends React.Component {
  constructor(props) {
    super();
    this.props = props;
  }

  render() {
    return (<div>
      <CommentEditor key={this.props.comment.id} comment={this.props.comment} />
      </div>);
  }
}


export default Relay.createContainer(SingleMessageView, {
  fragments: {
    comment: () => Relay.QL`
      fragment on Comment {
        id,
        ${CommentEditor.getFragment('comment')}
      }
    `
  }
});
