package com.pn.entily;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
    * 用户表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class WxUser implements Serializable, UserDetails {
    /**
     * ID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;

    /**
     * 用户昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 用户邮箱
     */
    @TableField(value = "user_mail")
    private String userMail;

    /**
     * 登录密码
     */
    @TableField(value = "login_password")
    private String loginPassword;

    /**
     * 支付密码
     */
    @TableField(value = "pay_password")
    private String payPassword;

    /**
     * 手机号码
     */
    @TableField(value = "user_mobile")
    private String userMobile;

    /**
     * 修改时间
     */
    @TableField(value = "modify_time")
    private Date modifyTime;

    /**
     * 注册时间
     */
    @TableField(value = "user_regtime")
    private Date userRegtime;

    /**
     * 注册IP
     */
    @TableField(value = "user_regip")
    private String userRegip;

    /**
     * 最后登录时间
     */
    @TableField(value = "user_lasttime")
    private Date userLasttime;

    /**
     * 最后登录IP
     */
    @TableField(value = "user_lastip")
    private String userLastip;

    /**
     * 备注
     */
    @TableField(value = "user_memo")
    private String userMemo;

    /**
     * M(男) or F(女)
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 例如：2009-11-27
     */
    @TableField(value = "birth_date")
    private String birthDate;

    /**
     * 头像图片路径
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    private Integer score;

    private static final long serialVersionUID = 1L;


    //------------------实现UserDetails接口的方法--------------------------------------------------------------------------------------------------
    //前台系统用户没有权限,返回空集合
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }


    //对固定密码WECHAT进行加密后返回
    @Override
    public String getPassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return  passwordEncoder.encode("WECHAT");
    }


    //username返回user_id列的值,即为openid,因为openid会存入user表的user_id列
    @Override
    public String getUsername() {
        return userId;
    }

    //用户对象的status属性值为1,则账户未过期,值为0则账户过期
    @Override
    public boolean isAccountNonExpired() {
        return status==1;
    }


    //用户对象的status属性值为1,则账户未冻结,值为0则账户冻结
    @Override
    public boolean isAccountNonLocked() {
        return status==1;
    }


    //用户对象的status属性值为1,则密码未过期,值为0则密码过期
    @Override
    public boolean isCredentialsNonExpired() {
        return status==1;
    }


    //用户对象的status属性值为1,则账户可用,值为0则账户禁用
    @Override
    public boolean isEnabled() {
        return status==1;
    }
}