package dayp308.lebabel.converter.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dayp308.lebabel.bean.view.post.PostBriefView;
import dayp308.lebabel.bean.entity.projection.PostProjection;
import dayp308.lebabel.util.MarkdownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

import static dayp308.lebabel.util.MarkdownUtil.getThumbnails;

@Component
public class PostToBriefView implements Converter<PostProjection, PostBriefView> {
    private final ObjectMapper objectMapper;

    public PostToBriefView(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PostBriefView convert(PostProjection source) {
        PostBriefView target = new PostBriefView();
        BeanUtils.copyProperties(source, target, "tags", "content");
        try {
            // https://stackoverflow.com/questions/37187447/map-json-string-array-to-liststring-using-jackson
            target.setTags(objectMapper.readValue(
                    source.tags(),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, String.class)
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        target.setContent(cropContent(source.content()));
        target.setThumbnails(getThumbnails(source.content(), 3));
        return target;
    }

    public static String cropContent(String content) {
        String[] splitted = MarkdownUtil.stripMarkdown(content).split("(\\r?\\n|<br/>|</p>)",2);
        return splitted[0].trim();
    }

}
