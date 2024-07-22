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
								  @Nullable @RequestParam("index") String index,
								  @Nullable @RequestParam("timestamp") String timestamp
								  ) throws Exception {
		if ( ( index != null && timestamp != null ) || ( index == null && timestamp == null ) ) {
			throw new Exception();
		}
		List<Message> listMsg;
		String outMsg = "no result";
		if ( index != null) {
			listMsg = messageService.getLastMessageInChannel(channelId, Integer.parseInt(index));
			outMsg = objectMapper.writeValueAsString(listMsg);
		}
		if (timestamp != null) {
			listMsg = messageService.getLastMessageInChannelByTimestamp(channelId, Long.parseLong(timestamp));
			outMsg = objectMapper.writeValueAsString(listMsg);
		}
		System.out.println("out: " + outMsg);
		return outMsg;
	}
}
