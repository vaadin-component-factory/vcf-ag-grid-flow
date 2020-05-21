package org.vaadin.jchristophe;

/**
 * @author jcgueriaud
 */
public class Price {
    private int price;
    private String currency = "EUR";

    public Price(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Price setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Price setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
