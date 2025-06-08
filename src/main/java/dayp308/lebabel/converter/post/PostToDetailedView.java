package dayp308.lebabel.converter.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import dayp308.lebabel.entity.view.post.PostDetailedView;
import dayp308.lebabel.repository.projection.PostProjection;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostToDetailedView implements Converter<PostProjection, PostDetailedView> {
    private final ObjectMapper objectMapper;

    public PostToDetailedView(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PostDetailedView convert(PostProjection source) {
        PostDetailedView target = new PostDetailedView();
        BeanUtils.copyProperties(source, target, "tags");
        try {
            // https://stackoverflow.com/questions/37187447/map-json-string-array-to-liststring-using-jackson
            target.setTags(objectMapper.readValue(
                    source.tags(),
                    TypeFactory.defaultInstance().constructCollectionType(List.class, String.class)
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

}
