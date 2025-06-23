package dayp308.lebabel.converter.post;

import dayp308.lebabel.bean.entity.category.PostCategory;
import dayp308.lebabel.bean.ao.CategoryCreationForm;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FormToPostCategory implements Converter<CategoryCreationForm, PostCategory> {
    @Override
    public PostCategory convert(CategoryCreationForm source) {
        PostCategory target = new PostCategory();
        target.setName(source.getName());
        target.setInfo(source.getInfo());
        target.setAvatar(source.getAvatar());
        return target;
    }
}
