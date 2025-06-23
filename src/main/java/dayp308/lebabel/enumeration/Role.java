package dayp308.lebabel.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum Role {
    GUEST, SUBSCRIBER, MODERATOR, ADMIN
}
