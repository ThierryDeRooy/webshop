package com.webshop.controller;

import com.webshop.entity.*;
import com.webshop.constants.Constants;
import com.webshop.repository.OrderCostRepository;
import com.webshop.repository.TransportCostRepository;
import com.webshop.utils.SessionsStock;
import com.webshop.utils.Utils;
import com.webshop.model.*;
import com.webshop.service.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class WebController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
    }

    @Value("${file.mainfolder}")
    private String UPLOADED_FOLDER;
    @Value("${file.subfolder}")
    private String SUB_FOLDER;


    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CountryService countryService;
    private final OrderService orderService;
    private final OrderDetailsService orderDetailsService;
    private final CustomerService customerService;
    private final EmailService emailService;
    private final InvoiceService invoiceService;
    private final TransportCostRepository transportCostRepository;
    private final OrderCostRepository orderCostRepository;
    private final MessageSource messageSource;
    private final CartService cartService;

    private final CacheManager cacheManager;   // autowire cache manager



    @GetMapping("/")
    public String root(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpServletRequest request) {
        Locale loc = (Locale) request.getSession().getAttribute("language");
        if (loc==null) {
            loc = request.getLocale();
            request.getSession().setAttribute("language", loc);
            return "redirect:/home?language="+loc.getLanguage();
        }

        return "home";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        return "contact";
    }

    @GetMapping("/productList")
    public String productList(Model model,
                              HttpServletRequest request,
                              @RequestParam(defaultValue = "0") Integer pageNo,
                              @RequestParam(required = false) Integer pageSize,
                              @RequestParam(required = false) String sortBy,
                              @RequestParam(required = false) String direction,
                              @RequestParam(required = false) String search) {

        model = getProducts(request, model, pageNo, pageSize, sortBy, direction, search);
        model.addAttribute("imageFolder", SUB_FOLDER);
        setWeergaveProductList(request);

        return "productList";
    }

    @GetMapping("/buyProduct")
    public String productDetails(HttpServletRequest request, Model model) {
        CartLine cartLine = new CartLine();
        Locale loc = getLocale();
        String code = request.getParameter("code");
        if (code==null || code.isEmpty()) {
            code = (String) request.getSession().getAttribute("code");
        } else {
            request.getSession().setAttribute("code", code);
        }

        Product product = productService.findByCode(loc, code);
//        if (product != null && product.getProductDetails().getStock()-product.getProductDetails().getReserved()>0) {
        if (product != null ) {
            cartLine.setProduct(product);
            model.addAttribute("cartLine", cartLine);
            model.addAttribute("stock", product.getProductDetails().getStock()- SessionsStock.getQuantity(product.getProductDetails().getId()));
        }
 //       model.addAttribute("newCartLine", new CartLine());
        List<Category> cats = categoryService.findMainCategories(loc);
        model.addAttribute("categories", cats);
        model.addAttribute("imageFolder", SUB_FOLDER);
        File imagesFolder = new File(SUB_FOLDER+product.getProductDetails().getCode());
        File[] images = imagesFolder.listFiles();
        model.addAttribute("images", images);

        return "productDetails";
    }

    @PostMapping("/orderProduct")
    public  String orderProduct(@Valid @ModelAttribute("cartLine") CartLine newCartLine,
                                BindingResult result,
                                HttpServletRequest request, Model model) {
        Cart myCart = Utils.getCartInSession(request);
        if (result.hasErrors()) {
            ProductDetails pd = productService.findById(newCartLine.getProduct().getProductDetails().getId());
            int stock = pd.getStock()-SessionsStock.getQuantity(pd.getId());
            model.addAttribute("stock", stock);
            return "productDetails";
        }
        cartService.updateQuantitiesCartLine(myCart, myCart.findLineByCode(newCartLine.getProduct().getProductDetails().getCode()), newCartLine);
        cartService.setTransportCost(myCart);
        return "redirect:/shoppingCart";
    }


    // POST: Update quantity for product in cart
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @Valid @ModelAttribute("myCart") Cart cartForm,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/shoppingCart";
        }

        Cart cartInfo = Utils.getCartInSession(request);
        cartService.updateQuantitiesCart(cartInfo, cartForm);
