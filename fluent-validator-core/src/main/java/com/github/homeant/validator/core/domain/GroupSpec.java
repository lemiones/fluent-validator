package com.github.homeant.validator.core.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * GroupSpec
 *
 * @author junchen
 * @date 2019-12-29 09:34
 */
@Data
@ToString
public class GroupSpec implements Serializable {
	/**
	 * 条件表达式
	 */
	private String expression;
}
