package it.unisalento.recproject.adminservice.service.pdf;

import java.util.Objects;
import java.util.Optional;

public class Energy {
    private Optional<String> pname;
    private int quantity;
    private double priceperpeice;

    public Energy(String pname, int quantity, double priceperpeice) {
        this.pname = Optional.ofNullable(pname);
        this.quantity = quantity;
        this.priceperpeice = priceperpeice;
    }

    public Optional<String> getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = Optional.ofNullable(pname);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceperpeice() {
        return priceperpeice;
    }

    public void setPriceperpeice(double priceperpeice) {
        this.priceperpeice = priceperpeice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Energy)) return false;
        Energy product = (Energy) o;
        return Objects.equals(pname, product.pname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pname);
    }

    @Override
    public String toString() {
        return "{" +
                "pname=" + pname +
                ", quantity=" + quantity +
                ", priceperpeice=" + priceperpeice +
                '}';
    }
}
