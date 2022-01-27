## Tree

---

#### 基础部分

###### 二叉树的遍历

- 深度优先遍历

1. 前序遍历 (Pre-Order Traversal)：
    - 先访问根，然后访问子树
    - Java 代码：
   ```    
   if (root != null) {
      System.out.print(root.val + " ");
      recursionMiddleorderTraversal(root.left);
      recursionMiddleorderTraversal(root.right);
   }
   ```
2. 中序遍历 (In-Order Traversal)：
    - 先访问左（右）子树，然后访问根，最后访问右（左）子树
    - Java 代码：
   ```    
   if (root != null) {
      recursionMiddleorderTraversal(root.left);
      System.out.print(root.val + " ");
      recursionMiddleorderTraversal(root.right);
   }
   ```
3. 后序遍历 (Post-Order Traversal)：
    - 先访问子树，然后访问根
    - Java 代码：
   ```    
   if (root != null) {
      recursionMiddleorderTraversal(root.left);
      recursionMiddleorderTraversal(root.right);
      System.out.print(root.val + " ");
   }
   ```

- 广度优先遍历

1. 按层次遍历：
    - 先访问离根节点最近的节点
    - 借助队列实现
    - Java 代码：
   ```    
        if (root == null) {
			return;
		}
		LinkedList<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			System.out.print(node.val+"  ");
			if (node.left != null) {
				queue.offer(node.left);
			}
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
   ```

###### 多叉树的遍历

- 深度优先遍历
    - 先访问根结点，后选择一子结点访问并访问该节点的子结点，持续深入后再依序访问其他子树
    - 可以用递归或栈的方式实现

###### 平衡二叉树

- 本质上是特殊的二叉搜索树

- 它具有二叉搜索树所有的特点，此外它有自己的特别的性质：
    1. 它是一棵空树或它的左右两个子树的高度差的绝对值不超过1
    2. 平衡二叉树的左右两个子树都是一棵平衡二叉树。

- 平衡因子：
    - 节点的平衡因子 = 该节点的左子树的高度 - 该节点右子树的高度

- 旋转分类：
    1. 左旋转
    2. 右旋转
    3. 左右旋转
    4. 右左旋转

- 搜索性能：
    - 最差的情况下的也能达到O（logN）

###### 红黑树

自平衡的排序二叉树

###### 红黑树和 AVL 树的区别

1. AVL 树：
    - AVL 树是最早被发明的**自平衡二叉查找树**
    - 在 AVL 树中，任一节点对应的两棵子树的最大高度差为 1，因此它也被称为**高度平衡树**
    - 查找、插入和删除在平均和最坏情况下的时间复杂度都是 O（logN）
    - 增加和删除元素的操作则可能需要借由一次或多次树旋转，以实现树的重新平衡
    - AVL 树得名于它的发明者 G. M. Adelson-Velsky 和 Evgenii Landis
    - AVL 平衡二叉树很好的解决了，二叉搜索树在遇到有序序列性能退化为 O（N）的情况，使得在最坏情况下的搜索效率仍然能够达到 O（logN）
    - **这种优化是牺牲了插入和删除的性能换来的**
    - **AVL 树并不适合需要频繁插入和删除的场景**

2. 红黑树
    - 并不强调严格的平衡性，而是保持一定的平衡性
    - 从而使得在搜索，插入，删除的场景下均有一个不错的速度

---

#### 提高部分

######

---









---

参考链接：

- [树的遍历](https://zh.wikipedia.org/wiki/%E6%A0%91%E7%9A%84%E9%81%8D%E5%8E%86)
- [二叉树遍历(先序、中序、后序)](https://www.jianshu.com/p/456af5480cee)
- [二叉树遍历（前序、中序、后序、层次遍历、深度优先、广度优先）](https://blog.csdn.net/My_Jobs/article/details/43451187)
- [什么是平衡二叉树](https://cloud.tencent.com/developer/article/1419168)

---











