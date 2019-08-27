package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface NoFriendDao extends JpaRepository<NoFriend,String>,JpaSpecificationExecutor<NoFriend> {
    @Query("select count(f) from NoFriend f where f.userid=?1 and f.friendid=?2 ")
    public int selectCount(String userid,String friendid);
}
