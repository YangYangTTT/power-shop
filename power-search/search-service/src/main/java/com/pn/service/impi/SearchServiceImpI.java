package com.pn.service.impi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pn.entily.EsProd;
import com.pn.service.SearchService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchServiceImpI implements SearchService {


    //注入es模板
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    //-----------------------------------------  根据商品标签分组id从ES中分页查询该分组下的商品的业务方法;----------------------------------------
    @Override
    public Page<EsProd> searchProdByTagId(Long tagId, Integer current, Integer size) {
        //构建match条件对象--通过tagList字段(商品所有标签分组id)根据商品标签分组id查询商品
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("tagList", tagId);
        //创建条件组装器并组装条件
        NativeSearchQuery positiveRating = new NativeSearchQueryBuilder().withQuery(matchQueryBuilder)
                //ES分页查询起始页是从0开始的,所以是分页查询current-1页,每页size行
                .withPageable(PageRequest.of(current - 1, size))
                //按照positiveRating字段(好评率)降序排序
                .withSort(SortBuilders.fieldSort("positiveRating").order(SortOrder.DESC))
                .build();
        //查询
        SearchHits<EsProd> search = elasticsearchRestTemplate.search(positiveRating, EsProd.class);
          /*
          将查询结果组装到Page对象
         */
        //创建Page对象并设置页码和每页行数
        Page<EsProd> page = new Page<>(current, size);
        //设置总行数
        page.setTotal(search.getTotalHits());
        //设置分页查询到的数据List<EsProd>
        List<EsProd> esProds = new ArrayList<>();
        List<SearchHit<EsProd>> searchHits = search.getSearchHits();
        for(SearchHit<EsProd> searchHit:searchHits){
            EsProd content = searchHit.getContent();
            esProds.add(content);
        }
        page.setRecords(esProds);
        return page;
    }
//------------------------------------------------------------ 根据商品名称从ES中分页查询商品的业务方法------------------------------------------
    @Override
    public Page<EsProd> searchProdByProdName(String prodName, Integer current, Integer size, Integer sort) {
        //创建条件组装器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //组装分页条件(这次页码是从0开始传的,所以不用-1)
        nativeSearchQueryBuilder  .withPageable(PageRequest.of(current,size));
        //组装排序条件
        nativeSearchQueryBuilder  .withSort(getSortBuilder(sort));
                //判断是否有prodName 即搜索的商品名称
            if(StringUtils.hasText(prodName)){
                //组装match条件
                MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("prodName", prodName);
               nativeSearchQueryBuilder.withQuery(matchQueryBuilder);
            }
        //查询
        SearchHits<EsProd> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), EsProd.class);
             /*
          将查询结果组装到Page对象
         */
        Page<EsProd>page=new Page<>(current,size);
        //设置总行数
        page.setTotal(search.getTotalHits());
        //设置分页查询到的数据List<EsProd>
        List<EsProd>esProds=new ArrayList<>();
        List<SearchHit<EsProd>> searchHits = search.getSearchHits();
        for(SearchHit<EsProd> searchHit:searchHits){
            EsProd content = searchHit.getContent();
            esProds.add(content);
        }
        page.setRecords(esProds);
        return page;



    }
    //根据参数sort获取排序条件的方法
    private SortBuilder getSortBuilder(Integer sort) {
        switch (sort) {
            case 0:
                //sort值为0,则根据好评率倒序排序
                return   SortBuilders.fieldSort("positiveRating").order(SortOrder.DESC);
            case 1:
                //sort值为1,则根据销量倒序排序
                return    SortBuilders.fieldSort("soldNum").order(SortOrder.DESC);
            case 2:
                //sort值为2,则根据价格升序排序
                return     SortBuilders.fieldSort("price").order(SortOrder.ASC);
            default:
                //sort为其它值,默认按照好评率倒序排序
                return   SortBuilders.fieldSort("positiveRating").order(SortOrder.DESC);
        }
    }

}
