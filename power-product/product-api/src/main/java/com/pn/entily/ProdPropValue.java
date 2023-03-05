package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "prod_prop_value")
public class ProdPropValue implements Serializable {
    /**
     * 属性值ID
     */
    @TableId(value = "value_id", type = IdType.INPUT)
    private Long valueId;

    /**
     * 属性值名称
     */
    @TableField(value = "prop_value")
    private String propValue;

    /**
     * 属性ID
     */
    @TableField(value = "prop_id")
    private Long propId;

    private static final long serialVersionUID = 1L;
}