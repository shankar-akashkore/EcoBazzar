package com.ecobazzar.eco.bazzar.dto;

import java.util.List;
import com.ecobazzar.eco.bazzar.model.CartItem;

public class CartSummaryDto {

    private List<CartItem> items;

    private double totalPrice;

    private double totalCarbonUsed;

    private double totalCarbonSaved;

    private String ecoSuggestion;

    private EcoSwapSuggestion swapSuggestion;

    public CartSummaryDto() {}

    public CartSummaryDto(List<CartItem> items, double totalPrice,
                          double totalCarbonUsed, double totalCarbonSaved,
                          String ecoSuggestion, EcoSwapSuggestion swapSuggestion) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalCarbonUsed = totalCarbonUsed;
        this.totalCarbonSaved = totalCarbonSaved;
        this.ecoSuggestion = ecoSuggestion;
        this.swapSuggestion = swapSuggestion;
    }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public double getTotalCarbonUsed() { return totalCarbonUsed; }
    public void setTotalCarbonUsed(double totalCarbonUsed) { this.totalCarbonUsed = totalCarbonUsed; }

    public double getTotalCarbonSaved() { return totalCarbonSaved; }
    public void setTotalCarbonSaved(double totalCarbonSaved) { this.totalCarbonSaved = totalCarbonSaved; }

    public String getEcoSuggestion() { return ecoSuggestion; }
    public void setEcoSuggestion(String ecoSuggestion) { this.ecoSuggestion = ecoSuggestion; }

    public EcoSwapSuggestion getSwapSuggestion() { return swapSuggestion; }
    public void setSwapSuggestion(EcoSwapSuggestion swapSuggestion) { this.swapSuggestion = swapSuggestion; }

    public static class EcoSwapSuggestion {
        private Long cartItemIdToReplace;
        private Long suggestedProductId;
        private String suggestedProductName;
        private Double carbonSavingsPerUnit;
        private Integer quantity;

        public EcoSwapSuggestion() {}
        public EcoSwapSuggestion(Long cartItemIdToReplace, Long suggestedProductId,
                                 String suggestedProductName, Double carbonSavingsPerUnit, Integer quantity) {
            this.cartItemIdToReplace = cartItemIdToReplace;
            this.suggestedProductId = suggestedProductId;
            this.suggestedProductName = suggestedProductName;
            this.carbonSavingsPerUnit = carbonSavingsPerUnit;
            this.quantity = quantity;
        }

        public Long getCartItemIdToReplace() { return cartItemIdToReplace; }
        public void setCartItemIdToReplace(Long cartItemIdToReplace) { this.cartItemIdToReplace = cartItemIdToReplace; }

        public Long getSuggestedProductId() { return suggestedProductId; }
        public void setSuggestedProductId(Long suggestedProductId) { this.suggestedProductId = suggestedProductId; }

        public String getSuggestedProductName() { return suggestedProductName; }
        public void setSuggestedProductName(String suggestedProductName) { this.suggestedProductName = suggestedProductName; }

        public Double getCarbonSavingsPerUnit() { return carbonSavingsPerUnit; }
        public void setCarbonSavingsPerUnit(Double carbonSavingsPerUnit) { this.carbonSavingsPerUnit = carbonSavingsPerUnit; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Double getTotalSavings() {
            return carbonSavingsPerUnit != null && quantity != null
                    ? carbonSavingsPerUnit * quantity : 0.0;
        }
    }
}
