import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 按期还款申请响应
 */
@Getter
@Setter
public class RepayApplyResponse {

    // 受理记录数
    private Integer acceptNumber;

    // 按期还款提交状态
    private String status;

    // 错误描述
    private String errorMsg;

    private List<RepayApplyFailItem> list = new ArrayList<>();


    /**
     * 按期还款申请响应
     */
    @Getter
    @Setter
    public static class RepayApplyFailItem {

        // 借据号
        private String loanNo;

        // 期号
        private String periodTime;

        // 错误描述
        private String errorDesc;

    }

}
