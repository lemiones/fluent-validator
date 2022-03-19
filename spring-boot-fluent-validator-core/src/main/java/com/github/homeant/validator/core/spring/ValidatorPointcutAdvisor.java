/**
 * Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.homeant.validator.core.spring;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-14 15:06:10
 */
public class ValidatorPointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	private static final long serialVersionUID = 1532844349109915966L;

	private final Advice advice;
	private final Pointcut pointcut;

	/**
	 * 传入你自定义注解和MethodInterceptor实现
	 *
	 * @param annotationType The common interface extended by all annotation types.
	 * @param interceptor    Intercepts calls on an interface on its way to the target.
	 */
	public ValidatorPointcutAdvisor(Class<? extends Annotation> annotationType, MethodInterceptor interceptor) {
		this.advice = interceptor;
		this.pointcut = buildPointcut(annotationType);
	}

	private Pointcut buildPointcut(Class<? extends Annotation> annotationType) {
		Assert.notNull(annotationType, "'annotationTypes' must not be null");
		return new Pointcut() {
			@Override
			@NonNull
			public MethodMatcher getMethodMatcher() {
				return new MethodMatcher() {

					@Override
					public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass, @Nullable Object... args) {
						throw new UnsupportedOperationException("Illegal MethodMatcher usage");
					}

					@Override
					public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
						Parameter[] parameters = method.getParameters();
						boolean b = false;
						for (Parameter parameter : parameters) {
							if (parameter.getAnnotation(annotationType) != null) {
								b = true;
								break;
							}
						}
						return b;
					}

					@Override
					public boolean isRuntime() {
						return false;
					}
				};
			}

			@Override
			@NonNull
			public ClassFilter getClassFilter() {
				return ClassFilter.TRUE;
			}
		};
	}

	@Override
	@NonNull
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	@NonNull
	public Advice getAdvice() {
		return advice;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		if (this.advice instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
		}
	}

}
