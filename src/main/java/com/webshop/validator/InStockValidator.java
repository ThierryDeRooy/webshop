package com.webshop.validator;

import com.webshop.entity.ProductDetails;
import com.webshop.model.Cart;
import com.webshop.model.CartLine;
import com.webshop.service.ProductService;
import com.webshop.utils.SessionsStock;
import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class InStockValidator implements ConstraintValidator<InStock, CartLine> {

    private ProductService productService;

    @Override
    public boolean isValid(CartLine cartLine, ConstraintValidatorContext constraintValidatorContext) {
        int quantityAlreadyInCart = 0;
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        Cart cart = (Cart) session.getAttribute("myOrders");
        if (cart!=null) {
            CartLine line = cart.findLineByCode(cartLine.getProduct().getProductDetails().getCode());
            if (line!=null)
                quantityAlreadyInCart = line.getQuantity();
        }

        ProductDetails prdDet = productService.findById(cartLine.getProduct().getProductDetails().getId());
        if (prdDet == null)
            return false;
        int maxim = prdDet.getStock() - SessionsStock.getQuantity(cartLine.getProduct().getProductDetails().getId()) + quantityAlreadyInCart;
        if (cartLine.getQuantity()!=null && cartLine.getQuantity() > maxim)
            return false;
        else
            return true;

    }




}
