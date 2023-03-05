package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/*
  参照实体类EsProd创建索引：
  indexName = "prod_index" -- 索引名称为prod_index
  shards = 1 --- 索引存入1个分片
  replicas = 1 --- 分片有1个副本
 */
@Document(indexName = "prod_index", shards = 1, replicas = 1)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsProd {

    //商品id,作为主键,对应的索引字段和属性同名
    @Id
    @Field
    private Long prodId;

    //商品名称,对应的索引字段和属性同名,文本类型,存入和搜索使用的分词模式都是ik_max_word
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String prodName;

    //商品价格
    @Field(type = FieldType.Double)
    private BigDecimal price;

    //商品销量
    @Field(type = FieldType.Long)
    private Long soldNum;

    //商品卖点,对应的索引字段和属性同名,文本类型,存入和搜索使用的分词模式都是ik_max_word
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String brief;

    //商品主图
    @Field(type = FieldType.Text)
    private String pic;

    //商品状态
    @Field(type = FieldType.Integer)
    private Integer status;

    //商品库存
    @Field(type = FieldType.Integer)
    private Long totalStocks;

    //商品分类id
    @Field(type = FieldType.Long)
    private Long categoryId;

    //商品标签分组
    @Field(type = FieldType.Text)
    private List<Long> tagList;

    //商品好评数
    @Field(type = FieldType.Long)
    private Long praiseNumber;

    //商品好评率
    @Field(type = FieldType.Double)
    private BigDecimal positiveRating;
}
