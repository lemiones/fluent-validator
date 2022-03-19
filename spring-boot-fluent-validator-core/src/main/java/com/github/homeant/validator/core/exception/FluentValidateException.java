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
package com.github.homeant.validator.core.exception;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 校验异常类
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 16:40:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FluentValidateException extends RuntimeException {

	private static final long serialVersionUID = 6113950199369314904L;

	private List<ValidationError> errors;

	public FluentValidateException(String message, ValidationError... errors) {
		super(message);
		this.errors = resolvingValidationErrors(errors);
	}

	public FluentValidateException(Throwable e) {
		super(e);
	}

	public FluentValidateException(String message, Throwable e) {
		super(message, e);
	}

	private List<ValidationError> resolvingValidationErrors(ValidationError... errors) {
		List<ValidationError> validationErrors = new ArrayList<>();
		if (errors != null && errors.length > 0) {
			for (ValidationError error : errors) {
				if (error != null) {
					validationErrors.add(error);
				}
			}
		}
		return validationErrors;
	}
}
