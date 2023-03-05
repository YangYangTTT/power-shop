package vo;

import entily.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuVo {
//存储用户菜单树的List -- 名字必须叫menuList
    private  List<SysMenu> menuList;

    //存储用户权限的List -- 名字必须叫authorities
    private List<String> authorities;

}
