# graphql-java-relay-router
Small sample app I'm playing with to learn about React, Relay & GraphQL.
Maybe someone else will also learn from it.

Uses:

* React (0.14)
* Relay
* React Router (https://github.com/relay-tools/react-router-relay)
* Material UI (https://github.com/juhaelee/material-ui-io)
* Webpack
* GraphQL backend in Java (https://github.com/andimarek/graphql-java)

Adapted from @andimarek's todomvc-relay-java project (https://github.com/andimarek/todomvc-relay-java), but replaced with my own very simple data model.

## Running
Build the java project with `mvn install` and run the `GraphQLServerStart` class

Then start a node process which uses webpack to serve javascript and proxy all graphql requests by running `npm start` in the `app` directory

That takes a while to load up, but then you can visit `http://localhost:3000/#/reg/messages`

## Notes
* I've added a delay on all graphql requests on purpose to see that the optimistic updates are applied. (See `GraphQLServerStart`)

* material-ui doesn't support React 0.14 so that's why I use material-ui-io

* Relay currently has a bug that causes components not to re-render when their optimistic update state changes. This is why the 'SAVING' text stays even after you've edited a comment. (See https://github.com/facebook/relay/issues/86)

* If you change the schema in GraphQLTest, you need to run the ExportSchema class to get a new JSON version of the schema and put in the `app/data/schema.json` file. The babelRelayPlugin uses that schema to validate the queries in the javascript code.

* If you can't find why nothing gets logged, it's because I included slf4j-nop in pom.xml (lazy me)

* The model doesn't at all correspond to the UI. The model is only for messages and barely that. 