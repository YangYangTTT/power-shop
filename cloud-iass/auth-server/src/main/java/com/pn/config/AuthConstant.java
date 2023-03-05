package com.pn.config;

/*
  常量类:
  其实是个接口,接口中定义的全是全局常量;
  因为常量类中的常量值可以能会被修改,所以就将这些常量定义在一个常量类中,需要修改时只需要在常量类中修改即可;
 */
public interface AuthConstant {

    //区分前台系统用户和后台系统用户的请求头的常量
    public static final String LOGIN_TYPE = "loginType";

    //标记前台系统用户的请求头的值的常量
    public static final String USER = "user";

    //标记后台系统用户的请求头的值的常量
    public static final String SYS_USER = "sysUser";
}
