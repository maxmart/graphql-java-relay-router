package net.cupmanager.graphql;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardRoomSchema {
	public static Map<Integer,Comment> comments = new HashMap<Integer,Comment>();
	private static Map<Integer,User> users = new HashMap<Integer,User>();
	static{
		comments.put(1, new Comment(1, 98, "Hey guys! Wanna go for lunch?"));
		comments.put(2, new Comment(2, 99, "Alright, sounds cool!"));
		
		users.put(98, new User(98, "Max Martinsson"));
		users.put(99, new User(99, "Someo N. Eelse"));
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
		
		public List<Comment> comments(Integer id) {
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
	}
	
	static BoardRoom theOnlyBoardRoom = new BoardRoom();
	public static class Root {
		public static BoardRoom boardroom() {
			return theOnlyBoardRoom;
		}
	}
}
