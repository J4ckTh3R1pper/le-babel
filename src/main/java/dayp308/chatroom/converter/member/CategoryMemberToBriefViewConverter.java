package dayp308.chatroom.converter.member;

import dayp308.chatroom.entity.member.CategoryMember;
import dayp308.chatroom.entity.view.UserBriefView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryMemberToBriefViewConverter implements Converter<CategoryMember, UserBriefView> {
    @Override
    public UserBriefView convert(CategoryMember source) {
        UserBriefView target = new UserBriefView();
        target.setUserId(source.getUser().getId());
        target.setRole(source.getRole().toString());
        target.setNickName(source.getUser().getNickName());
        target.setHeadImgUrl(source.getUser().getHeadImgUrl());
        target.setLocation(source.getUser().getLocation());
        target.setLevel(source.getExperience() / 100);
        target.setTitle(source.getTitle());

        return target;
    }
}
