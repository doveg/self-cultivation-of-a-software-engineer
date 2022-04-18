package com.sft.ccs.core.manager.plan;

import java.util.ArrayList;
import java.util.List;

import com.sft.ccs.core.enums.product.RoundMethodEnum;
import com.sft.ccs.core.manager.dto.PrincipalInterestBO;

/**
 * 等本等息
 */
public class CalculatorEqualPrincipalEqualInterest extends BaseCalculator {

    @Override
    List<PrincipalInterestBO> doCalculate(long amount, int term, double interestRate, String roundMethod) {
        List<PrincipalInterestBO> list = new ArrayList<>(term);

        // 每月本金 = 贷款金额 / 期数
        long principal = RoundMethodEnum.round(amount * 1.0 / term, roundMethod);
        // 每月利息 = 贷款金额 * 月利率
        long interest = RoundMethodEnum.round(amount * 1.0 * interestRate, roundMethod);

        for (int i = 1; i <= term; i++) {
            list.add(new PrincipalInterestBO(principal, interest));
        }

        return list;
    }

}
