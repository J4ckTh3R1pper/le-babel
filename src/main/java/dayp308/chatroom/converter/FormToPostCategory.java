package dayp308.chatroom.converter;

import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToPostCategory implements Converter<CategoryCreationForm, PostCategory> {
    @Override
    public PostCategory convert(CategoryCreationForm source) {
        PostCategory target = new PostCategory();
        target.setCategoryName(source.getCategoryName());
        target.setInfo(source.getCategoryInfo());
        target.setAvatar(source.getAvatar());
        return target;
    }
}
