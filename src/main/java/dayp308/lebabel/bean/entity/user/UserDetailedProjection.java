package dayp308.lebabel.bean.entity.user;

import dayp308.lebabel.enumeration.Gender;

import java.time.Instant;


public interface UserDetailedProjection {
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
