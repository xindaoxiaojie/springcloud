package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;
    @Autowired
    private UserClient userClient;
    /*
    * 添加好友
    * */
    @Transactional
    public int addFriend(String userid,String friendid){
        if (noFriendDao.selectCount(userid,friendid)>0){
            NoFriend noFriend = new NoFriend();
            noFriend.setUserid(userid);
            noFriend.setFriendid(friendid);
            noFriendDao.delete(noFriend);
        }
        if(friendDao.selectCount(userid,friendid)>0){
            return 0;//已经关注过，不需要再添加，返回0
        }
        Friend friend = new Friend();
        friend.setIslike("0");
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friendDao.save(friend);
        if(userClient.incFanscount(friendid, 1)==0||userClient.incFollowcount(userid,1)==0){
            throw new RuntimeException("添加好友失败");
        }
        if(friendDao.selectCount(friendid,userid)>0){
            //如果对方也关注了你，则修改islike为1，表示互关
            friendDao.updataLike(userid,friendid,"1");
            friendDao.updataLike(friendid,userid,"1");
        }
        return 1;//表示执行了操作
    }
    /*
    * 添加为非好友
    * */
    @Transactional
    public void addNoFriend(String userid,String friendid){
        if(friendDao.selectCount(userid,friendid)>0&&friendDao.selectCount(friendid,userid)>0){
            //如果两人之前互关，则一人取关时需要修改另一人的关注islike为0，同时删除本人与他人的关注记录
            friendDao.delFriend(userid,friendid);
            friendDao.updataLike(friendid,userid,"0");
            //userClient.incFollowcount(userid,-1);//关注-1
            //userClient.incFanscount(friendid,-1);//被取关的粉丝-1
            if(userClient.incFanscount(friendid, -1)==0||userClient.incFollowcount(userid,-1)==0){
                throw new RuntimeException("取关好友失败");
            }
        }else if (friendDao.selectCount(userid,friendid)>0&&friendDao.selectCount(friendid,userid)==0){
            //A关注B，B未关注A
            friendDao.delFriend(userid,friendid);
            //userClient.incFollowcount(userid,-1);//关注-1
            //userClient.incFanscount(friendid,-1);//被取关的粉丝-1
            if(userClient.incFanscount(friendid, 1)==0||userClient.incFollowcount(userid,1)==0){
                throw new RuntimeException("取关好友失败");
            }
        }
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
    /*
    * 从好友表中删除记录（并未添加黑名单）
    * */
    @Transactional
    public void delFriend(String userid,String friendid){
        if(friendDao.selectCount(userid,friendid)>0&&friendDao.selectCount(friendid,userid)>0){
            //如果两人之前互关，则一人取关时需要修改另一人的关注islike为0，同时删除本人与他人的关注记录
            friendDao.delFriend(userid,friendid);
            friendDao.updataLike(friendid,userid,"0");
            //userClient.incFollowcount(userid,-1);//关注-1
            //userClient.incFanscount(friendid,-1);//被取关的粉丝-1
            if(userClient.incFanscount(friendid, -1)==0||userClient.incFollowcount(userid,-1)==0){
                throw new RuntimeException("删除好友失败");
            }
        }else if (friendDao.selectCount(userid,friendid)>0&&friendDao.selectCount(friendid,userid)==0){
            //A关注B，B未关注A
            friendDao.delFriend(userid,friendid);
            //userClient.incFollowcount(userid,-1);//关注-1
            //userClient.incFanscount(friendid,-1);//被取关的粉丝-1
            if(userClient.incFanscount(friendid, -1)==0||userClient.incFollowcount(userid,-1)==0){
                throw new RuntimeException("删除好友失败");
            }
        }
    }

    /*public Friend findById(String id) {
        Optional<Friend> byId = friendDao.findById(id);
        return byId.get();
    }*/

    public List<Friend> findAll() {
        List<Friend> list = friendDao.findAll();
        return list;
    }
}
