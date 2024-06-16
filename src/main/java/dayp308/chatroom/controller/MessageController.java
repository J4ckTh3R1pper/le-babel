package dayp308.chatroom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dayp308.chatroom.bean.Message;
import dayp308.chatroom.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    @PostMapping(value = "/get_msg_history", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMessageHistory(@RequestParam("channel") String channel_id,
                                    @RequestParam("timestamp") String timestamp) throws JsonProcessingException {
        System.out.println("in: channel "+ channel_id + ", timestamp: " + timestamp);
        List<Message> listMsg = messageService.getMessageInChannelByTime(Integer.parseInt(channel_id), Long.parseLong(timestamp));
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String outMsg = mapper.writeValueAsString(listMsg);
        System.out.println("out: " + outMsg);
        return outMsg;
    }
}
