package com.solace.support;

import java.util.Locale;

import com.solace.logging.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticMessageSource;

/**
 * Hijacked from Spring to see if we can register and actual instance in for singletons
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 *
 */
public class ConfigurableApplicationContext extends AnnotationConfigApplicationContext {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurableApplicationContext.class);

	private final StaticMessageSource staticMessageSource;

	/**
	 * Create a new StaticApplicationContext.
	 * 
	 * @see #registerSingleton
	 * @see #registerPrototype
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public ConfigurableApplicationContext() throws BeansException {
		this((ApplicationContext)null);
		LOGGER.info("Invoking default ctor");
	}
	
	
	public ConfigurableApplicationContext(String... basePackages) throws BeansException {
		super(basePackages);
		LOGGER.info("Invoking ctor with base packages");
		this.staticMessageSource = new StaticMessageSource();
		getBeanFactory().registerSingleton(MESSAGE_SOURCE_BEAN_NAME,
				this.staticMessageSource);

	}

	/**
	 * Create a new StaticApplicationContext with the given parent.
	 * 
	 * @see #registerSingleton
	 * @see #registerPrototype
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public ConfigurableApplicationContext(ApplicationContext parent)
			throws BeansException {
		LOGGER.infoFormat("Invoking ctor with a context");
		// Initialize and register a StaticMessageSource.
		this.staticMessageSource = new StaticMessageSource();
		getBeanFactory().registerSingleton(MESSAGE_SOURCE_BEAN_NAME,
				this.staticMessageSource);
	}

	/**
	 * Return the internal StaticMessageSource used by this context. Can be used
	 * to register messages on it.
	 * 
	 * @see #addMessage
	 */
	public final StaticMessageSource getStaticMessageSource() {
		return this.staticMessageSource;
	}

	/**
	 * Register a singleton bean with the underlying bean factory.
	 * <p>
	 * For more advanced needs, register with the underlying BeanFactory
	 * directly.
	 * 
	 * @see #getDefaultListableBeanFactory
	 */
	public void registerSingleton(String name, Class clazz)
			throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);		
	}

	/**
	 * Register a singleton bean with the underlying bean factory.
	 * <p>
	 * For more advanced needs, register with the underlying BeanFactory
	 * directly.
	 * 
	 * @see #getDefaultListableBeanFactory
	 */
	public void registerSingleton(String name, Class clazz,
			MutablePropertyValues pvs) throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		bd.setPropertyValues(pvs);
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
	}
	
	
	public void registerSingleton(String name, Class clazz,
			ConstructorArgumentValues ctorArgs) throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		bd.setConstructorArgumentValues(ctorArgs);		
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);		
	}
	
	
	public void registerSingleton(String name, Object o) throws BeansException {
		LOGGER.infoFormat("Registering a %s with name %s as singleton", o.getClass().getName(), name);
		getDefaultListableBeanFactory().registerSingleton(name, o);		
	}

	/**
	 * Register a prototype bean with the underlying bean factory.
	 * <p>
	 * For more advanced needs, register with the underlying BeanFactory
	 * directly.
	 * 
	 * @see #getDefaultListableBeanFactory
	 */
	public void registerPrototype(String name, Class clazz)
			throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setScope(GenericBeanDefinition.SCOPE_PROTOTYPE);
		bd.setBeanClass(clazz);
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
	}

	/**
	 * Register a prototype bean with the underlying bean factory.
	 * <p>
	 * For more advanced needs, register with the underlying BeanFactory
	 * directly.
	 * 
	 * @see #getDefaultListableBeanFactory
	 */
	public void registerPrototype(String name, Class clazz,
			MutablePropertyValues pvs) throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setScope(GenericBeanDefinition.SCOPE_PROTOTYPE);
		bd.setBeanClass(clazz);
		bd.setPropertyValues(pvs);
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
	}
	
	
	public void registerPrototype(String name, Class clazz,
			ConstructorArgumentValues ctorArgs) throws BeansException {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		bd.setScope(GenericBeanDefinition.SCOPE_PROTOTYPE);
		bd.setConstructorArgumentValues(ctorArgs);		
		getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
	}

	/**
	 * Associate the given message with the given code.
	 * 
	 * @param code
	 *            lookup code
	 * @param locale
	 *            locale message should be found within
	 * @param defaultMessage
	 *            message associated with this lookup code
	 * @see #getStaticMessageSource
	 */
	public void addMessage(String code, Locale locale, String defaultMessage) {
		getStaticMessageSource().addMessage(code, locale, defaultMessage);
	}
}
