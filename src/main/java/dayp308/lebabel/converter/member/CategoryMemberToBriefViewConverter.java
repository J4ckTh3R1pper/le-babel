package dayp308.lebabel.converter.member;

import dayp308.lebabel.bean.entity.member.CategoryMember;
import dayp308.lebabel.bean.view.UserBriefView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryMemberToBriefViewConverter implements Converter<CategoryMember, UserBriefView> {
    @Override
    public UserBriefView convert(CategoryMember source) {
        UserBriefView target = new UserBriefView();
        target.setId(source.getUser().getId());
        target.setRole(source.getRole().ordinal());
        target.setNickName(source.getUser().getNickName());
        target.setHeadImgUrl(source.getUser().getHeadImgUrl());
        target.setLocation(source.getUser().getLocation());
        target.setLevel(1 + source.getExperience() / 100);
        target.setTitle(source.getTitle());

        return target;
    }
}
