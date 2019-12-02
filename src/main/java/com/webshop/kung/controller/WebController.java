package com.webshop.kung.controller;

import com.webshop.kung.constants.Constants;
import com.webshop.kung.entity.*;
import com.webshop.kung.model.*;
import com.webshop.kung.service.*;
import com.webshop.kung.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class WebController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
    }


    private ProductService productService;
    private CategoryService categoryService;
    private CountryService countryService;
    private OrderService orderService;
    private OrderDetailsService orderDetailsService;
    private UserService userService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) { this.countryService = countryService;}

    @Autowired
    public void setOrderService(OrderService orderService) { this.orderService = orderService;}

    @Autowired
    public void setOrderDetailsService(OrderDetailsService orderDetailsService) { this.orderDetailsService = orderDetailsService;}

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService;}

    @GetMapping("/")
    public String root(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        return "contact";
    }

    @GetMapping("/productList")
    public String productList(Model model) {
        List<Product> products = productService.findAllProductsByStatus(LocaleContextHolder.getLocale(), Constants.PRODUCT_ACTIVE);
        model.addAttribute("products", products);
        List<Category> cats = categoryService.findMainCategories(LocaleContextHolder.getLocale());
        model.addAttribute("categories", cats);
        return "productList";
    }

    @GetMapping("/buyProduct")
    public String productDetails(HttpServletRequest request, Model model) {
        CartLine cartLine = new CartLine();
        Locale loc = LocaleContextHolder.getLocale();
        String code = request.getParameter("code");
        if (code==null || code.isEmpty()) {
            code = (String) request.getSession().getAttribute("code");
        } else {
            request.getSession().setAttribute("code", code);
        }

        Product product = productService.findByCodeAndStatus(loc, code, Constants.PRODUCT_ACTIVE);
        if (product != null) {
            cartLine.setProduct(product);
            model.addAttribute("cartLine", cartLine);
        }
 //       model.addAttribute("newCartLine", new CartLine());
        List<Category> cats = categoryService.findMainCategories(loc);
        model.addAttribute("categories", cats);

        return "productDetails";
    }

    @PostMapping("/orderProduct")
    public  String orderProduct(@Valid @ModelAttribute("cartLine") CartLine newCartLine,
                                BindingResult result,
                                HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return "productDetails";
        }
        Cart myCart = Utils.getCartInSession(request);

