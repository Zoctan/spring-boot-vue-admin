package com.zoctan.api.core.constant;

/**
 * 项目常量
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public final class ProjectConstant {
  /** 开发环境 */
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

  /** 生产环境 */
  public static final String SPRING_PROFILE_PRODUCTION = "prod";

  /** 测试环境 */
  public static final String SPRING_PROFILE_TEST = "test";

  /** 项目基础包名称 */
  public static final String BASE_PACKAGE = "com.zoctan.api";

  /** Entity 所在包 */
  public static final String ENTITY_PACKAGE = BASE_PACKAGE + ".entity";

  /** Mapper 所在包 */
  public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";

  /** Filter 所在包 */
  public static final String FILTER_PACKAGE = BASE_PACKAGE + ".filter";

  /** Service 所在包 */
  public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";

  /** ServiceImpl 所在包 */
  public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";

  /** Controller 所在包 */
  public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";

  /** Mapper 插件基础接口的完全限定名 */
  public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.mapper.MyMapper";
}
