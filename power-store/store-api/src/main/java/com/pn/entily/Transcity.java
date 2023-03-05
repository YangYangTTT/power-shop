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
@TableName(value = "transcity")
public class Transcity implements Serializable {
    @TableId(value = "transcity_id", type = IdType.INPUT)
    private Long transcityId;

    /**
     * 运费项id
     */
    @TableField(value = "transfee_id")
    private Long transfeeId;

    /**
     * 城市id
     */
    @TableField(value = "city_id")
    private Long cityId;

    private static final long serialVersionUID = 1L;
}