//        CartLine cartLine = new CartLine(prod, quantityOrder);
        myCart.addCartLine(newCartLine);
        model.addAttribute("myCart", myCart);
        return "redirect:/shoppingCart";
    }


    // POST: Update quantity for product in cart
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @Valid @ModelAttribute("myCart") Cart cartForm,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return "shoppingCart";
        }

        Cart cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    // GET: Show cart.
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        Cart myCart = Utils.getCartInSession(request);
        checkLanguageCart(myCart);

        model.addAttribute("myCart", myCart);
        return "shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveProduct" })
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            Cart cart = Utils.getCartInSession(request);
            cart.removeCartLine(code);
        }
         return "redirect:/shoppingCart";
    }

    // GET: Enter customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

        Cart cart = Utils.getCartInSession(request);

        if (cart.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cart.getCustomerInfo();

        if (customerInfo==null) {
            customerInfo = fillInCustomerInfoIfUserLoggedIn(request.getUserPrincipal());
         }

        model.addAttribute("customerInfo", customerInfo);
        List<Country> countries = countryService.findAllCountries(LocaleContextHolder.getLocale());
        model.addAttribute("countries", countries);

        return "shoppingCartCustomer";
    }


    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(@Valid @ModelAttribute("customerInfo") CustomerInfo customerInfo,
                                           BindingResult result,
                                           HttpServletRequest request, //
                                           Model model, //
                                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            if (customerInfo== null) {
                List<Country> countries = countryService.findAllCountries(LocaleContextHolder.getLocale());
                model.addAttribute("countries", countries);
                model.addAttribute("customerInfo", new CustomerInfo());
            }
            List<Country> countries = countryService.findAllCountries(LocaleContextHolder.getLocale());
            model.addAttribute("countries", countries);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        if (request.getParameter("saveAddress") != null ) {
            saveAddressInDb(customerInfo, request.getUserPrincipal());
        }

        Cart cart = Utils.getCartInSession(request);
        cart.setCustomerInfo(customerInfo);

        return "redirect:/shoppingCartConfirmation";
    }


    // GET: Show information to confirm.
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        Cart cartInfo = Utils.getCartInSession(request);

        if (cartInfo == null || cartInfo.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (cartInfo.getCustomerInfo()==null) {

            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    // POST: Submit Cart (Save)
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        Cart cartInfo = Utils.getCartInSession(request);

        if (cartInfo.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (cartInfo.getCustomerInfo()==null) {

            return "redirect:/shoppingCartCustomer";
        }
        try {
//            orderDAO.saveOrder(cartInfo);
            Order order = saveCartToOrder(cartInfo, request.getUserPrincipal());
//            Long orderNr = saveCartToOrder(cartInfo);
//            Order order = orderService.findById(orderNr);
            ShowOrder myShowOrder = prepareOrderForShowing(order);
            model.addAttribute("myShowOrder", myShowOrder);
        } catch (Exception e) {

            return "shoppingCartConfirmation";
        }

        // Remove Cart from Session.
        Utils.removeCartInSession(request);

        // Store last cart.
        Utils.storeLastOrderedCartInSession(request, cartInfo);


        return "shoppingCartFinalize";
    }


    @RequestMapping(value = { "/showOrder" })
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model,
                                               @RequestParam(value = "orderNr", defaultValue = "") String orderNr) {
        String orderIdStr = request.getParameter("orderNr");
        Order order = null;
        try {
            long orderId = Long.parseLong(orderIdStr);
            order = orderService.findById(orderId);
        } catch (Exception ex) {
            return "error";
        }

        model.addAttribute("myShowOrder", prepareOrderForShowing(order));
        return "shoppingCartFinalize";
    }

//    @GetMapping("/buyProduct")
//    public String productDetails(HttpServletRequest request, Model model) {
//        OrderDetails orderDetails = new OrderDetails();
//        Locale loc = LocaleContextHolder.getLocale();
//        String code = request.getParameter("code");
//        if (code==null || code.isEmpty()) {
//            code = (String) request.getSession().getAttribute("code");
//        } else {
//            request.getSession().setAttribute("code", code);
//        }
//
//        Product product = productService.findByCode(loc.getLanguage(), code);
//        orderDetails.setProduct(product);
//        model.addAttribute("orderDetailsTemp", orderDetails);
//        List<Category> cats = categoryService.findMainCategories(loc);
//        model.addAttribute("categories", cats);
//        model.addAttribute("orderDetails", orderDetails);
//        return "productDetails";
//    }

    private void checkLanguageCart(Cart myCart) {
        Locale loc = LocaleContextHolder.getLocale();
        if (!loc.equals(myCart.getLocale())) {
            myCart.setLocale(loc);
            for (CartLine cartLine : myCart.getCartLines()) {
                Product langProd = productService.findByCode(loc, cartLine.getProduct().getProductDetails().getCode());
                if (langProd!=null)
                    cartLine.setProduct(langProd);
            }
        }
    }

    private Order saveCartToOrder(Cart myCart, Principal principal) {
        Date today = new Date();
        CustomerInfo customer = myCart.getCustomerInfo();
        Order order = new Order(today, customer.getName(), customer.getAddress(), customer.getPostCode(), customer.getCity(),
                customer.getCountry(), customer.getEmail(), customer.getPhone());
        if (principal != null && principal.getName() != null) {
            User regCustomer = userService.findByUsername(principal.getName());
            order.setCustomer(regCustomer);
        }
        order.setStatus(Constants.ORDER_CREATED);
        orderService.addOrder(order);
        for (CartLine cartLine : myCart.getCartLines()) {
            BigDecimal discount = new BigDecimal(0);
            OrderDetails orderDetails = new OrderDetails(order, cartLine.getProduct(), cartLine.getProduct().getName(),
                    cartLine.getProduct().getProductDetails().getPrice(),
                    cartLine.getProduct().getProductDetails().getUnit(),discount, cartLine.getQuantity());
            orderDetailsService.addOrderDetails(orderDetails);
            order.addOrderDetail(orderDetails);
        }
        return order;
    }

    private ShowOrder prepareOrderForShowing(Order order) {
        ShowOrder showOrder = new ShowOrder(order);
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            ShowOrderLine showOrderLine = new ShowOrderLine(orderDetails);
            showOrder.addOrderLine(showOrderLine);
        }
        return showOrder;
    }

    private CustomerInfo fillInCustomerInfoIfUserLoggedIn(Principal principal) {
        CustomerInfo customerInfo = new CustomerInfo();
        if (principal!=null && principal.getName()!=null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                customerInfo.setName(user.getFirstName()+ " " + user.getFamilyName());
                customerInfo.setAddress(user.getAddress());
                customerInfo.setPostCode(user.getPostNr());
                customerInfo.setCity(user.getCity());
                customerInfo.setCountry(user.getCountry());
                customerInfo.setEmail(user.getEmail());
                customerInfo.setPhone(user.getTel());
            }
        }
        return customerInfo;
    }

    private void saveAddressInDb(CustomerInfo customerInfo, Principal principal) {
        if (principal!=null && principal.getName()!=null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                user.setAddress(customerInfo.getAddress());
                user.setPostNr(customerInfo.getPostCode());
                user.setCity(customerInfo.getCity());
                user.setCountry(customerInfo.getCountry());
                user.setTel(customerInfo.getPhone());
                userService.saveUser(user);
            }
        }
    }

}
