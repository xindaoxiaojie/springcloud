package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpitDao extends MongoRepository<Spit,String> {

    /*
    * 吐槽评论
    * */
    public Page<Spit> findByParentid(String parentId,Pageable pageable);
}
