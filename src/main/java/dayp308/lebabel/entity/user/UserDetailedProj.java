package dayp308.lebabel.entity.user;

import dayp308.lebabel.entity.enums.Gender;

import java.time.Instant;


public interface UserDetailedProj {
    Long getId();
    String getHeadImgUrl();
    String getLocation();
    String getIntroduce();
    Boolean getLocked();
    String getNickName();
    Gender getGender();
    Instant getLastLoginTime();
    Instant getCreateTime();
    Long getFollowingCount();
    Long getFollowerCount();
}
