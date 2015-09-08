// import Relay from 'react-relay';
import Relay from 'react-relay';
export default class EditCommentMutation extends Relay.Mutation {
  static fragments = {
    comment: () => Relay.QL`
      fragment on Comment {
        id
      }
    `,
  };
  getMutation() {
    return Relay.QL`mutation{editComment}`;
  }
  getFatQuery() {
    return Relay.QL`
      fragment on EditCommentPayload {
        comment {
          id,
          author {name},
          text
        }
      }
    `;
  }
  getConfigs() {
    return [{
      type: 'FIELDS_CHANGE',
      fieldIDs: {
        comment: this.props.comment.id
      }
    }];
  }
  getVariables() {
    return {
      id: this.props.comment.id,
      text: this.props.text,
    };
  }
  getOptimisticResponse() {
    return {
      // FIXME: totalCount gets updated optimistically, but this edge does not
      // get added until the server responds
      comment: {
        id: this.props.comment.id,
        text: this.props.text
      }
    };
  }
}
