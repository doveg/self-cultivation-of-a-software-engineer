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
