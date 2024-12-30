package com.nttdata.credit_card.model.response;

import com.nttdata.credit_card.model.entity.Balance;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceResponse {
    private String clientId;
    private List<Balance> balances;
}
