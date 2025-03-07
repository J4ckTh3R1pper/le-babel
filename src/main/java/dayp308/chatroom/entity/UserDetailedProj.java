package dayp308.chatroom.entity;

import dayp308.chatroom.entity.enums.Gender;

import java.time.Instant;


public interface UserDetailedProj {
    Long getId();
    String getHeadImgUrl();
    String getLocation();
    String getIntroduce();
    Boolean getUserLocked();
    String getNickName();
    Gender getGender();
    Instant getLastLoginTime();
    Instant getCreateTime();
    Long getFollowingCount();
    Long getFollowerCount();
}
