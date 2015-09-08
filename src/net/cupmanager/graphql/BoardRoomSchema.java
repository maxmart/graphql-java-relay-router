package net.cupmanager.graphql;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cupmanager.graphql.GraphQLTest.Argument;

public class BoardRoomSchema {
	public static Map<Integer,Comment> comments = new HashMap<Integer,Comment>();
	private static Map<Integer,User> users = new HashMap<Integer,User>();
	static{
		comments.put(1, new Comment(1, 98, "Hejsan på er"));
		comments.put(2, new Comment(2, 99, "Scchysch!"));
		
		users.put(98, new User(98, "Max Författaren"));
		users.put(99, new User(99, "Mux Fattaru"));
	}
	
	
	
	
	public static class Comment {
		public String id;
		private int authorUserId;
		public String text;
		
		public Comment(int id, int authorUserId, String text) {
			this.authorUserId = authorUserId;
			this.text = text;
			this.id = "Comment:" + id;
		}
		
		public User author() {
			return users.get(authorUserId);
		}
		
		public String getId() {
			return id;
		}
		public String getText() {
			return text;
		}
	}
	
	
	public static class User {
		public User(int id, String name) {
			this.name = name;
			this.id = "User:" + id;
		}
		public String id;
		public String name;
		
		public String getId() {
			return id;
		}
		public String getName() {
			return name;
		}
	}
	public static class BoardRoom {
		public String id = "BoardRoom:1";
		
		public List<Comment> comments(@Argument("id") Integer id) {
			List<Comment> cs = new ArrayList<>();
			if (id == null) {
				cs.addAll(comments.values());
			} else {
				cs.add(comments.get(id));
			}
			return cs;
		}
		
		public String getId() {
			return id;
		}
		
//		public static void postComment(@Argument("author") String author, @Argument("text") String text) {
//			Map<String,String> comment1 = new HashMap<>();
//			comment1.put("author", author);
//			comment1.put("text", text);
//			comments.add(comment1);
//		}
	}
	
	static BoardRoom theOnlyBoardRoom = new BoardRoom();
	public static class Root {
		public static BoardRoom boardroom() {
			return theOnlyBoardRoom;
		}
	}
	
	public static class AddCommentInput {
		public String clientMutationId;
		public String text;
	}
	public static class AddCommentPayload {
		public String clientMutationId;
		public Comment comment;
		public BoardRoom boardRoom;
	}
}
