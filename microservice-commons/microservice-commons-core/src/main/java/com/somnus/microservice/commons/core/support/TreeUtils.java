package com.somnus.microservice.commons.core.support;

import com.somnus.microservice.commons.base.dto.BaseTree;

import java.io.Serializable;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.core.support
 * @title: TreeUtils
 * @description: TODO
 * @date 2019/4/16 17:35
 * @param <T>
 * @param <ID>
 */
public class TreeUtils<T extends BaseTree<T, ID>, ID extends Serializable> extends AbstractTreeService<T, ID> {

}