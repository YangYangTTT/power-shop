package com.pn.dao;

import com.pn.entily.EsProd;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/////////////////////////此类在springboot一启动的时候就会创建  索引   一EsPord里有信息
@Repository
public interface  EsProdDao extends ElasticsearchRepository<EsProd,Long> {
}
