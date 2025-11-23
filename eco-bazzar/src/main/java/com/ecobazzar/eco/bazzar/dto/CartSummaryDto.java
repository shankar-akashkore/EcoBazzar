package com.ecobazzar.eco.bazzar.dto;

import java.util.List;
import com.ecobazzar.eco.bazzar.model.CartItem;

public class CartSummaryDto {

    private List<CartItem> items;

    private double totalPrice;

    private double totalCarbonUsed;

    private double totalCarbonSaved;

    private String ecoSuggestion;

    public CartSummaryDto() {}

    public CartSummaryDto(List<CartItem> items, double totalPrice,
                          double totalCarbonUsed, double totalCarbonSaved,
                          String ecoSuggestion) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalCarbonUsed = totalCarbonUsed;
        this.totalCarbonSaved = totalCarbonSaved;
        this.ecoSuggestion = ecoSuggestion;
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

}
