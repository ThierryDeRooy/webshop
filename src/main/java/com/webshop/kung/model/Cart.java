package com.webshop.kung.model;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart {

    @Valid
    private List<CartLine> cartLines = new ArrayList<>();
    @Valid
    private CustomerInfo customerInfo;
    private Locale locale;

    public Cart(){}

    public List<CartLine> getCartLines() {
        return cartLines;
    }

    public void setCartLines(List<CartLine> cartLines) {
        this.cartLines = cartLines;
    }

    public void addCartLine(CartLine cartLine) {
        CartLine line = this.findLineByCode(cartLine.getProduct().getProductDetails().getCode());

        if (line == null) {
            cartLines.add(cartLine);
        } else {
            line.setQuantity(line.getQuantity() + cartLine.getQuantity());
        }
    }

    public void removeCartLine(CartLine cartLine){
        cartLines.remove(cartLine);
    }

    public void removeCartLine(String productCode) {
        for (CartLine cartLine : cartLines) {
            if (productCode.equals(cartLine.getProduct().getProductDetails().getCode())) {
                removeCartLine(cartLine);
                break;
            }
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totPrice = new BigDecimal(0);
        for (CartLine cartLine: cartLines) {
            totPrice = totPrice.add(cartLine.getTotalPrice());
        }
        return totPrice;
    }

    public int getNumberOfProducts() {
        return cartLines.size();
    }

    public void updateQuantity(Cart cartForm) {
        if (cartForm != null) {
            List<CartLine> lines = cartForm.getCartLines();
            for (CartLine line : lines) {
                this.updateProduct(line.getProduct().getProductDetails().getCode(), line.getQuantity());
            }
        }

    }

    public void updateProduct(String code, int quantity) {
        CartLine line = this.findLineByCode(code);

        if (line != null) {
            if (quantity <= 0) {
                this.cartLines.remove(line);
            } else {
                line.setQuantity(quantity);
            }
        }
    }


    private CartLine findLineByCode(String code) {
        for (CartLine line : this.cartLines) {
            if (line.getProduct().getProductDetails().getCode().equals(code)) {
                return line;
            }
        }
        return null;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }
}
