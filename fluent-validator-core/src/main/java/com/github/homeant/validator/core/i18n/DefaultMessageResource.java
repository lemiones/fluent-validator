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
package com.github.homeant.validator.core.i18n;

import com.github.homeant.validator.core.domain.MessageResource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态国际化
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-07 10:55:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultMessageResource extends AbstractMessageSource implements ResourceLoaderAware, InitializingBean {

	private ResourceLoader resourceLoader;

	private final MessageProvider messageService;

	private static final Map<String, Map<Locale, MessageFormat>> LOCAL_CACHE = new ConcurrentHashMap<>(256);

	public void reload() {
		LOCAL_CACHE.clear();
		List<MessageResource> list = messageService.getAllMessage();
		Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(r -> {
			Locale locale = parseLocaleValue(r.getLanguage());
			Map<Locale, MessageFormat> map = LOCAL_CACHE.getOrDefault(r.getCode(), new ConcurrentHashMap<>());
			map.put(locale, createMessageFormat(r.getMessage(), locale));
			LOCAL_CACHE.put(r.getCode(), map);
		});

	}

	@Override
	public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	protected MessageFormat resolveCode(@Nullable String code, @Nullable Locale locale) {
		Map<Locale, MessageFormat> map = LOCAL_CACHE.get(code);
		if (map != null) {
			return map.get(locale);
		}
		return null;
	}

	private Locale parseLocaleValue(String locale) {
		return StringUtils.parseLocaleString(locale);
	}

	/**
	 * Invoked by the containing {@code BeanFactory} after it has set all bean properties
	 * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
	 * <p>This method allows the bean instance to perform validation of its overall
	 * configuration and final initialization when all bean properties have been set.
	 */
	@Override
	public void afterPropertiesSet() {
		this.reload();
	}
}
