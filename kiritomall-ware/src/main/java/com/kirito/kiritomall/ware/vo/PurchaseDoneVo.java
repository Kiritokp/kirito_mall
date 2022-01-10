package com.kirito.kiritomall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id; // 采购单id
    private List<PurchaseDetailDoneVo> items;  //采购项
}
