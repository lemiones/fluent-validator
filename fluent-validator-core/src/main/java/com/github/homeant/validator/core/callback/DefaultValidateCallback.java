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
package com.github.homeant.validator.core.callback;

import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.validator.element.ValidatorElementList;
import com.github.homeant.validator.core.exception.ValidateFailException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 默认校验回调
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 16:38:04
 */
@Slf4j
public class DefaultValidateCallback implements ValidateCallback {

	@Override
	public void onSuccess(ValidatorElementList validatorElementList) {

	}

	@Override
	public void onFail(ValidatorElementList validatorElementList, List<ValidationError> errors) {
		Object clazz = validatorElementList.getAllValidatorElements().get(0).getTarget();
		ValidateFailException exception = new ValidateFailException(clazz.getClass() + " validated failures...");
		exception.setErrors(errors);
		throw exception;
	}

	@Override
	public void onUncaughtException(Validator validator, Exception e, Object target) throws Exception {
		log.error("Failed to validating the object:{},  error:{}", target, e.getMessage());
		throw e;
	}

}
