package com.solace.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.solace.logging.Logger;

/**
 * Utility class to access the {@link ApplicationContext} as a singleton object
 * 
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 * 
 */
public class Context {

	public static final String[] CONFIG_FILES = new String[] { "applicationContext.xml" };

	static final Logger LOGGER = Logger.getLogger(Context.class);

	private static ApplicationContext appCtx;

	public synchronized static ApplicationContext getApplicationContext() {
		try {
			if (appCtx == null) {
				LOGGER.debug("Initializing Application Context");
				appCtx = new ClassPathXmlApplicationContext(CONFIG_FILES);
				LOGGER.debug("finished  Application Context Initialization");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return appCtx;
	}

	public synchronized static void setApplicationContext(
			ApplicationContext context) {
		Context.appCtx = context;
	}

	@SuppressWarnings("unchecked")
	public static <T> T loadFromContext(final Class<?> _class,
			int autoWireType, boolean autoWire) throws Exception {

		return loadFromContext(Context.getApplicationContext(), _class,
				autoWireType, autoWire);
	}

	@SuppressWarnings("unchecked")
	public static <T> T loadFromContext(final ApplicationContext context,
			final Class<?> _class, int autoWireType, boolean autoWire)
			throws Exception {

		LOGGER.info("Attempting to {} a {} from the context....",
				autoWire ? "autowire" : "load", _class.getName());

		T t = null;
		if (autoWire) {
			t = (T) context.getAutowireCapableBeanFactory().autowire(_class,
					autoWireType, true);

			context.getAutowireCapableBeanFactory().autowireBean(t);

			if (_class.isAnnotationPresent(DependencyRequiredClasses.class)) {
				DependencyRequiredClasses required = _class
						.getAnnotation(DependencyRequiredClasses.class);

				if (required.required() != null)
					for (DependencyRequired d : required.required()) {

						Object o = null;
						
						if (d.scope() == EDependencyScope.Singleton && (o = context.getBean(d.instanceType())) == null) {
							LOGGER.info("Loading a s{} for {}",
									d.instanceType(), t.getClass());

							o = Context
									.loadFromContext(
											context,
											d.instanceType(),
											AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR,
											true);
						} else if (d.scope() == EDependencyScope.Prototype ) {
							o = Context
									.loadFromContext(
											context,
											d.instanceType(),
											AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR,
											true);
						} else {
							LOGGER.info("An instance of {} was discovered in the context", d.instanceType().getName());							
						}

						String method = d.setter() == null
								|| d.setter().trim().length() == 0 ? "set"
								+ o.getClass().getSimpleName() : d.setter();

						Class<?> inputType = d.castTo().equals(Class.class) ? o
								.getClass() : d.castTo();

						LOGGER.info("Interrogating {} for {}({})", o
								.getClass().getName(), method, inputType
								.getName());

						Method toBeSet = t.getClass().getMethod(
								method,
								d.castTo().equals(Class.class) ? o.getClass()
										: d.castTo());

						if (method == null)
							throw new RuntimeException(
									String.format(
											"{} is annotated with RequiredClasses for property injection of {}, it did not have a setter of {} to be set.",
											t.getClass(), d.instanceType(),
											method));

						LOGGER.info("Setting an instance of {} on {}.{}",
								o.getClass(), t.getClass(), method);

						if (!d.castTo().equals(Class.class))
							toBeSet.invoke(t, d.castTo().cast(o));
						else
							toBeSet.invoke(t, o);
					}
			}
		} else
			t = (T) context.getBean(_class);

		return t;
	}
}
