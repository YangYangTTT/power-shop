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
@TableName(value = "area")
public class Area implements Serializable {
    @TableId(value = "area_id", type = IdType.INPUT)
    private Long areaId;

    @TableField(value = "area_name")
    private String areaName;

    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "`level`")
    private Integer level;

    private static final long serialVersionUID = 1L;
}