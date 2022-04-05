// 策略接口
interface Strategy {
    void issue(Object... params);
}


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


// 优化后的策略服务
class RewardService {
    public void issueReward(String rewardType, Object... params) {
        Strategy strategy = StrategyContext.getStrategy(rewardType);
        strategy.issue(params);
    }
}
