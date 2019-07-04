package com.z.gateway.service.support;
public class CartDTO {
    /**
     * 商品id
     */
    private String productId;
    /**
     * 商品数量
     */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return "CartDTO [productId=" + productId + ", productQuantity=" + productQuantity + "]";
    }
    
    
}
