package com.nttdata.credit_card.util;


import com.nttdata.credit_card.model.entity.Balance;
import com.nttdata.credit_card.model.entity.CreditCard;
import com.nttdata.credit_card.model.response.BalanceResponse;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceConverter {
    public static BalanceResponse toBalanceResponse(List<CreditCard> cardList) {
        BalanceResponse balanceResponse = new BalanceResponse();
        List<Balance> listBalances = cardList.stream()
                .map(creditCard -> new Balance(creditCard.getId(), creditCard.getAvailableBalance(), new Date(), creditCard.getType()))
                .collect(Collectors.toList());
        balanceResponse.setBalances(listBalances);
        if (!cardList.isEmpty()) {
            balanceResponse.setClientId(cardList.get(0).getClientId());
        }
        return balanceResponse;
    }

}
