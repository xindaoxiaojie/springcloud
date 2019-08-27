package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tensquare.user.pojo.Admin;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	/*
	 * 普通用户登录
	 * */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		User user1 = userService.findByMobile(user.getMobile());
		if(user1==null){
			return new Result(false,StatusCode.ERROR,"该用户不存在");
		}
		boolean matches = bCryptPasswordEncoder.matches(user.getPassword(), user1.getPassword());
		if(!matches){
			return new Result(false,StatusCode.ACCESSERROR,"密码错误");
		}else {
			String token = jwtUtil.createJWT(user1.getId(),user1.getNickname(),"user");
			HashMap<Object, Object> map = new HashMap<>();
			map.put("token",token);
			return new Result(true,StatusCode.OK,"登录成功",map);
		}
	}
	/*
	* 修改用户粉丝数
	* */
	@RequestMapping(value = "/incfans/{userid}/{x}",method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod ="fbincFans" )
	public int incFanscount(@PathVariable String userid ,@PathVariable int x){
		//int i=1/0;
		userService.incFanscount(userid,x);
		return 1;
	}
	public int fbincFans(@PathVariable String userid ,@PathVariable int x){
		//int i=1/0;
		//throw new RuntimeException("添加粉丝失败");
		return 0;
	}
	/*
	 * 修改用户关注数
	 * */
	@RequestMapping(value = "/incfollow/{userid}/{x}",method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod ="fbincFoll" )
	public int incFollowcount(@PathVariable String userid ,@PathVariable int x){
		//int i=1/0;
		userService.incFollowcount(userid,x);
		return 1;
	}
	public int  fbincFoll(@PathVariable String userid ,@PathVariable int x){
		//int i=1/0;
		//throw new RuntimeException("添加关注失败");
		return 0;
	}
	/*
	* 短信发送
	* */
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"短信发送成功");
	}
	/*
	* 注册
	* */
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result register(@RequestBody User user,@PathVariable String code){
		if(code==null||"".equals(code)){
			return new Result(false,StatusCode.ERROR,"请输入验证码");
		}
		String rcode = (String) redisTemplate.opsForValue().get(user.getMobile());
		if(!code.equals(rcode)){
			return new Result(false,StatusCode.ERROR,"请输入正确的验证码");
		}
		userService.add(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		user.setPassword(encoder.encode(user.getPassword()));
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		/*String auth=request.getHeader("Authorization");
		if(auth==null){
			return new Result(false,StatusCode.ACCESSERROR,"请先登录");
		}
		if(!auth.startsWith("Bearer ")){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		String token = auth.substring(7);
		Claims claims = jwtUtil.parseJWT(token);
		String roles = (String) claims.get("roles");
		if(roles==null||!roles.equals("admin")){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}*/
		Claims claims = (Claims)request.getAttribute("admin_claims");
		if(claims==null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
