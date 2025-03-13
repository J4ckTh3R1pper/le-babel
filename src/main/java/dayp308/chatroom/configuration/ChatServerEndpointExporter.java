package dayp308.chatroom.configuration;

import dayp308.chatroom.websocket.ChatEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//@Configuration
public class ChatServerEndpointExporter {
//    @Bean
    public ServerEndpointExporter export() {
        ServerEndpointExporter exporter  = new ServerEndpointExporter();
        exporter.setAnnotatedEndpointClasses(ChatEndpoint.class);
        return exporter;
    }
}
