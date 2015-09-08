// import Relay from 'react-relay';
import Relay from 'react-relay';
export default class AddCommentMutation extends Relay.Mutation {
  static fragments = {
    boardroom: () => Relay.QL`
      fragment on BoardRoom {
        id,
        comments(first:1000) {
          edges{node{id, author{name}, text}}
        }
      }
    `,
  };
  getMutation() {
    return Relay.QL`mutation{addComment}`;
  }
  getFatQuery() {
    return Relay.QL`
      fragment on AddCommentPayload {
        commentEdge {
          cursor,
          node {
            id,
            author {name},
            text
          }
        },
        boardroom {
          id
        }
      }
    `;
  }
  getConfigs() {
    console.log('this.props.boardroom.id', this.props.boardroom.id);

    return [{
      type: 'RANGE_ADD',
      parentName: 'boardroom',
      parentID: this.props.boardroom.id,
      connectionName: 'comments',
      edgeName: 'commentEdge',
      rangeBehaviors: {
        '': 'prepend',
      },
    }];
  }
  getVariables() {
    return {
      text: this.props.text,
    };
  }
  getOptimisticResponse() {
    return {
      // FIXME: totalCount gets updated optimistically, but this edge does not
      // get added until the server responds
      commentEdge: {
        node: {
          text: this.props.text + ' håller på att sparas...',
          author: {
            name: 'You'
          }
        },
      },
      boardroom: {
        id: this.props.boardroom.id,
      },
    };
  }
}
