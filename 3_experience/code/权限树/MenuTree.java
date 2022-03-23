@Data
@NoArgsConstructor
public class MenuTree {

    //获取菜单树
    public List<Menu> getMenuList() {
        //获取所有菜单
        List<Menu> menuList = menuMapper.getMenuList();
        //返回的菜单树列表
        List<Menu> menuTree = new ArrayList<>();
        //  先找到所有的一级菜单,放入菜单树列表中
        for (Menu menu : menuList) {
            if (menu.getPsLevel().equals("0")) {
                menuTree.add(menu);
            }
        }
        //遍历一级菜单，开始查找子菜单
        for (Menu menu : menuTree) {
            menu.setChildren(getChildren(menu.getPsId(),menuList));
        }
        return menuList;
    }

    private List<Menu> getChildren(Short psId, List<Menu> menuList) {
        //  查找二级子菜单
        List<Menu> childList = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getPsPid().equals(psId)) {
                childList.add(menu);
            }
        }
        //递归查找查找二级菜单的子菜单
        for (Menu menu : childList) {
            menu.setChildren(getChildren(menu.getPsId(),menuList));
        }
        return childList;
    }

}
