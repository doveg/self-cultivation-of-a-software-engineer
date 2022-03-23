@Data
@NoArgsConstructor
public class MenuTreeOptimise {

    //获取菜单树
    public List<Menu> getMenuList() {

        // 寻找根节点

        // list 转 map，parentId 作为 key
        Map<Long, User> maps = menuList.stream().collect(Collectors.toMap(User::getId, Function.identity(), (key1, key2) -> key2));

        // 遍历设置子节点

    }

}
