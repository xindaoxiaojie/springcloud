package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/label")
@CrossOrigin
public class LabelController {
    @Autowired
    private LabelService labelService;
    /*
    * 查询所有
    * */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Label> list = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }
    /*
    * 多条件查询
    * */
    @RequestMapping(method = RequestMethod.GET,value = "/search")
    public Result findAll(@RequestBody Map map){
        List<Label> list=labelService.findAll(map);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }
    /*
    * 多条件分页查询
    * */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findAll(@RequestBody Map map,@PathVariable int page,@PathVariable int size){
        Page<Label> page1 = labelService.findAll(map, page, size);
        PageResult pageResult = new PageResult(page1.getTotalElements(), page1.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
    /*
    * 根据id查询
    * */
    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public Result findById(@PathVariable("id") String id){
        Label label = labelService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",label);
    }
    /*
    * 保存
    * */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,StatusCode.OK,"保存成功");
    }
    /*
    * 更新
    * */
    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public Result update(@RequestBody Label label,@PathVariable String id){
        label.setId(id);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"更新成功");
    }
    /*
    * 删除
    * */
    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
    public Result deleteById(@PathVariable("id") String id){
        labelService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

}
