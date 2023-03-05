package com.pn.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pn.entily.UserAddr;
import com.pn.entily.UserCollection;
import com.pn.service.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/p/collection")
@RestController
public class UserCollectionController {

    //注入IUserCollectionService
    @Autowired
    private UserCollectionService userCollectionService;

    //url接口/p/collection/count的请求处理方法
    @GetMapping("/count")
    public ResponseEntity<Integer>getUserCollectionCount(){
        //拿到当前登录的用户名(我们存的是userId)
        String userId  = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        //从user_collection表中查询user_id列的值为当前登录用户的userId的总行数
        int count = userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
        );
        return ResponseEntity.ok(count);
    }
    //-------------------查询收藏的方法-------------------------------------------------------------------

    @GetMapping("/isCollection")
    public ResponseEntity<Boolean> isCollection(Long prodId) {
        //拿到当前登录的用户名(我们存的userId)
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();
        //从user_collection表中根据user_id列(用户id)和prod_id列(商品id)查询记录行数
        int count = userCollectionService.count(new LambdaQueryWrapper<UserCollection>()
                .eq(UserCollection::getUserId, userId)
                .eq(UserCollection::getProdId, prodId)
        );
        //如果记录行数大于0说明商品被用户收藏,反之没被收藏
        return ResponseEntity.ok(count > 0);
    }

    //--------------------------------------收藏 和取消收藏的方法---------------------------------------
    @PostMapping("/addOrCancel")
    public ResponseEntity<Void>addOrCancel(@RequestBody  Long prodId){
          //拿到当前登录用户id
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        //执行业务方法
        userCollectionService.addOrCancel(userId, prodId);
        return  ResponseEntity.ok().build();


    }
}
