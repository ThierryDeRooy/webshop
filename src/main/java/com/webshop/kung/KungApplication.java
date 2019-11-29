package com.webshop.kung;

import com.webshop.kung.constants.Constants;
import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;
import com.webshop.kung.repository.CategoryRepository;
import com.webshop.kung.repository.ProductDetailsRepository;
import com.webshop.kung.service.CountryService;
import com.webshop.kung.service.OrderDetailsService;
import com.webshop.kung.service.OrderService;
import com.webshop.kung.service.ProductService;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
@EnableCaching
public class KungApplication {

	private static final Logger log = LoggerFactory.getLogger(KungApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KungApplication.class, args);
	}

	@Bean
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

/*	private Connector redirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setRedirectPort(8443);
		return connector;
	}
*/


	@Bean
	public CommandLineRunner demo(CategoryRepository repository, ProductService productService, ProductDetailsRepository productDetailsRepository, OrderDetailsService orderDetailsService) {
		return (args) -> {
//			Category cat = new Category(0L, "comcumber");
			Category cat = new Category(null, new Locale("EN"),"fruit");
			Category catNL = new Category(null, new Locale("NL"),"fruit");
			repository.save(cat);
			repository.save(catNL);
			cat = new Category(null, new Locale("EN"),  "vegetables");
			catNL = new Category(null, new Locale("NL"),  "groenten");
			repository.save(cat);
			repository.save(catNL);
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

			Locale lang = new Locale("en");
			ProductDetails prodDet = new ProductDetails("A125", new BigDecimal(12.11), Constants.PER_UNIT, "images/imag3.jpg", Constants.PRODUCT_ACTIVE);
			productDetailsRepository.save(prodDet);
			Product prodEn = new Product(cat, lang, "red beat", "nice for eating",prodDet);
			productService.addProduct(prodEn);
			Product prodNl = new Product(catNL, new Locale("nl"), "rode biet", "goed voor eten",prodDet);
			productService.addProduct(prodNl);

			prodDet = new ProductDetails("B125", new BigDecimal(42.41), Constants.PER_WEIGHT_KG, "images/imag2.jpg", Constants.PRODUCT_ACTIVE);
			productDetailsRepository.save(prodDet);
			prodEn = new Product(cat, lang, "red weed", "nice for sniffing",prodDet);
			productService.addProduct(prodEn);
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

