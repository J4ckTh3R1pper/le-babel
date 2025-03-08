package dayp308.chatroom.converter.post;

import dayp308.chatroom.entity.category.PostCategory;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToPostCategory implements Converter<CategoryCreationForm, PostCategory> {
    @Override
    public PostCategory convert(CategoryCreationForm source) {
        PostCategory target = new PostCategory();
        target.setName(source.getCategoryName());
        target.setInfo(source.getCategoryInfo());
        target.setAvatar(source.getAvatar());
        return target;
    }
}
