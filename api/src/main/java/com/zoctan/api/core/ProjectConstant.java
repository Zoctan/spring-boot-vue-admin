package com.zoctan.api.core;

/**
 * 项目常量
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public final class ProjectConstant {
    // 项目基础包名称
    public static final String BASE_PACKAGE = "com.zoctan.api";
    // Model所在包
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";
    // Mapper所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";
    // Service所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";
    // ServiceImpl所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";
    // Controller所在包
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";
    // Mapper插件基础接口的完全限定名
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.mapper.MyMapper";
}
