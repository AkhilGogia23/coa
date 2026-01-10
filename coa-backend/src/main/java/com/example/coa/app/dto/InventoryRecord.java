package com.example.coa.app.dto;

import java.time.LocalDate;
import java.math.BigDecimal;
public class InventoryRecord {

    private String fgInventoryId;
    private String fgDescription;
    private String matlDescription;
    private BigDecimal qtyRequired;
    private LocalDate matlExpiryDate;
    private String manufacturer;
    private String ingredientInventoryId;
    private String blendQty;
    // --- Getters & Setters ---



    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getIngredientInventoryId() {
        return ingredientInventoryId;
    }

    public void setIngredientInventoryId(String ingredientInventoryId) {
        this.ingredientInventoryId = ingredientInventoryId;
    }

    public String getBlendQty() {
        return blendQty;
    }

    public void setBlendQty(String blendQty) {
        this.blendQty = blendQty;
    }
    public BigDecimal getQtyRequired() {
        return qtyRequired;
    }

    public void setQtyRequired(BigDecimal qtyRequired) {
        this.qtyRequired = qtyRequired;
    }

    public String getFgInventoryId() {
        return fgInventoryId;
    }

    public void setFgInventoryId(String fgInventoryId) {
        this.fgInventoryId = fgInventoryId;
    }

    public String getFgDescription() {
        return fgDescription;
    }

    public void setFgDescription(String fgDescription) {
        this.fgDescription = fgDescription;
    }

    public String getMatlDescription() {
        return matlDescription;
    }

    public void setMatlDescription(String matlDescription) {
        this.matlDescription = matlDescription;
    }


    public LocalDate getMatlExpiryDate() {
        return matlExpiryDate;
    }

    public void setMatlExpiryDate(LocalDate matlExpiryDate) {
        this.matlExpiryDate = matlExpiryDate;
    }

}
