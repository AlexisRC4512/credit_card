package com.nttdata.credit_card.model.response;

import com.nttdata.credit_card.model.enums.TransactionType;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResponse {
    private TransactionType type;
    private double amount;
    private Date date;
    private String description;
}
