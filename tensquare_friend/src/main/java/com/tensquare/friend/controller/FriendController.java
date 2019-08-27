package com.tensquare.friend.controller;

import com.netflix.discovery.converters.Auto;
import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserClient userClient;
    /*
    * 添加好友或黑名单
    * */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable("friendid") String friendid,@PathVariable("type") String type){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"无权访问");
        }
        if(type.equals("1")){
            //如果是喜欢
            if(friendService.addFriend(claims.getId(),friendid)==0){
                return new Result(true,StatusCode.REPERROE,"已经添加过了");
            }else if(friendService.addFriend(claims.getId(),friendid)>0){
                return new Result(true,StatusCode.OK,"添加成功");
            }

        }else {
            //若果是0，添加到黑名单
            friendService.addNoFriend(claims.getId(),friendid);
        }
        return new Result(true,StatusCode.OK,"操作成功");
    }
    /*
    * 删除好友
    * */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result remove(@PathVariable String friendid){
        Claims claims =(Claims) request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false,StatusCode.ACCESSERROR,"权限不足");
        }
        friendService.delFriend(claims.getId(),friendid);
        return new Result(true,StatusCode.OK,"操作成功");
    }

}
