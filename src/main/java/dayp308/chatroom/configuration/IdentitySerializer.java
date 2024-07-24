package dayp308.chatroom.configuration;
import com.fasterxml.jackson.databind.JsonSerializer;
import dayp308.chatroom.bean.Identity;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class IdentitySerializer extends JsonSerializer<Identity> {
	@Override
	public void serialize(Identity value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.getName());
	}
}
