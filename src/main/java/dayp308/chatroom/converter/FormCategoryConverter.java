package dayp308.chatroom.converter;

import dayp308.chatroom.entity.PostCategory;
import dayp308.chatroom.entity.business.CategoryCreationForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormCategoryConverter implements Converter<CategoryCreationForm, PostCategory> {
    @Override
    public PostCategory convert(CategoryCreationForm source) {
        PostCategory target = new PostCategory();
        target.setCategoryName(source.getCategoryName());
        target.setCategoryInfo(source.getCategoryInfo());
        target.setCategoryAvatar(source.getAvatar());
        return target;
    }
}
