package dayp308.chatroom.model;

import dayp308.chatroom.configuration.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

// @Component
@JsonSerialize(using = IdentitySerializer.class)
public enum Identity {
	NON_MEMBER("non_member", -1),
	MEMBER("member", 0),
	ADMIN("admin", 1),
	OWNER("owner", 2);
	private final String name;
	private final int index;

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	Identity(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static Identity getIdentity(String str) {
		if (str != null)
			switch (str) {
				case "member" -> { return Identity.MEMBER; }
				case "admin" -> { return Identity.ADMIN; }
				case "owner" -> { return Identity.OWNER; }
				default -> {return Identity.NON_MEMBER;}
			}
		return Identity.NON_MEMBER;
	}
}
