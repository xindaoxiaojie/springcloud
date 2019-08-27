package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;
    /*
    * 查询所有
    * */
    public List<Label> findAll(){
        return labelDao.findAll();
    }
    /*
    * 多条件查询
    * */
    public List<Label> findAll(Map map){
        Specification specification=createSpecification(map);
        return labelDao.findAll(specification);
    }
    /*
    * 多条件加分页查询
    * */
    public Page<Label> findAll(Map map,int page,int size){
        Pageable pageable = PageRequest.of(page - 1, 2);
        Specification specification=createSpecification(map);
        return labelDao.findAll(specification,pageable);
    }
    private Specification createSpecification(Map map){
        Specification specification=new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ArrayList<Object> list = new ArrayList<>();
                if(map.get("labelname")!=null&&!"".equals(map.get("labelname"))){
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + map.get("labelname") + "%");
                    list.add(p1);
                }
                if(map.get("state")!=null&&!"".equals(map.get("state"))){
                    Predicate p2 = criteriaBuilder.equal(root.get("state").as(String.class), map.get("state"));
                    list.add(p2);
                }
                if(map.get("recommend")!=null&&!"".equals(map.get("recommend"))){
                    Predicate p3 = criteriaBuilder.equal(root.get("recommend").as(String.class), map.get("recommend"));
                    list.add(p3);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return specification;
    }
    /*
    * 根据id查询
    * */
    public Label findById(String id){
        Optional<Label> label = labelDao.findById(id);
        return label.isPresent()?label.get():null;
    }
    /*
    * 保存
    * */
    public void save(Label label){
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }
    /*
    * 更新
    * */
    public void update(Label label){
        labelDao.save(label);
    }
    /*
    * 删除
    * */
    public void delete(String id){
        labelDao.deleteById(id);
    }
}
