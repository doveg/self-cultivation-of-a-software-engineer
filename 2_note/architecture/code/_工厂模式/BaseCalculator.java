package com.sft.ccs.core.manager.plan;

import java.util.List;

import com.sft.ccs.core.enums.product.RepayMethodEnum;
import com.sft.ccs.core.manager.dto.PrincipalInterestBO;
import com.sft.ccs.support.bean.ApplicationException;
import com.sft.ccs.support.bean.BaseService;

/**
 * 还款方式计算器基类
 */
public abstract class BaseCalculator {

    /**
     * 还款方式对应的计算器
     */
    public static BaseCalculator get(String repayMethod) {
        if (RepayMethodEnum.EQUAL_P_PLUS_I.name().equals(repayMethod)) {
            return new CalculatorEqualPrincipalPlusInterest();
        }

        if (RepayMethodEnum.EQUAL_P_EQUAL_I.name().equals(repayMethod)) {
            return new CalculatorEqualPrincipalEqualInterest();
        }

        throw ApplicationException.serviceError("不支持的还款方式 {}", repayMethod);
    }

    /**
     * 计算本金利息
     *
     * @param amount       贷款金额
     * @param term         贷款期限 (月)
     * @param interestRate 月利率
     * @param roundMethod  整数化方法
     */
    public List<PrincipalInterestBO> calculate(long amount, int term, double interestRate, String roundMethod) {
        List<PrincipalInterestBO> list = this.doCalculate(amount, term, interestRate, roundMethod);

        // 调整首期本金, 使得本金合计等于贷款金额
        long diff = amount - list.stream().mapToLong(PrincipalInterestBO::getPrincipal).sum();
        if (diff != 0L) {
            list.get(0).setPrincipal(list.get(0).getPrincipal() + diff);
        }

        return list;
    }

    /**
     * 计算本金利息
     *
     * @param amount       贷款金额
     * @param term         贷款期限 (月)
     * @param interestRate 月利率
     * @param roundMethod  整数化方法
     */
    abstract List<PrincipalInterestBO> doCalculate(long amount, int term, double interestRate, String roundMethod);

}
