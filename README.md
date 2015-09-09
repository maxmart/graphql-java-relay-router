# graphql-java-relay-router
Small sample app I'm playing with to learn about React, Relay & GraphQL. 

Adapted from @andimarek's todomvc-relay-java project (https://github.com/andimarek/todomvc-relay-java), but also adding routing (https://github.com/relay-tools/react-router-relay)

## running
Build the java project with `mvn install` and run the `GraphQLServerStart` class

Then start a node process which uses webpack to serve javascript and proxy all graphql requests by running `npm start` in the `app` directory
That takes a while to load up, but then you can visit `http://localhost:3000/#/reg/messages`

Note that I've added a 1500 ms delay on all graphql requests on purpose to see that the optimistic updates are applied.

Note also that Relay currently has a bug that causes components not to re-render when their optimistic update state changes: https://github.com/facebook/relay/issues/86
This is why the 'SAVING' text stays even after you've edited a comment.
