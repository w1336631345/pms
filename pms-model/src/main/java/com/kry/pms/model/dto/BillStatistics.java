package com.kry.pms.model.dto;

import lombok.Data;

@Data
public class BillStatistics {
    Double cost;
    Double pay;

    public BillStatistics(Double cost, Double pay) {
        this.cost = cost;
        this.pay = pay;
    }
}
