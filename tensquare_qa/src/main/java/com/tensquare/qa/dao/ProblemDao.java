package com.tensquare.qa.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query("SELECT p FROM Problem p WHERE p.id IN(SELECT pl.problemid FROM PL pl WHERE pl.labelid=?1) ORDER BY p.replytime DESC")
    public Page<Problem> findNewListByLabelId(String labelid, Pageable pageable);

    @Query("SELECT p FROM Problem p WHERE p.id IN(SELECT pl.problemid FROM PL pl WHERE pl.labelid=?1) ORDER BY p.reply DESC")
    public Page<Problem> findHotListByLabelId(String labelId, Pageable pageable);

    @Query("SELECT p FROM Problem p WHERE p.id IN(SELECT pl.problemid FROM PL pl WHERE pl.labelid=?1) and p.reply=0 order by p.createtime desc")
    public Page<Problem> findWaitListByLabelId(String labelId, Pageable pageable);



}

