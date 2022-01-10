package com.kirito.kiritomall.ware.vo;

import lombok.Data;

@Data
public class PurchaseDetailDoneVo {
    private Long itemId; //采购项id
    private Integer status;
    private String reason; //采购失败原因
}
