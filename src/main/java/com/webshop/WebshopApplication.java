package com.webshop;

import com.webshop.constants.Constants;
import com.webshop.entity.Category;
import com.webshop.entity.Product;
import com.webshop.entity.ProductDetails;
import com.webshop.repository.CategoryRepository;
import com.webshop.repository.ProductDetailsRepository;
import com.webshop.repository.TransportCostRepository;
import com.webshop.service.OrderDetailsService;
import com.webshop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
@EnableCaching
public class WebshopApplication {

	private static final Logger log = LoggerFactory.getLogger(WebshopApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebshopApplication.class, args);
	}

/*	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(redirectConnector());
		return tomcat;
	}
*/
/*	private Connector redirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setRedirectPort(8443);
		return connector;
	}
*/



	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource
				= new ReloadableResourceBundleMessageSource();

		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;
	}



	@Bean
	public CommandLineRunner demo(CategoryRepository repository, ProductService productService, ProductDetailsRepository productDetailsRepository,
                                  OrderDetailsService orderDetailsService, TransportCostRepository transportCostRepository) {
		return (args) -> {
//			Category cat = new Category(0L, "comcumber");
			Category catF = new Category(null, new Locale("EN"),"fruit", "FR");
			Category catNL = new Category(null, new Locale("NL"),"fruit", "FR");
			repository.save(catF);
			repository.save(catNL);
			Category cat = new Category(null, new Locale("EN"),  "vegetables", "GR");
			catNL = new Category(null, new Locale("NL"),  "groenten", "GR");
			repository.save(cat);
			repository.save(catNL);
			Category subcat = new Category(cat, new Locale("EN"),  "herbs", "HB");
			repository.save(subcat);

			log.info("**********************************************************************" );
//			try {
				for (Category category : repository.findAll()) {
					log.info("The category is: " + category.getName() + " -- id = " + category.getId());
//				Localised loc = lRepo.findById(1L);
//				log.info("NL description = " + category.getDescription("NL"));
//					log.info("EN description = " + category.getDescription("EN"));
				}
//			} catch (Exception ex) {
//				log.error("EXCEPTION in desplying categories: " + ex.fillInStackTrace());
//				}
			List<Category> catList = repository.findByLangAndName(new Locale("EN"), "banana");
			Iterator<Category> itCat = catList.listIterator();
			while (itCat.hasNext()) {
				Category category = itCat.next();
				log.info("The found category is: " + category.getName()+ " -- id = "+category.getId());
			}
			BigDecimal eenBigDec = new BigDecimal(1);

			Locale lang = new Locale("en");
			log.info("going to create ProdDetails ---");
			ProductDetails prodDet = new ProductDetails("002", new BigDecimal(12.11), new BigDecimal(9), Constants.PER_UNIT, "lemongrass1.jpg", 8, new BigDecimal(1));
			log.info("ProdDetails created ---");
//			productDetailsRepository.save(prodDet);
			log.info("ProdDetails saved ---");
			Product prodEn = new Product(subcat, lang, "lemon grass", "nice for eating",prodDet);
			productService.updateProduct(lang, prodEn, null, 8);
//			productService.addProduct(prodEn);
			Product prodNl = new Product(catNL, new Locale("nl"), "citroen gras", "goed voor eten",prodDet);
			productService.updateProduct(new Locale("nl"), prodNl, null, 0);
//			productService.addProduct(prodNl);

			prodDet = new ProductDetails("003", new BigDecimal(42.41), new BigDecimal(9.50), Constants.PER_WEIGHT_KG, "chinese-chive-chives.jpg", 7, eenBigDec);
//			productDetailsRepository.save(prodDet);
			prodEn = new Product(cat, lang, "chinese chive", "nice for cooking",prodDet);
			productService.updateProduct(lang, prodEn, null, 7);
//			productService.addProduct(prodEn);

			prodDet = new ProductDetails("A125", new BigDecimal(1.11), new BigDecimal(21), Constants.PER_UNIT, "apple.jpg", 10, new BigDecimal((1.25)));
//			productDetailsRepository.save(prodDet);
			prodEn = new Product(catF, lang, "apple", "healthy",prodDet);
			productService.updateProduct(lang, prodEn, null, 10);
//			productService.addProduct(prodEn);
			prodDet = new ProductDetails("A126", new BigDecimal(0.99), new BigDecimal(21), Constants.PER_UNIT, null, 2, new BigDecimal(1));
//			productDetailsRepository.save(prodDet);
			prodEn = new Product(catF, lang, "peach", "nice",prodDet);
			productService.updateProduct(lang, prodEn, null, 4);
//			productService.addProduct(prodEn);
		};
	}

	/*@Bean
	public CommandLineRunner demo2(CategoryService service) {
		return (args) -> {
			service.addCategory(new Category(0L, "komkommer1", "comcumber", "Thai komkommer"));
			service.addCategory(new Category(0L, "banaan1", "banana", "Thai banaan"));
			Iterable<Category> catIt = service.listCategories();
			for (Category category : catIt)
			{
				log.info("The category is: " + category.getNameNl()+ " -- id = "+category.getId());
			}
		};
	}*/

//	@Bean
//	public LocaleResolver localeResolver() {
//		SessionLocaleResolver slr = new SessionLocaleResolver();
//		slr.setDefaultLocale(Locale.ENGLISH);
//		return slr;
//	}

//	@Bean
//	public LocaleChangeInterceptor localeChangeInterceptor() {
//		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//		lci.setParamName("lang");
//		return lci;
//	}

}