//        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    // GET: Show cart.
    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        Cart myCart = Utils.getCartInSession(request);
        checkLanguageCart(myCart);
//        myCart = removeUnavailableProducts(myCart);

        model.addAttribute("myCart", myCart);
        model.addAttribute("stock", cartService.getStockShoppingCart(myCart));
        return "shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveProduct" })
    public synchronized String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            Cart cart = Utils.getCartInSession(request);
            for (CartLine cartLine : cart.getCartLines()) {
                if (code.equals(cartLine.getProduct().getProductDetails().getCode())) {
                    cart.removeCartLine(cartLine);
//                    SessionsStock.add(cartLine.getProduct().getProductDetails().getId(), -cartLine.getQuantity());
//                    productService.updateReservedStock(cartLine.getProduct().getProductDetails(), 0, cartLine.getQuantity());
//                    clearCache();
                    break;
                }
            }
            cartService.setTransportCost(cart);
        }
         return "redirect:/shoppingCart";
    }

    // GET: Enter customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
        Locale locale = getLocale();
        Cart cart = Utils.getCartInSession(request);

        if (cart.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cart.getCustomerInfo();

        if (customerInfo==null) {
            customerInfo = fillInCustomerInfoIfUserLoggedIn(request.getUserPrincipal());
         }

        model.addAttribute("customerInfo", customerInfo);
        List<Country> countries = countryService.findAllCountries(locale);
        model.addAttribute("countries", countries);

        return "shoppingCartCustomer";
    }


    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(@Valid @ModelAttribute("customerInfo") CustomerInfo customerInfo,
                                           BindingResult result,
                                           HttpServletRequest request, //
                                           Model model, //
                                           final RedirectAttributes redirectAttributes) {
        Locale locale = getLocale();
        if (result.hasErrors()) {
            List<Country> countries = countryService.findAllCountries(locale);
            model.addAttribute("countries", countries);
            if (customerInfo== null) {
                model.addAttribute("customerInfo", new CustomerInfo());
            }
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        if (request.getParameter("saveAddress") != null && request.getUserPrincipal() != null) {
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
//        cartInfo = removeUnavailableProducts(cartInfo);

        if (cartInfo == null || cartInfo.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (cartInfo.getCustomerInfo()==null) {

            return "redirect:/shoppingCartCustomer";
        }
        cartService.setTransportCost(cartInfo);
        model.addAttribute("myCart", cartInfo);
//        model.addAttribute("orderCost", getTransportCost(cartInfo));
        return "shoppingCartConfirmation";
    }

    // POST: Submit Cart (Save)
    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        Cart cartInfo = Utils.getCartInSession(request);
        Locale locale = getLocale();

        if (cartInfo.getCartLines().isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (cartInfo.getCustomerInfo()==null) {

            return "redirect:/shoppingCartCustomer";
        }
        try {
            Order order = cartService.saveCartToOrder(cartInfo, request.getUserPrincipal());

            // create invoice in PDF
            File pdfFile = invoiceService.generateInvoiceFor(order, locale);

            //send email to customer
            try {
                if (pdfFile != null) {
                    emailService.sendOrderMail(order, pdfFile);
                } else {
                    emailService.sendOrderMail(order);
                }
            } catch (Exception ex) {
                log.error("No email to Customer sent: " + ex.getMessage());
            }
            ShowOrder myShowOrder = prepareOrderForShowing(order);
            model.addAttribute("myShowOrder", myShowOrder);
        } catch (Exception e) {

            return "redirect:/shoppingCartConfirmation";
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

    @RequestMapping(value = { "/showOrderList" })
    public String showOrderList(HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        List<Order> orderList = new ArrayList<>();
        if (principal != null)
                orderList = orderService.findOrdersByUsernameAndStatusNot(principal.getName(), Constants.ORDER_ARCHIVED);
        model.addAttribute("orders", orderList);
        String chosenOrderId = request.getParameter("id");
        if (chosenOrderId!=null && !chosenOrderId.isEmpty() && chosenOrderId.matches("\\d*")) {
            Long idLong = Long.parseLong(chosenOrderId);
            Order chosenOrder = orderService.findByIdAndUsernameAndStatusNot(idLong, principal.getName(), Constants.ORDER_ARCHIVED);
            model.addAttribute("chosenOrder", chosenOrder);
            Utils.setOrderInSession(request, chosenOrder);
        }
        return "ordersCustomer";
    }


    @RequestMapping(value = { "/orderSame" }, method = RequestMethod.POST)
    public String orderSame(HttpServletRequest request, Model model) {

        Cart cartInfo = Utils.getCartInSession(request);
        Order chosenOrder = Utils.getOrderInSession(request);
        Utils.removeOrderInSession(request);
        if (chosenOrder != null)
            cartInfo = addOrderToCart(cartInfo, chosenOrder);

        return "redirect:/shoppingCart";
    }

    @RequestMapping(value = { "/orderIdentically" }, method = RequestMethod.POST)
    public String orderIdentically(HttpServletRequest request, Model model) {

        Cart cartInfo = Utils.getCartInSession(request);
        cartInfo.setCartLines(new ArrayList<>());

        Order chosenOrder = Utils.getOrderInSession(request);
        Utils.removeOrderInSession(request);
        if (chosenOrder != null) {
            cartInfo = addOrderToCart(cartInfo, chosenOrder);
            cartInfo = addCustomerInfoToCart(cartInfo, chosenOrder);
            cartInfo.setLocale(chosenOrder.getReceiverCountry().getLang());
        }
        return "redirect:/shoppingCartConfirmation";
    }

    private Cart addOrderToCart(Cart myCart, Order myOrder) {
        Cart addNewCart = new Cart();
        for (OrderDetails orderDetail : myOrder.getOrderDetails()){
            Product prod = productService.findByCode(orderDetail.getProduct().getLang(), orderDetail.getProduct().getProductDetails().getCode());
            if (prod != null) {
                CartLine cartLine = new CartLine(orderDetail.getProduct(), orderDetail.getQuantity());
                addNewCart.addCartLine(cartLine);
            }
        }
        cartService.updateQuantitiesCart(myCart, addNewCart);
        return myCart;
    }

    private Cart addCustomerInfoToCart(Cart myCart, Order myOrder) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setName(myOrder.getReceiverName());
        customerInfo.setAddress(myOrder.getReceiverAddress());
        customerInfo.setPostCode(myOrder.getReceiverPostNr());
        customerInfo.setCity(myOrder.getReceiverCity());
        customerInfo.setCountry(myOrder.getReceiverCountry());
        customerInfo.setEmail(myOrder.getReceiverEmail());
        customerInfo.setPhone(myOrder.getReceiverTel());
        customerInfo.setBtwNr(myOrder.getReceiverBtw());
        myCart.setCustomerInfo(customerInfo);
        return myCart;
    }


    private void checkLanguageCart(Cart myCart) {
        Locale loc = getLocale();
        if (!loc.equals(myCart.getLocale())) {
            myCart.setLocale(loc);
            for (CartLine cartLine : myCart.getCartLines()) {
                Product langProd = productService.findByCode(loc, cartLine.getProduct().getProductDetails().getCode());
                if (langProd!=null)
                    cartLine.setProduct(langProd);
            }
        }
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
            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                if (customer.getCompany()!=null)
                    customerInfo.setName(customer.getCompany());
                else
                    customerInfo.setName(customer.getFirstName()+ " " + customer.getFamilyName());
                customerInfo.setAddress(customer.getAddress());
                customerInfo.setPostCode(customer.getPostNr());
                customerInfo.setCity(customer.getCity());
                customerInfo.setCountry(customer.getCountry());
                customerInfo.setEmail(customer.getEmail());
                customerInfo.setPhone(customer.getTel());
                customerInfo.setBtwNr(customer.getBtwNr());
            }
        }
        return customerInfo;
    }

    private void saveAddressInDb(CustomerInfo customerInfo, Principal principal) {
        if (principal!=null && principal.getName()!=null) {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            if (customer != null) {
                customer.setCompany(customerInfo.getName());
                customer.setEmail(customerInfo.getEmail());
                customer.setAddress(customerInfo.getAddress());
                customer.setPostNr(customerInfo.getPostCode());
                customer.setCity(customerInfo.getCity());
                customer.setCountry(customerInfo.getCountry());
                customer.setTel(customerInfo.getPhone());
                customer.setBtwNr(customerInfo.getBtwNr());
                customerService.saveUser(customer);
            }
        }
    }

    private void setWeergaveProductList(HttpServletRequest request) {
        String weergave = request.getParameter("weergave");
        if (weergave != null) {
            request.getSession().setAttribute("weergave", weergave);
        }
        if (request.getSession().getAttribute("weergave") == null)
            request.getSession().setAttribute("weergave", "grid");
    }

    private Model getProducts(HttpServletRequest request, Model model, Integer pageNo, Integer pageSizeIn, String sortByIn, String directionIn, String search) {
        Locale locale = getLocale();
        Integer pageSize = (Integer) Utils.getOrSetAttributeInSession(request, "pageSize", pageSizeIn, 10);
        String sortBy = (String) Utils.getOrSetAttributeInSession(request, "sortBy", sortByIn, "productDetails.code");
        String direction = (String) Utils.getOrSetAttributeInSession(request, "direction", directionIn, "ASC");
        String catStr = request.getParameter("category");
        if (request.getParameter("language") != null)
            catStr = "ALL";
        Category presentCat = (Category) request.getSession().getAttribute("selectedCategory");
        Category selectedCat = categoryService.getSelectedCategory(catStr, locale, presentCat);
        Page<Product> pagedResult = productService.getAllProductsByStock(locale, 0, pageNo, pageSize, sortBy, direction, search, selectedCat);
        request.getSession().setAttribute("selectedCategory", selectedCat);
        model.addAttribute("cats", breadtails(selectedCat));
        if (pagedResult.hasContent()) {
            model.addAttribute("sessionsStock", getStocksInSessions(pagedResult.getContent()));
            model.addAttribute("products", pagedResult.getContent());
            model.addAttribute("totalPages" , pagedResult.getTotalPages());
            model.addAttribute("totalElems", pagedResult.getTotalElements());
            model.addAttribute("pageNr", pagedResult.getNumber());
            model.addAttribute("pageSizeMax", pagedResult.getSize());
            model.addAttribute("elemsOnPage", pagedResult.getNumberOfElements());
        } else {
            model.addAttribute("products", new ArrayList<Product>());
            model.addAttribute("totalPages" , 0);
            model.addAttribute("totalElems",0);
        }
        List<Category> cats = categoryService.findMainCategories(locale);
        model.addAttribute("categories", cats);
        if (search!=null)
            model.addAttribute("search", search);
        return model;
    }


    private synchronized Map<Long, Integer> getStocksInSessions(List<Product> productList) {
        Map<Long, Integer> sessionsStocks = new TreeMap<>();
        for (Product prd : productList) {
            sessionsStocks.put(prd.getProductDetails().getId(), SessionsStock.getQuantity(prd.getProductDetails().getId()));
        }
        return sessionsStocks;
    }


    private List<Category> breadtails(Category cat) {
        List<Category> breadtails = new ArrayList<Category>();
        while (cat!=null) {
            breadtails.add(0, cat);
            cat = cat.getUpperCategory();
        }
        return breadtails;
    }

    private Locale getLocale() {
        return new Locale(LocaleContextHolder.getLocale().getLanguage());
    }


}
