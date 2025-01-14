/**
 * Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.homeant.validator.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 国际化实体对象
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 13:58:57
 */
@Data
public class MessageResource implements Serializable {

	private static final long serialVersionUID = 5085324419668041501L;

	private String message;

	private String code;

	private String language;

	private String site;
}
