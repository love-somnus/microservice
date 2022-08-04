/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.somnus.microservice.commons.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.dto
 * @title: LoginAuthDto
 * @description: TODO
 * @date 2019/3/28 15:27
 */
@Data
public class BaseQuery implements Serializable {

	private static final long serialVersionUID = 3319698607712846427L;

	/**
	 * 当前页
	 */
	@Schema(title = "当前页", required = false, example = "1")
	private Integer pageNum = 1;

	/**
	 * 每页条数
	 */
	@Schema(title = "每页条数", required = false, example = "10")
	private Integer pageSize = 10;

}
