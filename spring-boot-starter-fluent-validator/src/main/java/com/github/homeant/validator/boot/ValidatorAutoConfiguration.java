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
package com.github.homeant.validator.boot;


import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.interceptor.FluentValidateInterceptor;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.baidu.unbiz.fluentvalidator.support.MethodNameFluentValidatorPostProcessor;
import com.github.homeant.validator.ValidatorProperties;
import com.github.homeant.validator.core.callback.DefaultValidateCallback;
import com.github.homeant.validator.core.i18n.DefaultMessageResource;
import com.github.homeant.validator.core.i18n.MessageProvider;
import com.github.homeant.validator.core.spring.ValidatorBeanPostProcessor;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.validation.Validator;

/**
 * validator auto config
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:41:18
 */
@Data
@Configuration
@ConditionalOnClass(value = { com.baidu.unbiz.fluentvalidator.Validator.class })
@ConditionalOnProperty(prefix = ValidatorProperties.PREFIX, value = "enable", matchIfMissing = true, havingValue = "true")
@AutoConfigureBefore({ MessageSourceAutoConfiguration.class })
@EnableConfigurationProperties({ ValidatorProperties.class })
public class ValidatorAutoConfiguration {

	private final ValidatorProperties validatorProperties;

	private final Validator validator;

	/**
	 * 国际化资源
	 *
	 * @return MessageSource
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:04:51
	 */
	@Bean
	@ConditionalOnBean(MessageProvider.class)
	public MessageSource messageSource(MessageProvider messageProvider, MessageSource messageSource) {
		DefaultMessageResource resource = new DefaultMessageResource(messageProvider);
		resource.setParentMessageSource(messageSource);
		return resource;
	}

	@Bean
	@ConditionalOnMissingBean(MessageProvider.class)
	public MessageSupport messageSupport(MessageSource messageSource) {
		MessageSupport support = new MessageSupport();
		support.setMessageSource(messageSource);
		return support;
	}

	/**
	 * 校验回调
	 *
	 * @return ValidateCallback
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:04:11
	 */
	@Bean
	@ConditionalOnMissingBean(ValidateCallback.class)
	public ValidateCallback callback() {
		return new DefaultValidateCallback();
	}

	@Bean
	@ConditionalOnBean({ ValidateCallback.class })
	public FluentValidateInterceptor fluentValidateInterceptor(ValidateCallback callback) {
		FluentValidateInterceptor validateInterceptor = new FluentValidateInterceptor();
		validateInterceptor.setFluentValidatorPostProcessor(new MethodNameFluentValidatorPostProcessor());
		validateInterceptor.setCallback(callback);
		validateInterceptor.setValidator(validator);
		validateInterceptor.setHibernateDefaultErrorCode(validatorProperties.getHibernateDefaultErrorCode());
		return validateInterceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public ValidatorBeanPostProcessor validatorBeanPostProcessor(
			FluentValidateInterceptor fluentValidateInterceptor, Environment environment
	) {
		ValidatorBeanPostProcessor postProcessor = new ValidatorBeanPostProcessor();
		boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
		postProcessor.setProxyTargetClass(proxyTargetClass);
		postProcessor.setFluentValidateInterceptor(fluentValidateInterceptor);
		return postProcessor;
	}

}
