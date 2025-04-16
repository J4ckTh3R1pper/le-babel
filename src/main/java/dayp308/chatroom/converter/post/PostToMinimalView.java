package dayp308.chatroom.converter.post;

import static dayp308.chatroom.util.MarkdownUtil.getThumbnails;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import dayp308.chatroom.entity.view.post.PostMinimalView;
import dayp308.chatroom.repository.projection.PostProjection;

@Component
public class PostToMinimalView implements Converter<PostProjection, PostMinimalView>{

    @Override
    public PostMinimalView convert(PostProjection source) {
        PostMinimalView target = new PostMinimalView();
        BeanUtils.copyProperties(source, target);
        var thumbnails = getThumbnails(source.content(), 1);
        if (!thumbnails.isEmpty())
            target.setThumbnail(thumbnails.get(0));
        return target;
    }
    
}
