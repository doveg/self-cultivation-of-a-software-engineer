package com.sft.ccs.core.manager.plan;

import java.util.ArrayList;
import java.util.List;

import com.sft.ccs.core.enums.product.RoundMethodEnum;
import com.sft.ccs.core.manager.dto.PrincipalInterestBO;

/**
 * 等额本息
 */
public class CalculatorEqualPrincipalPlusInterest extends BaseCalculator {

    @Override
    List<PrincipalInterestBO> doCalculate(long amount, int term, double interestRate, String roundMethod) {
        List<PrincipalInterestBO> list = new ArrayList<>(term);

        double tmp = Math.pow(1 + interestRate, term);
        // 每月本金利息 = 贷款金额 * 月利率 * ((1 + 月利率) ^ 期数) / ((1 + 月利率) ^ 期数 - 1)
        long principalInterest = RoundMethodEnum.round(amount * 1.0 * interestRate * tmp / (tmp - 1), roundMethod);

        for (int i = 1; i <= term; i++) {
            // 每月本金 = 贷款金额 * 月利率 * ((1 + 月利率) ^ (当期 - 1)) / ((1 + 月利率) ^ 期数 - 1)
            long principal = RoundMethodEnum.round(amount * 1.0 * interestRate * (Math.pow(1 + interestRate, i - 1)) / (tmp - 1), roundMethod);
            long interest = principalInterest - principal;
            list.add(new PrincipalInterestBO(principal, interest));
        }

        return list;
    }

}
