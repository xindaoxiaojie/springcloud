package com.tensquare.spit.Controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        Spit spit = spitService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",spit);
    }
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        try {
            spitService.save(spit);
            return new Result(true,StatusCode.OK,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR,"添加失败");
        }
    }
    @RequestMapping(value ="/{id}" ,method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody Spit spit){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        spitService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
    /*
    * 根据父id分页查询吐槽
    * */
    @RequestMapping(value = "/comment/{parentId}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentId,@PathVariable int page,@PathVariable int size){
        Page<Spit> spitPage = spitService.findByParentId(parentId, page, size);
        return new Result(true,StatusCode.OK,"查询成功",spitPage.getContent());
    }
    /*
    * 吐槽点赞
    * */
    @RequestMapping(value = "/thumbup/{spitid}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitid){
        String userId="111";
        if(redisTemplate.opsForValue().get("thumbup_"+userId+"_"+spitid)!=null){
            return new Result(false,StatusCode.REPERROE,"你已经点过赞了");
        }
        spitService.thumbup(spitid);
        redisTemplate.opsForValue().set("thumbup_"+userId+"_"+spitid,spitid);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}
