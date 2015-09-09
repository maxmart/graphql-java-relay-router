import React from 'react';
import Relay from 'react-relay';
import EditCommentMutation from '../mutations/EditCommentMutation';

class CommentEditor extends React.Component {
  constructor(props) {
    super();
    this.props = props;
    this.state = {
      editing: false
    };
  }

  save() {
    Relay.Store.update(
        new EditCommentMutation({text: 'Ändrad kommentar!',
          comment: this.props.comment} )
    );
  }

  toggle() {
    this.setState({
      editing: !this.state.editing
    });
  }

  render() {
    return (<div key={this.props.key} onClick={this.toggle.bind(this)}>
          {this.state.editing ? 'EDITING' : ''}
          {this.props.relay.hasOptimisticUpdate(this.props.comment) ?
            'SAVING' : ''}
          {this.props.comment.author.name} wrote '{this.props.comment.text}'
          {this.state.editing ? (
              <button onClick={this.save.bind(this)}>Save</button>
            ) : ''}
        </div>);
  }
}


export default Relay.createContainer(CommentEditor, {
  fragments: {
    comment: () => Relay.QL`
			fragment on Comment {
				id,
				author {
					name
				},
				text,
        ${EditCommentMutation.getFragment('comment')}
			}
		`
  }
});
