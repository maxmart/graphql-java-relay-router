package net.cupmanager.graphql;


import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import graphql.relay.Relay;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolverProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cupmanager.graphql.BoardRoomSchema.BoardRoom;
import net.cupmanager.graphql.BoardRoomSchema.Comment;

public class GraphQLTest {
	
	public static Map<String, Object> exec(String query, Map<String,Object> variables) {
        ExecutionResult result = new GraphQL(schema).execute(query, (Object)null, variables);
        if (!result.getErrors().isEmpty()) {
        	System.err.println(result.getErrors());
        	throw new RuntimeException();
        }
        Map<String, Object> map = result.getData();
        return map;
	}
	
	
    private static Relay relay = new Relay();
    private static GraphQLSchema schema;
	static {
		
		DataFetcher authorFetcher = new DataFetcher() {
			@Override
			public Object get(DataFetchingEnvironment environment) {
				Comment c = (Comment) environment.getSource();
				return c.author();
			}
		};
		
		GraphQLObjectType UserType = GraphQLObjectType.newObject()
				.name("User")
				.field(newFieldDefinition().name("id").type(new GraphQLNonNull(Scalars.GraphQLID)).build())
				.field(newFieldDefinition().name("name").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
				.build();
		GraphQLObjectType CommentType = GraphQLObjectType.newObject()
				.name("Comment")
				.field(newFieldDefinition().name("id").type(new GraphQLNonNull(Scalars.GraphQLID)).build())
				.field(newFieldDefinition().name("text").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
				.field(newFieldDefinition().name("author").type(UserType).dataFetcher(authorFetcher).build())
				.build();
		
		TypeResolverProxy typeResolverProxy = new TypeResolverProxy();
        GraphQLInterfaceType nodeInterface = relay.nodeInterface(typeResolverProxy);
		GraphQLObjectType commentEdge = relay.edgeType("Comment", CommentType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
		GraphQLObjectType CommentsConnectionType = relay.connectionType("Comment", commentEdge, Arrays.asList());
		
		DataFetcher commentsFetcher = new DataFetcher() {
			@Override
			public Object get(DataFetchingEnvironment environment) {
				BoardRoom br = (BoardRoom) environment.getSource();
				List<Comment> comments = br.comments(null);
				
				List<Edge> edges = new ArrayList<>();
		        int ix = 0;
		        for (Object object : comments) {
		            edges.add(new Edge(object, new ConnectionCursor("CURSOR_" + (ix++))));
		        }
		        
		        PageInfo pageInfo = new PageInfo();
		        pageInfo.setStartCursor(edges.get(0).getCursor());
		        pageInfo.setEndCursor(edges.get(edges.size()-1).getCursor());
		        pageInfo.setHasPreviousPage(false);
		        pageInfo.setHasNextPage(false);
				
		        Connection connection = new Connection();
		        connection.setEdges(edges);
		        connection.setPageInfo(pageInfo);

		        return connection;
				
			}
		};
		GraphQLObjectType BoardRoomType = GraphQLObjectType.newObject()
				.name("BoardRoom")
				.field(newFieldDefinition().name("id").type(new GraphQLNonNull(Scalars.GraphQLID)).build())
				.field(newFieldDefinition()
						.name("comments")
						.type(CommentsConnectionType)
						.argument(relay.getConnectionFieldArguments())
						.dataFetcher(commentsFetcher)
						.build())
				.build();
		
		DataFetcher boardRoomFetcher = new DataFetcher() {
			@Override
			public Object get(DataFetchingEnvironment environment) {
				return BoardRoomSchema.theOnlyBoardRoom;
			}
		};
		
		GraphQLObjectType RootType = GraphQLObjectType.newObject()
			.name("Root")
			.field(newFieldDefinition()
					.name("boardroom")
					.type(BoardRoomType)
					.dataFetcher(boardRoomFetcher)
					.build())
			.field(newFieldDefinition()
					.name("comment")
					.type(CommentType)
					.argument(newArgument().name("id").type(Scalars.GraphQLString).build())
					.dataFetcher(new DataFetcher() {
						@Override
						public Object get(DataFetchingEnvironment environment) {
							String id = environment.getArgument("id");
							Comment comment = BoardRoomSchema.comments.get(Integer.parseInt(id));
							return comment;
						}
					})
					.build())
			.build();
		
		
		
		
		// MUTATIONS!
		
		// ADDING
		GraphQLInputObjectType inputType = GraphQLInputObjectType.newInputObject()
				.name("AddCommentInput")
				.field(newInputObjectField()
						.name("clientMutationId")
						.type(new GraphQLNonNull(Scalars.GraphQLString))
						.build())
				.field(newInputObjectField()
						.name("text")
						.type(new GraphQLNonNull(Scalars.GraphQLString))
						.build())
				.build();
		
		GraphQLObjectType outputType = GraphQLObjectType.newObject()
				.name("AddCommentPayload")
				.field(newFieldDefinition()
                        .name("clientMutationId")
                        .type(new GraphQLNonNull(GraphQLString))
                        .build())
                .field(newFieldDefinition()
                		.name("commentEdge")
                		.type(commentEdge)
                		.dataFetcher(new DataFetcher() {
							@Override
							public Object get(DataFetchingEnvironment environment) {
								Map<String,Object> map = (Map<String, Object>) environment.getSource();
								Comment comment = (Comment) map.get("comment");
								return new Edge(comment, new ConnectionCursor("CURSOR_87"));
							}
						}).build())
				.field(newFieldDefinition()
						.name("boardroom")
						.type(BoardRoomType)
						.build()
						)
                .build();
		
		GraphQLFieldDefinition addCommentField = newFieldDefinition()
	        .name("addComment")
	        .type(outputType)
	        .argument(newArgument()
	                .name("input")
	                .type(new GraphQLNonNull(inputType))
	                .build())
			.dataFetcher(new DataFetcher() {
					@Override
					public Object get(DataFetchingEnvironment environment) {
						Map<String,Object> input = environment.getArgument("input");
						String text = (String) input.get("text");
						String clientMutationId = (String) input.get("clientMutationId");
						
						int id = (int) (System.currentTimeMillis() % 765432);
						Comment comment = new Comment(id, 98, text);
						BoardRoomSchema.comments.put(id, comment);
						
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("clientMutationId", clientMutationId);
						map.put("comment", comment);
						map.put("boardroom", BoardRoomSchema.theOnlyBoardRoom);
						
						System.out.println("DID MUTATION. Map is " + map);
						
						return map;
					}
				})
	        .build();
		
		
		// EDITING
		GraphQLInputObjectType edit_inputType = GraphQLInputObjectType.newInputObject()
				.name("EditCommentInput")
				.field(newInputObjectField()
						.name("clientMutationId")
						.type(new GraphQLNonNull(Scalars.GraphQLString))
						.build())
				.field(newInputObjectField()
						.name("id")
						.type(new GraphQLNonNull(Scalars.GraphQLString))
						.build())
				.field(newInputObjectField()
						.name("text")
						.type(new GraphQLNonNull(Scalars.GraphQLString))
						.build())
				.build();
		
		GraphQLObjectType edit_outputType = GraphQLObjectType.newObject()
				.name("EditCommentPayload")
				.field(newFieldDefinition()
                        .name("clientMutationId")
                        .type(new GraphQLNonNull(GraphQLString))
                        .build())
                .field(newFieldDefinition()
                		.name("comment")
                		.type(CommentType)
                		.dataFetcher(new DataFetcher() {
							@Override
							public Object get(DataFetchingEnvironment environment) {
								Map<String,Object> map = (Map<String, Object>) environment.getSource();
								Comment comment = (Comment) map.get("comment");
								return comment;
							}
						}).build())
				.field(newFieldDefinition()
						.name("boardroom")
						.type(BoardRoomType)
						.build()
						)
                .build();
		
		GraphQLFieldDefinition editCommentField = newFieldDefinition()
	        .name("editComment")
	        .type(edit_outputType)
	        .argument(newArgument()
	                .name("input")
	                .type(new GraphQLNonNull(edit_inputType))
	                .build())
			.dataFetcher(new DataFetcher() {
					@Override
					public Object get(DataFetchingEnvironment environment) {
						Map<String,Object> input = environment.getArgument("input");
						String id = (String) input.get("id");
						String text = (String) input.get("text");
						String clientMutationId = (String) input.get("clientMutationId");
						
						Comment comment = null;
						for (Comment c : BoardRoomSchema.comments.values()) {
							if (c.id.equals(id)) {
								comment = c;
								break;
							}
						}
						
						comment.text = text;
						
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("clientMutationId", clientMutationId);
						map.put("comment", comment);
						
						System.out.println("DID MUTATION. Map is " + map);
						
						return map;
					}
				})
	        .build();
		
		schema = GraphQLSchema.newSchema()
              .query(RootType)
              .mutation(GraphQLObjectType.newObject()
            		  .name("Mutation")
            		  .field(addCommentField)
            		  .field(editCommentField)
            		  .build())
              .build();
	}
	
	
	public static void main(String[] args) {
		ExecutionResult result = new GraphQL(schema).execute(" query Test { comment(id:\"1\") {id, author{name}, text} }");
        System.out.println("errors: " + result.getErrors());
		Map<String, Object> map = result.getData();
        System.out.println("result: " + map);
		System.out.println(schema);
	}

// Tried to generate schema by using reflection on classes, but it became too complicated. Might return to that later..
	
//	@Retention(RetentionPolicy.RUNTIME)
//	public static @interface Argument {
//		String value();
//	}
	
//	private static GraphQLObjectType[] reflect(Class cl) {
//		return new GraphQLObjectType[]{
//			reflectQueryType(cl),
//			null
//		};
//	}



//	private static GraphQLObjectType reflectQueryType(Class cl) {
//		Builder o = newObject().name(cl.getSimpleName());
//		for (Field f : cl.getFields()) {
//			if (f.getType().isAssignableFrom(List.class)) {
//				
//			} else {
//				o.field(newFieldDefinition()
//					.name(f.getName())
//					.type(toGraphQLType(f))
//					.dataFetcher(new DataFetcher(){
//						@Override
//						public Object get(DataFetchingEnvironment environment) {
//							try {
//								return f.get(environment.getSource());
//							} catch (IllegalArgumentException | IllegalAccessException e) {
//								try {
//									Map map = (Map) environment.getSource();
//									return map.get(f.getName());
//								} catch (Exception e2) {
//									e2.printStackTrace();
//									return null;
//								}
//							}
//						}
//					})
//					.build());
//			}
//		}
//		
//		for (Method m : cl.getMethods()) {
////			if (!Modifier.isStatic(m.getModifiers())) continue;
//			if (m.getDeclaringClass() != cl) continue;
//			
////			if (m.getm)
//			if (m.getReturnType().equals(Void.class)) {
//				// Mutation
//			} else {
//				GraphQLOutputType gtype = toGraphQLType(m.getGenericReturnType());
//				graphql.schema.GraphQLFieldDefinition.Builder gfield = newFieldDefinition()
//						.name(m.getName())
//						.type(gtype);
//				
//				Parameter[] params = m.getParameters();
//				for (Parameter param : params) {
//					GraphQLInputType paramType = toGraphQLInputType(param.getType());
//					String paramname = param.getAnnotation(Argument.class).value();
//					GraphQLArgument arg = newArgument()
//						.name(paramname)
//						.type(paramType)
//						.build();
//					gfield.argument(arg);
//				}
//				
//				gfield.dataFetcher(new DataFetcher() {
//							@Override
//							public Object get(DataFetchingEnvironment environment) {
//								Parameter[] params = m.getParameters();
//								Object[] args = new Object[params.length];
//								for (int i=0; i<params.length; i++) {
//									Parameter param = params[i];
//									String paramname = param.getAnnotation(Argument.class).value();
//									args[i] = environment.getArgument(paramname);
//								}
//								try {
//									Object result = m.invoke(environment.getSource(), args);
//									return result;
////									return toGraphQLObject(result);
//								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//									e.printStackTrace();
//								}
//								return null;
//							}
//						});
//				
//				o.field(gfield.build());
//			}
//		}
//		return o.build();
//	}



//	protected static Object toGraphQLObject(Object obj) throws IllegalArgumentException, IllegalAccessException {
//		if (obj instanceof List) {
//			List result = new ArrayList();
//			for (Object item : (List)obj) {
//				result.add(toGraphQLObject(item));
//			}
//			return result;
//		} else if (obj instanceof String) {
//			return obj;
//		} else {
//			Map<String,Object> map = new HashMap<String,Object>();
//			for (Field f : obj.getClass().getFields()) {
//				String name = f.getName();
//				Object value = f.get(obj);
//				map.put(name,value);
//			}
//			return map;
//		}
//	}



//	private static GraphQLOutputType toGraphQLType(Field f) {
//		if (f.getName().equals("id")) {
//			return new GraphQLNonNull(Scalars.GraphQLID);
//		}
//		GraphQLOutputType gtype = toGraphQLType(f.getGenericType());
//		
//		boolean notNull = (f.getAnnotation(NotNull.class) != null);
//		if (notNull) {
//			gtype = new graphql.schema.GraphQLNonNull(gtype);
//		}
//		
//		return gtype;
//	}
	
//	private static GraphQLInputType toGraphQLInputType(Type type) {
//		GraphQLInputType gtype = null;
//		
//		if (type.equals(Integer.class) || type.equals(int.class)) {
//			return Scalars.GraphQLInt;
//		} else if (type.equals(String.class)) {
//			gtype = graphql.Scalars.GraphQLString;
//		}
//		
//		return gtype;
//	}
	
//	private static GraphQLOutputType toGraphQLType(Type type) {
//		GraphQLOutputType gtype = null;
//		
//		if (type instanceof ParameterizedType) {
//			ParameterizedType ptype = (ParameterizedType) type;
//			Type rawType = ptype.getRawType();
//			Type[] innerTypes = ptype.getActualTypeArguments();
//			if (rawType.equals(List.class)) {
//				gtype = new GraphQLList(toGraphQLType(innerTypes[0]));
//			}
//		} else if (type.equals(String.class)) {
//			gtype = graphql.Scalars.GraphQLString;
//		} else {
//			// Custom type?
//			gtype = reflectQueryType((Class) type);
//		}
//		return gtype;
//	}
	
	
	
}
