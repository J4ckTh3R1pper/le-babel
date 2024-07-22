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
import org.springframework.lang.Nullable;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
	private ObjectMapper objectMapper;
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
		this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @PostMapping(value = "/get_msg_history", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getMessageHistory(@RequestParam("channelId") int channelId,
                                    @RequestParam("startTime") Long startTime,
									@RequestParam("endTime") Long endTime
									) throws JsonProcessingException {
        System.out.println("in: channel "+ channelId + ", startTime: " + startTime + ",endTime:" + endTime);
        List<Message> listMsg = messageService.getMessageInChannelByTime(channelId, startTime, endTime);
        String outMsg = objectMapper.writeValueAsString(listMsg);
        System.out.println("out: " + outMsg);
        return outMsg;
    }

	@PostMapping(value = "/get_last_messages", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getLastMessages(@RequestParam("channelId") int channelId,
								  @RequestParam("timestamp") long timestamp) throws JsonProcessingException {
		List<Message> listMsg = messageService.getLastMessageInChannel(channelId, timestamp);
		String outMsg = objectMapper.writeValueAsString(listMsg);
		System.out.println("out: " + outMsg);
		return outMsg;
	}
}
