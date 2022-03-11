# Design Pattern / 设计模式

---

## 基础

---

### 设计模式的定义

设计模式：是众多软件开发人员经过长时间的试错和应用总结出来的，解决特定问题的一系列方案。

### 策略模式

策略模式：定义了一系列的算法，并将每一个算法封装起来，使它们可以相互替换。

策略模式通常包含以下角色：

> 抽象策略（Strategy）类：定义了一个公共接口，各种不同的算法以不同的方式实现这个接口，环境角色使用这个接口调用不同的算法，一般使用接口或抽象类实现。
>
> 具体策略（Concrete Strategy）类：实现了抽象策略定义的接口，提供具体的算法实现。
>
> 环境（Context）类：持有一个策略类的引用，最终给客户端调用。

### 适配器模式

适配器模式：将一个类的接口转换成客户希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类能一起工作。

适配器模式包含以下主要角色：

> 目标（Target）接口：当前系统业务所期待的接口，它可以是抽象类或接口。
>
> 适配者（Adaptee）类：它是被访问和适配的现存组件库中的组件接口。
>
> 适配器（Adapter）类：它是一个转换器，通过继承或引用适配者的对象，把适配者接口转换成目标接口，让客户按目标接口的格式访问适配者。

### 单例模式

单例模式：属于创建型模式，它提供了一种创建对象的最佳方式。

这种模式涉及到一个单一的类，该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。

### 状态模式

状态模式：对有状态的对象，把复杂的 “判断逻辑” 提取到不同的状态对象中，允许状态对象在其内部状态发生改变时改变其行为。

状态模式包含以下主要角色：

> 环境类（Context）角色：也称为上下文，它定义了客户端需要的接口，内部维护一个当前状态，并负责具体状态的切换。
>
> 抽象状态（State）角色：定义一个接口，用以封装环境对象中的特定状态所对应的行为，可以有一个或多个行为。
>
> 具体状态（Concrete State）角色：实现抽象状态所对应的行为，并且在需要的情况下进行状态切换。

### 观察者模式

观察者模式：指多个对象间存在一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。这种模式有时又称作发布 - 订阅模式、模型 - 视图模式，它是对象行为型模式。

观察者模式的主要角色如下：

> 抽象主题（Subject）角色：也叫抽象目标类，它提供了一个用于保存观察者对象的聚集类和增加、删除观察者对象的方法，以及通知所有观察者的抽象方法。
>
> 具体主题（Concrete Subject）角色：也叫具体目标类，它实现抽象目标中的通知方法，当具体主题的内部状态发生改变时，通知所有注册过的观察者对象。
>
> 抽象观察者（Observer）角色：它是一个抽象类或接口，它包含了一个更新自己的抽象方法，当接到具体主题的更改通知时被调用。
>
> 具体观察者（Concrete Observer）角色：实现抽象观察者中定义的抽象方法，以便在得到目标的更改通知时更新自身的状态。

### 建造者模式

建造者模式：指将一个复杂对象的构造与它的表示分离，使同样的构建过程可以创建不同的表示。它是将一个复杂的对象分解为多个简单的对象，然后一步一步构建而成。它将变与不变相分离，即产品的组成部分是不变的，但每一部分是可以灵活选择的。

建造者模式的主要角色如下:

