package com.nttdata.credit_card.model.response;

import com.nttdata.credit_card.model.entity.Transaction;
import lombok.*;

import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResponse {
    private Date date;
    private List<Transaction> transactions;
}
