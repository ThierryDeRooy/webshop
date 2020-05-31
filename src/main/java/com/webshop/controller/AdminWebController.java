package com.webshop.controller;

import com.webshop.config.HttpSessionConfig;
import com.webshop.constants.Constants;
import com.webshop.entity.*;
import com.webshop.event.UserRegistrationEvent;
import com.webshop.exception.CategoryAlreadyExistsException;
import com.webshop.exception.ProductException;
import com.webshop.repository.TransportCostRepository;
import com.webshop.repository.WebUserRepository;
import com.webshop.userDetails.AuthenticationSuccessHandlerImpl;
import com.webshop.utils.SessionsStock;
import com.webshop.utils.Utils;
import com.webshop.model.*;
import com.webshop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequiredArgsConstructor
public class AdminWebController {

//    private String UPLOADED_FOLDER = "D:/webshop/webshop/";
//    private String SUB_FOLDER = "images/";


//    @Autowired
//    private FileValidator fileValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
//        dataBinder.setValidator(fileValidator);
    }


    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final PasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;
    private final WebUserRepository webUserRepository;
    private final CountryService countryService;
    private final TransportCostRepository transportCostRepository;
    private final InvoiceService invoiceService;
    private final EmailService emailService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final SessionRegistry sessionRegistry;


    @Autowired
    private CacheManager cacheManager;   // autowire cache manager


    @GetMapping("/showCategories")
    public String newCategory(HttpServletRequest request, Model model) {
        model.addAttribute("upperCats", (List<Category>) categoryService.listCategories(new Locale("en")));
//      model.addAttribute("categories", categories);
        model.addAttribute("categories", getCategoriesForAdmin());
        model.addAttribute("category", new CategoryDisplay());

        return "adminCategory";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDisplay categoryDisplay,
                               BindingResult result, HttpServletRequest request,  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("upperCats", (List<Category>) categoryService.listCategories(new Locale("en")));
            model.addAttribute("categories", getCategoriesForAdmin());
            return "adminCategory";
        }
        for (String lang : categoryDisplay.getNames().keySet()){
            try {
                categoryService.addCategory(categoryDisplay.getIds().get(lang),
                        new Locale(lang),
                        categoryDisplay.getCode(),
                        categoryDisplay.getNames().get(lang),
                        categoryDisplay.getUpperCatCode());
            } catch (CategoryAlreadyExistsException ex) {
                result.rejectValue("code", "lang.catCodeAlreadyExists");
                model.addAttribute("upperCats", (List<Category>) categoryService.listCategories(new Locale("en")));
                model.addAttribute("categories", getCategoriesForAdmin());
                return "adminCategory";
            }
        }
        return "redirect:/showCategories";

    }


    @GetMapping("/addNewProduct")
    public synchronized String newProduct(Model model) {
//        model.addAttribute("categories", categoryService.findLowestLevelCategories());
        Locale loc = getLocale();
        model.addAttribute("categorien", getOrderCats());
        model.addAttribute("productInfo", new ProductInfo());
        model.addAttribute("lang", loc);
//        model.addAttribute("PriceUnits", ProductDetails.PriceUnit.values());
        List<Category> cats = categoryService.findMainCategories(loc);
        model.addAttribute("categories", cats);
        List<Product> productList = productService.findAllProducts(loc);
        model.addAttribute("products", productList);
        List<Product> missingProductList = productService.findMissingProducts(loc);
        model.addAttribute("missingProducts", missingProductList);
        Map<Long,Integer> sessionsStock = SessionsStock.getSessionsStock();
        model.addAttribute("sessionsStock", sessionsStock);
        model.addAttribute("langCat", getCategoryIdCodeValuesLang(loc));

        return "addNewProduct";
    }

    @GetMapping("/saveProduct")
    public String saveProduct(Model model) {
        return "redirect:/addNewProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("productInfo") ProductInfo productInfo,
                              BindingResult result,
                              @RequestParam("btnSubmit") String submit,
                              @RequestParam("updateStock") String updateStock,
                              Model model,
                              HttpServletRequest request) {
        Locale loc = getLocale();
        int stockToAdd = 0;
        try {
            stockToAdd = Integer.parseInt(request.getParameter("updateStock"));
        } catch (NumberFormatException ex) {
        }
        if (!result.hasErrors()) {
            try {
                productService.updateProduct(loc, productInfo.getProduct(), productInfo.getFile(), stockToAdd);
            } catch (ProductException e) {
                if ("Stock is negative".equalsIgnoreCase(e.getMessage()))
                    result.rejectValue("product.productDetails.stock", "lang.stock.not.negative");
                else
                    result.rejectValue("product.productDetails.code", "lang.codeAlreadyExists");
            } catch (IOException e) {
                result.rejectValue("file", "lang.error.fileRejected");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("categorien", getOrderCats());
//            model.addAttribute("PriceUnits", ProductDetails.PriceUnit.values());
            List<Category> cats = categoryService.findMainCategories(getLocale());
            model.addAttribute("categories", cats);
            List<Product> productList = productService.findAllProducts(loc);
            model.addAttribute("products", productList);
            List<Product> missingProductList = productService.findMissingProducts(loc);
            model.addAttribute("missingProducts", missingProductList);
            return "addNewProduct";
        }

//        updateProduct(productInfo.getProduct(), productInfo.getFile(),  stockToAdd);
        return "redirect:/addNewProduct";
    }

    @PostMapping("/removeProductFromBaskets")
    public synchronized String removeFromBaskets(@RequestParam("prodDetailsId") long prodDetailsId) {
        for (HttpSession httpSession : HttpSessionConfig.getActiveSessions()) {
            try {
                Cart cart = (Cart) httpSession.getAttribute("myOrders");
                if (cart != null) {
                    for (CartLine cartLine : cart.getCartLines()) {
                        if (prodDetailsId == cartLine.getProduct().getProductDetails().getId()) {
//                        productService.updateReservedStock(cartLine.getProduct().getProductDetails(), 0, cartLine.getQuantity());
//                        clearCache();
                            cart.removeCartLine(cartLine);
//                            SessionsStock.add(cartLine.getProduct().getProductDetails().getId(), -cartLine.getQuantity());
                            break;
                        }
                    }
                    cartService.setTransportCost(cart);
                }
            } catch (Exception ex) {
            }
         }
//        SessionsStock.reset(prodDetailsId);
        return "redirect:/addNewProduct";
    }


    @RequestMapping("/showTransportCosts")
    public String showTransportCosts(Model model,
                                     HttpServletRequest request) {
        List<Country> ctyList = countryService.findAllCountries(new Locale("en"));
//        Map<String, String> countries = new TreeMap<>();
//        for (Country country : ctyList){
//            countries.put(country.getCode(), country.getCountry());
//        }
        List<TransportCost> transportCosts = new ArrayList<>();
        Iterable<TransportCost> itTransp = transportCostRepository.findAll();
        itTransp.forEach(transportCosts::add);
        model.addAttribute("countries", ctyList);
        model.addAttribute("transportCosts", transportCosts);
        model.addAttribute("transportCost", new TransportCost());
        return "adminTransportCost";
    }

    @PostMapping("/saveTransportCost")
    public String saveTransportCost(@Valid @ModelAttribute("transportCost") TransportCost transportCost,
                                    BindingResult result,
                                    Model model,
                                    HttpServletRequest request) {
        if (result.hasErrors()) {
            List<Country> ctyList = countryService.findAllCountries(new Locale("en"));
            List<TransportCost> transportCosts = new ArrayList<>();
            Iterable<TransportCost> itTransp = transportCostRepository.findAll();
            itTransp.forEach(transportCosts::add);
            model.addAttribute("countries", ctyList);
            model.addAttribute("transportCosts", transportCosts);
            return "adminTransportCost";
        }
        if (transportCost.getId()==null) {
            TransportCost trCost = transportCostRepository.findByCountryCode(transportCost.getCountry().getCode());
            if (trCost!=null)
                transportCost.setId(trCost.getId());
        }
        transportCostRepository.save(transportCost);
//        clearCache();
        return "redirect:/showTransportCosts";
    }

    @RequestMapping("/showCountries")
    public String showCountries(Model model) {
        model.addAttribute("countries", getCountriesForAdmin());
        model.addAttribute("country", new CountryDisplay());
        return "adminCountry";
    }

    @PostMapping("/saveCountry")
    public String saveCountry(@Valid @ModelAttribute("country") CountryDisplay countryDisplay,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("countries", getCountriesForAdmin());
            return "adminCountry";
        }
        boolean countryExists = !countryService.findByCode(countryDisplay.getCode()).isEmpty();
        for (String lang : countryDisplay.getNames().keySet()){
            Country country = null;
            if (countryDisplay.getIds().get(lang) != null)
                country = countryService.findById(countryDisplay.getIds().get(lang));
            else if (countryExists) {
                result.rejectValue("code", "codeAlreadyExists", "country for this code already exists");
                model.addAttribute("countries", getCountriesForAdmin());
                return "adminCountry";
            }
            if (country == null)
                country = new Country();
            country.setLang(new Locale(lang));
            country.setCode(countryDisplay.getCode());
            country.setCountry(countryDisplay.getNames().get(lang));
            countryService.saveCountry(country);
//            clearCache();
        }
        return "redirect:/showCountries";
    }


    @RequestMapping("/showOrders")
    public String showOrders(Model model,
                             @RequestParam(required = false) String search,
                             @RequestParam(defaultValue = "100") Integer status,
                             HttpServletRequest request) {
        List<Order> orders = getOrders(request, status, search);
        model.addAttribute("orders", orders);
        String chosenOrderId = request.getParameter("id");
        if (chosenOrderId!=null && !chosenOrderId.isEmpty() && chosenOrderId.matches("\\d*")) {
            Long idLong = Long.parseLong(chosenOrderId);
            Order chosenOrder = orderService.findById(idLong);
            model.addAttribute("chosenOrder", chosenOrder);
        }
        return "orderTable";
    }

    @PostMapping("/showInvoice")
    public ResponseEntity<InputStreamResource> showInvoice(@ModelAttribute("chosenOrder") Order order) throws Exception {
        Order chosenOrder = orderService.findById(order.getId());
        File pdfFile = invoiceService.generateInvoiceFor(chosenOrder, chosenOrder.getReceiverCountry().getLang());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "attachment; filename=" + pdfFile.getName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.setContentLength(pdfFile.length());
        ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
                new InputStreamResource(new FileInputStream(pdfFile)), headers, HttpStatus.OK);
        return response;
    }

    @PostMapping("/resendEmail")
    public String resendEmail(@ModelAttribute("chosenOrder") Order order,
                              Model model) throws Exception {
        Order chosenOrder = orderService.findById(order.getId());
        if (chosenOrder==null) {
            return "redirect:/showOrders";
        }
        File pdfFile = invoiceService.generateInvoiceFor(chosenOrder, chosenOrder.getReceiverCountry().getLang());
        //send email to customer
        try {
            if (pdfFile != null) {
                emailService.resendOrderMail(chosenOrder, pdfFile);
                model.addAttribute("resentStatus", "RESENT OK, inclusive PDF invoice");
            } else {
                emailService.resendOrderMail(chosenOrder);
                model.addAttribute("resentStatus", "RESENT OK, but NO PDF invoice");
            }
        } catch (Exception ex) {
            model.addAttribute("resentStatus", ex.getMessage());
        }
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("chosenOrder", chosenOrder);
        return "orderTable";
    }


    @PostMapping("/updateOrder")
    public String updateOrder(@ModelAttribute("chosenOrder") Order order) {
        Order myOrder = orderService.findById(order.getId());
        myOrder.setStatus(order.getStatus());
        orderService.saveOrder(myOrder);
        return "redirect:/showOrders";
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@ModelAttribute("chosenOrder") Order order) {
        Order myOrder = orderService.findById(order.getId());
        if (myOrder.getStatus() == Constants.ORDER_ARCHIVED) {
            orderService.deleteOrder(myOrder.getId());
            for (OrderDetails orderDetails : myOrder.getOrderDetails()) {
                productService.updateStock(orderDetails.getProduct().getProductDetails(), orderDetails.getQuantity());
            }
        }
        return "redirect:/showOrders";
    }


    @PostMapping("/newAdmin")
    public String register(@Valid @ModelAttribute("newUser") WebUserModel newUser,
                           BindingResult result,
                           Model model,
                           HttpServletRequest request) {
        if(result.hasErrors()) {
            return "adminWebUsers";
        }
        WebUser webUser = new WebUser(newUser.getUsername(),
                newUser.getEmail().toLowerCase(),
                "TEST",
                newUser.getRole(),
                false,
                new Date(),
                0);

        webUserRepository.save(webUser);
        eventPublisher.publishEvent(new UserRegistrationEvent(webUser, "newAdminVerification", request));
        return "redirect:webusers?adminCreated="+newUser.getUsername();

    }

    @RequestMapping("/sessions")
    public String getSessions(Model model) {
        model.addAttribute("sessions", HttpSessionConfig.getActiveSessions());
        return "sessions";
    }

    @GetMapping("/removeSession")
    public String removeSession(@RequestParam String sessionId){
        if (sessionId != null) {
            List<HttpSession> actSessions = HttpSessionConfig.getActiveSessions();
            for (HttpSession session : actSessions) {
                if (sessionId.equals(session.getId())) {
                    session.invalidate();
                }
            }
        }
        return "redirect:/sessions";
    }

    @GetMapping("/webusers")
    public String allWebUsers(Model model) {
        model.addAttribute("newUser", new WebUserModel());
        model.addAttribute("webUser", new WebUser());
        List<WebUser> webusers = StreamSupport.stream(webUserRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        model.addAttribute("webUsers", webusers);
        return "adminWebUsers";
    }


    @PostMapping("/removeWebUser")
    public String removeWebUser(@ModelAttribute("webUser") WebUser webUser,
                                BindingResult result,
                                Model model) {
        WebUser webuserToRemove = webUserRepository.findByUsername(webUser.getUsername());
        if (webuserToRemove!=null) {
            Customer customerToRemove = customerService.findByUsername(webuserToRemove.getUsername());
            customerService.deleteCustomer(customerToRemove);
            webUserRepository.delete(webuserToRemove);
            persistentTokenRepository.removeUserTokens(webuserToRemove.getUsername());
            destroyAllUserSessions(webuserToRemove.getUsername());
        }
        return "redirect:/webusers";
    }

    private List<CategoryDisplay> getCategoriesForAdmin() {
        List<Category> catList = categoryService.findAll();
        Map<String, CategoryDisplay> catMap = new TreeMap<>();
        for (Category cat : catList) {
            String key = cat.getCode();
            if (catMap.containsKey(key)) {
                catMap.get(key).getNames().put(cat.getLang().getLanguage().toUpperCase(), cat.getName());
                catMap.get(key).getIds().put(cat.getLang().getLanguage().toUpperCase(), cat.getId());
            } else {
                String upperCatCode = "";
                if (cat.getUpperCategory() != null)
                    upperCatCode = cat.getUpperCategory().getCode();
                catMap.put(key, new CategoryDisplay(cat.getCode(), upperCatCode,
                        cat.getLang().getLanguage().toUpperCase(), cat.getName(), cat.getId()));
            }

        }
        List<CategoryDisplay> categories = new ArrayList<>(catMap.values());
        return categories;
    }


    private Map<Long ,String>  getOrderCats() {
        List<Category> cats = categoryService.findMainCategories(getLocale());
        Map<Long, String> map = new LinkedHashMap<Long, String>();
        return getOrderCatsSubRoutine("", map, cats);

    }
    private Map<Long, String> getOrderCatsSubRoutine(String addition, Map<Long, String> map, List<Category> cats){
        for (Category cat : cats){
            map.put(cat.getId(), addition + cat.getFullName());
            if (!cat.getSubCategories().isEmpty()) {
                getOrderCatsSubRoutine(addition , map, cat.getSubCategories());
            }
        }
        return map;
    }

    private Map<String, Long> getCategoryIdCodeValuesLang(Locale loc){
        Map<String, Long> result = new TreeMap<>();
        Iterable<Category> catIterable = categoryService.listCategories(loc);
        Iterator<Category> catIt = catIterable.iterator();
        while (catIt.hasNext()) {
            Category cat = catIt.next();
            result.put(cat.getCode(), cat.getId());
        }
        return result;
    }


    private List<CountryDisplay> getCountriesForAdmin() {
        List<Country> countryList = countryService.findAllCountries();
        Map<String, CountryDisplay> countryMap = new TreeMap<>();
        for (Country country : countryList) {
            String key = country.getCode();
            if (countryMap.containsKey(key)) {
                countryMap.get(key).getNames().put(country.getLang().getLanguage().toUpperCase(), country.getCountry());
                countryMap.get(key).getIds().put(country.getLang().getLanguage().toUpperCase(), country.getId());
            } else {
                countryMap.put(key, new CountryDisplay(country.getCode(), country.getLang().getLanguage().toUpperCase(),
                        country.getCountry(), country.getId()));
            }

        }
        List<CountryDisplay> countries = new ArrayList<>(countryMap.values());
        return countries;
    }

//    public void clearCache(){
//        for(String name:cacheManager.getCacheNames()){
//            cacheManager.getCache(name).clear();            // clear cache by name
//        }
//
//    }
//
//
//    private void copyFile(MultipartFile file) throws IOException {
//        String fileName = FilenameUtils.getName(file.getOriginalFilename());
//        File squareFile = new File(UPLOADED_FOLDER + SUB_FOLDER + fileName);
//        ImageIO.write(cropImageSquare(file), "jpg", squareFile);
////        FileCopyUtils.copy(squareFile, new File(UPLOADED_FOLDER + SUB_FOLDER + fileName));
////        FileCopyUtils.copy(file.getBytes(), new File(UPLOADED_FOLDER + SUB_FOLDER + fileName));
//    }
//
//    private BufferedImage cropImageSquare(MultipartFile file) throws IOException {
//        // Get a BufferedImage object from a byte array
//        InputStream in = new ByteArrayInputStream(file.getBytes());
//        BufferedImage originalImage = ImageIO.read(in);
//
//        // Get image dimensions
//        int height = originalImage.getHeight();
//        int width = originalImage.getWidth();
//
//        // The image is already a square
//        if (height == width) {
//            return originalImage;
//        }
//
//        // Compute the size of the square
//        int squareSize = (height > width ? width : height);
//
//        // Coordinates of the image's middle
//        int xc = width / 2;
//        int yc = height / 2;
//
//        // Crop
//        BufferedImage croppedImage = originalImage.getSubimage(
//                xc - (squareSize / 2), // x coordinate of the upper-left corner
//                yc - (squareSize / 2), // y coordinate of the upper-left corner
//                squareSize,            // width
//                squareSize             // height
//        );
//
//        return croppedImage;
//    }

    private List<Order> getOrders(HttpServletRequest request, Integer statusIn, String search) {
        Integer status = (Integer) Utils.getOrSetAttributeInSession(request, "orderStatus", statusIn, 100);
        if (status == 100) {
            if (search != null)
                return orderService.findOrdersByCustomerData(search);
            else
                return  orderService.getAllOrders();
        } else {
            if (search != null)
                return orderService.findOrdersByStatusAndCustomerData(status, search);
            else
                return orderService.findOrdersByStatus(status);
        }
    }

    private Locale getLocale() {
        return new Locale(LocaleContextHolder.getLocale().getLanguage());
    }

    private void destroyAllUserSessions(String user) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (user.equals(userDetails.getUsername())) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
        for (HttpSession session : HttpSessionConfig.getActiveSessions()) {
            if (user.equals(session.getAttribute(AuthenticationSuccessHandlerImpl.USERNAME)))
                session.invalidate();
        }
    }


//    private List<HttpSession> setSessionAttribute(List<HttpSession> sessions) {
//        for (HttpSession session : sessions) {
//            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
//            if (sessionInformation != null) {
//                Object principal = sessionInformation.getPrincipal();
//                if (principal instanceof UserDetails) {
//                    UserDetails userDetails = (UserDetails) principal;
//                    session.setAttribute("username", userDetails.getUsername());
//                    session.setAttribute("role", userDetails.getAuthorities().toString());
//                }
//            }
//        }
//        return sessions;
//    }


}