> 产品角色（Product）：它是包含多个组成部件的复杂对象，由具体建造者来创建其各个零部件。
>
> 抽象建造者（Builder）：它是一个包含创建产品各个子部件的抽象方法的接口，通常还包含一个返回复杂产品的方法 getResult ()。
>
> 具体建造者 (Concrete Builder）：实现 Builder 接口，完成复杂产品的各个部件的具体创建方法。
>
> 指挥者（Director）：它调用建造者对象中的部件构造与装配方法完成复杂对象的创建，在指挥者中不涉及具体产品的信息。

### 装饰器模式

装饰器模式：指在不改变现有对象结构的情况下，动态地给该对象增加一些职责（即增加其额外功能）的模式，它属于对象结构型模式。

装饰器模式主要包含以下角色：

> 抽象构件（Component）角色：定义一个抽象接口以规范准备接收附加责任的对象。
>
> 具体构件（ConcreteComponent）角色：实现抽象构件，通过装饰角色为其添加一些职责。
>
> 抽象装饰（Decorator）角色：继承抽象构件，并包含具体构件的实例，可以通过其子类扩展具体构件的功能。
>
> 具体装饰（ConcreteDecorator）角色：实现抽象装饰的相关方法，并给具体构件对象添加附加的责任。


---

## 应用

---

### 奖励的发放策略

假设现在就要做一个营销，需要用户参与一个活动，然后完成一系列的任务，最后可以得到一些奖励作为回报。活动的奖励包含美团外卖、酒旅和美食等多种品类券，现在需要你帮忙设计一套奖励发放方案。

无设计版本：

```java
// 奖励服务
class RewardService {
    // 外部服务
    private WaimaiService waimaiService;
    private HotelService hotelService;
    private FoodService foodService;

    // 使用对入参的条件判断进行发奖
    public void issueReward(String rewardType, Object... params) {
        if ("Waimai".equals(rewardType)) {
            WaimaiRequest request = new WaimaiRequest();
            // 构建入参
            request.setWaimaiReq(params);
            waimaiService.issueWaimai(request);
        } else if ("Hotel".equals(rewardType)) {
            HotelRequest request = new HotelRequest();
            request.addHotelReq(params);
            hotelService.sendPrize(request);
        } else if ("Food".equals(rewardType)) {
            FoodRequest request = new FoodRequest(params);
            foodService.getCoupon(request);
        } else {
            throw new IllegalArgumentException("rewardType error!");
        }
    }
}
```

这段代码有两个主要问题：

> 一是不符合开闭原则，可以预见，如果后续新增品类券的话，需要直接修改主干代码，而我们提倡代码应该是对修改封闭的；
>
> 二是不符合迪米特法则，发奖逻辑和各个下游接口高度耦合，这导致接口的改变将直接影响到代码的组织，使得代码的可维护性降低。

使用策略模式和适配器模式来优化：

```java
// 策略接口
interface Strategy {
    void issue(Object... params);
}

// 外卖策略
class Waimai implements Strategy {
    private WaimaiService waimaiService;

    @Override
    public void issue(Object... params) {
        WaimaiRequest request = new WaimaiRequest();
        // 构建入参
        request.setWaimaiReq(params);
        waimaiService.issueWaimai(request);
    }
}

// 酒旅策略
class Hotel implements Strategy {
    private HotelService hotelService;

    @Override
    public void issue(Object... params) {
        HotelRequest request = new HotelRequest();
        request.addHotelReq(params);
        hotelService.sendPrize(request);
    }
}

// 美食策略
class Food implements Strategy {
    private FoodService foodService;

    @Override
    public void issue(Object... params) {
        FoodRequest request = new FoodRequest(params);
        foodService.payCoupon(request);
    }
}
```

```java
// 使用分支判断获取的策略上下文
class StrategyContext {
    public static Strategy getStrategy(String rewardType) {
        switch (rewardType) {
            case "Waimai":
                return new Waimai();
            case "Hotel":
                return new Hotel();
            case "Food":
                return new Food();
            default:
                throw new IllegalArgumentException("rewardType error!");
        }
    }
}

// 优化后的策略服务
class RewardService {
    public void issueReward(String rewardType, Object... params) {
        Strategy strategy = StrategyContext.getStrategy(rewardType);
        strategy.issue(params);
    }
}
```

使用饿汉式单例模式去优化策略类：

```java
// 策略上下文，用于管理策略的注册和获取
class StrategyContext {
    private static final Map<String, Strategy> registerMap = new HashMap<>();

    // 注册策略
    public static void registerStrategy(String rewardType, Strategy strategy) {
        registerMap.putIfAbsent(rewardType, strategy);
    }

    // 获取策略
    public static Strategy getStrategy(String rewardType) {
        return registerMap.get(rewardType);
    }
}

// 抽象策略类
abstract class AbstractStrategy implements Strategy {
    // 类注册方法
    public void register() {
        StrategyContext.registerStrategy(getClass().getSimpleName(), this);
    }
}

// 单例外卖策略
class Waimai extends AbstractStrategy implements Strategy {
    private static final Waimai instance = new Waimai();
    private WaimaiService waimaiService;

    private Waimai() {
        register();
    }

    public static Waimai getInstance() {
        return instance;
    }

    @Override
    public void issue(Object... params) {
        WaimaiRequest request = new WaimaiRequest();
        // 构建入参
        request.setWaimaiReq(params);
        waimaiService.issueWaimai(request);
    }
}

// 单例酒旅策略
class Hotel extends AbstractStrategy implements Strategy {
    private static final Hotel instance = new Hotel();
    private HotelService hotelService;

    private Hotel() {
        register();
    }

    public static Hotel getInstance() {
        return instance;
    }

    @Override
    public void issue(Object... params) {
        HotelRequest request = new HotelRequest();
        request.addHotelReq(params);
        hotelService.sendPrize(request);
    }
}

// 单例美食策略
class Food extends AbstractStrategy implements Strategy {
    private static final Food instance = new Food();
    private FoodService foodService;

    private Food() {
        register();
    }

    public static Food getInstance() {
        return instance;
    }

    @Override
    public void issue(Object... params) {
        FoodRequest request = new FoodRequest(params);
        foodService.payCoupon(request);
    }
}
```

### 任务模型的设计

定义了一套任务状态的枚举和行为的枚举：

```java
// 任务状态枚举
@AllArgsConstructor
@Getter
enum TaskState {
    INIT("初始化"),
    ONGOING("进行中"),
    PAUSED("暂停中"),
    FINISHED("已完成"),
    EXPIRED("已过期");
    private final String message;
}

// 行为枚举
@AllArgsConstructor
@Getter
enum ActionType {
    START(1, "开始"),
    STOP(2, "暂停"),
    ACHIEVE(3, "完成"),
    EXPIRE(4, "过期");
    private final int code;
    private final String message;
}
```

对开始编写状态变更功能：

```java
class Task {
    private Long taskId;
    // 任务的默认状态为初始化
    private TaskState state = TaskState.INIT;
    // 活动服务
    private ActivityService activityService;
    // 任务管理器
    private TaskManager taskManager;

    // 使用条件分支进行任务更新
    public void updateState(ActionType actionType) {
        if (state == TaskState.INIT) {
            if (actionType == ActionType.START) {
                state = TaskState.ONGOING;
            }
        } else if (state == TaskState.ONGOING) {
            if (actionType == ActionType.ACHIEVE) {
                state = TaskState.FINISHED;
                // 任务完成后进对外部服务进行通知
                activityService.notifyFinished(taskId);
                taskManager.release(taskId);
            } else if (actionType == ActionType.STOP) {
                state = TaskState.PAUSED;
            } else if (actionType == ActionType.EXPIRE) {
                state = TaskState.EXPIRED;
            }
        } else if (state == TaskState.PAUSED) {
            if (actionType == ActionType.START) {
                state = TaskState.ONGOING;
            } else if (actionType == ActionType.EXPIRE) {
                state = TaskState.EXPIRED;
            }
        }
    }
}
```

在上述的实现中，updateState方法完成了 2 个重要的功能：

> 接收不同的行为，然后更新当前任务的状态；
>
> 当任务过期时，通知任务所属的活动和任务管理器。


用状态模式来优化任务类的实现：

```java
// 任务状态抽象接口
interface State {
    // 默认实现，不做任何处理
    default void update(Task task, ActionType actionType) {
        // do nothing
    }
}

// 任务初始状态
class TaskInit implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.START) {
            task.setState(new TaskOngoing());
        }
    }
}

// 任务进行状态
class TaskOngoing implements State {
    private ActivityService activityService;
    private TaskManager taskManager;

    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.ACHIEVE) {
            task.setState(new TaskFinished());
            // 通知
            activityService.notifyFinished(taskId);
            taskManager.release(taskId);
        } else if (actionType == ActionType.STOP) {
            task.setState(new TaskPaused());
        } else if (actionType == ActionType.EXPIRE) {
            task.setState(new TaskExpired());
        }
    }
}

// 任务暂停状态
class TaskPaused implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.START) {
            task.setState(new TaskOngoing());
        } else if (actionType == ActionType.EXPIRE) {
            task.setState(new TaskExpired());
        }
    }
}

// 任务完成状态
class TaskFinished implements State {

}

// 任务过期状态
class TaskExpired implements State {

}

@Data
class Task {
    private Long taskId;
    // 初始化为初始态
    private State state = new TaskInit();

    // 更新状态
    public void updateState(ActionType actionType) {
        state.update(this, actionType);
    }
}
```

首先设计好抽象目标和抽象观察者，然后将活动和任务管理器的接收通知功能定制成具体观察者：

```java
// 抽象观察者
interface Observer {
    void response(Long taskId); // 反应
}

// 抽象目标
abstract class Subject {
    protected List<Observer> observers = new ArrayList<Observer>();

    // 增加观察者方法
    public void add(Observer observer) {
        observers.add(observer);
    }

    // 删除观察者方法
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    // 通知观察者方法
    public void notifyObserver(Long taskId) {
        for (Observer observer : observers) {
            observer.response(taskId);
        }
    }
}

// 活动观察者
class ActivityObserver implements Observer {
    private ActivityService activityService;

    @Override
    public void response(Long taskId) {
        activityService.notifyFinished(taskId);
    }
}

// 任务管理观察者
class TaskManageObserver implements Observer {
    private TaskManager taskManager;

    @Override
    public void response(Long taskId) {
        taskManager.release(taskId);
    }
}
```

将任务进行状态类优化成使用通用的通知方法，并在任务初始态执行状态流转时定义任务进行态所需的观察者：

```java
// 任务进行状态
class TaskOngoing extends Subject implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.ACHIEVE) {
            task.setState(new TaskFinished());
            // 通知
            notifyObserver(task.getTaskId());
        } else if (actionType == ActionType.STOP) {
            task.setState(new TaskPaused());
        } else if (actionType == ActionType.EXPIRE) {
            task.setState(new TaskExpired());
        }
    }
}

// 任务初始状态
class TaskInit implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.START) {
            TaskOngoing taskOngoing = new TaskOngoing();
            taskOngoing.add(new ActivityObserver());
            taskOngoing.add(new TaskManageObserver());
            task.setState(taskOngoing);
        }
    }
}
```

### 活动的迭代重构

活动模型的特点在于其组成部分较多，小明原先的活动模型的构建方式是这样的：

```java
// 抽象活动接口
interface ActivityInterface {
    void participate(Long userId);
}

// 活动类
class Activity implements ActivityInterface {
    private String type;
    private Long id;
    private String name;
    private Integer scene;
    private String material;

    public Activity(String type) {
        this.type = type;
        // id的构建部分依赖于活动的type
        if ("period".equals(type)) {
            id = 0L;
        }
    }

    public Activity(String type, Long id) {
        this.type = type;
        this.id = id;
    }

    public Activity(String type, Long id, Integer scene) {
        this.type = type;
        this.id = id;
        this.scene = scene;
    }

    public Activity(String type, String name, Integer scene, String material) {
        this.type = type;
        this.scene = scene;
        this.material = material;
        // name的构建完全依赖于活动的type
        if ("period".equals(type)) {
            this.id = 0L;
            this.name = "period" + name;
        } else {
            this.name = "normal" + name;
        }
    }

    // 参与活动
    @Override
    public void participate(Long userId) {
        // do nothing
    }
}

// 任务型活动
class TaskActivity extends Activity {
    private Task task;

    public TaskActivity(String type, String name, Integer scene, String material, Task task) {
        super(type, name, scene, material);
        this.task = task;
    }

    // 参与任务型活动
    @Override
    public void participate(Long userId) {
        // 更新任务状态为进行中
        task.getState().update(task, ActionType.START);
    }
}
```

经过自主分析，小明发现活动的构造不够合理，主要问题表现在：

> 活动的构造组件较多，导致可以组合的构造函数太多，尤其是在模型增加字段时还需要去修改构造函数；
>
> 部分组件的构造存在一定的顺序关系，但是当前的实现没有体现顺序，导致构造逻辑比较混乱，并且存在部分重复的代码。

可以使用创建型模式中的建造者模式去做重构：

可以通过在活动里面实现静态的建造者类来简易地实现：

```java
// 活动类
class Activity implements ActivityInterface {
    protected String type;
    protected Long id;
    protected String name;
    protected Integer scene;
    protected String material;

    // 全参构造函数
    public Activity(String type, Long id, String name, Integer scene, String material) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.scene = scene;
        this.material = material;
    }

    @Override
    public void participate(Long userId) {
        // do nothing
    }

    // 静态建造器类，使用奇异递归模板模式允许继承并返回继承建造器类
    public static class Builder<T extends Builder<T>> {
        protected String type;
        protected Long id;
        protected String name;
        protected Integer scene;
        protected String material;

        public T setType(String type) {
            this.type = type;
            return (T) this;
        }

        public T setId(Long id) {
            this.id = id;
            return (T) this;
        }

        public T setId() {
            if ("period".equals(this.type)) {
                this.id = 0L;
            }
            return (T) this;
        }

        public T setScene(Integer scene) {
            this.scene = scene;
            return (T) this;
        }

        public T setMaterial(String material) {
            this.material = material;
            return (T) this;
        }

        public T setName(String name) {
            if ("period".equals(this.type)) {
                this.name = "period" + name;
            } else {
                this.name = "normal" + name;
            }
            return (T) this;
        }

        public Activity build() {
            return new Activity(type, id, name, scene, material);
        }
    }
}

// 任务型活动
class TaskActivity extends Activity {
    protected Task task;

    // 全参构造函数
    public TaskActivity(String type, Long id, String name, Integer scene, String material, Task task) {
        super(type, id, name, scene, material);
        this.task = task;
    }

    // 参与任务型活动
    @Override
    public void participate(Long userId) {
        // 更新任务状态为进行中
        task.getState().update(task, ActionType.START);
    }

    // 继承建造器类
    public static class Builder extends Activity.Builder<Builder> {
        private Task task;

        public Builder setTask(Task task) {
            this.task = task;
            return this;
        }

        public TaskActivity build() {
            return new TaskActivity(type, id, name, scene, material, task);
        }
    }
}
```

重构完活动构建的设计后，小明开始对参加活动方法增加风控。最简单的方式肯定是直接修改目标方法：

```
public void participate(Long userId) {
    // 对目标用户做风险控制，失败则抛出异常
    Risk.doControl(userId);
    // 更新任务状态为进行中
    task.state.update(task, ActionType.START);
}
```

装饰器模式后，新的代码就变成了这样：

```java
// 抽象装饰角色
abstract class ActivityDecorator implements ActivityInterface {
    protected ActivityInterface activity;

    public ActivityDecorator(ActivityInterface activity) {
        this.activity = activity;
    }

    public abstract void participate(Long userId);
}

// 能够对活动做风险控制的包装类
class RiskControlDecorator extends ActivityDecorator {
    public RiskControlDecorator(ActivityInterface activity) {
        super(activity);
    }

    @Override
    public void participate(Long userId) {
        // 对目标用户做风险控制，失败则抛出异常
        Risk.doControl(userId);
        // 更新任务状态为进行中
        activity.participate(userId);
    }
}
```

---






---

参考链接：

- [设计模式二三事](https://mp.weixin.qq.com/s/H2toewJKEwq1mXme_iMWkA)
- []()

---







