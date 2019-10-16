import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zoctan.api.core.constant.ProjectConstant.*;

/**
 * 代码生成器 根据数据表名称生成对应的 Entity、Mapper、Service、Controller 简化开发
 *
 * @author Zoctan
 * @date 2018/05/27
 */
class CodeGenerator {
  // JDBC配置，请修改为你项目的实际配置
  private static final String JDBC_URL =
      "jdbc:mysql://localhost:3306/admin_test"
          + "?useUnicode=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC";
  private static final String JDBC_USERNAME = "root";
  private static final String JDBC_PASSWORD = "root";
  private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  // 项目在硬盘上的基础路径
  private static final String PROJECT_PATH = System.getProperty("user.dir");
  // 模板位置
  private static final String TEMPLATE_FILE_PATH =
      CodeGenerator.PROJECT_PATH + "/src/test/resources/generator/template";
  // java文件路径
  private static final String JAVA_PATH = "/src/main/java";
  // 资源文件路径
  private static final String RESOURCES_PATH = "/src/main/resources";
  // 生成的Service存放路径
  private static final String PACKAGE_PATH_SERVICE =
      CodeGenerator.packageConvertPath(SERVICE_PACKAGE);
  // 生成的Service实现存放路径
  private static final String PACKAGE_PATH_SERVICE_IMPL =
      CodeGenerator.packageConvertPath(SERVICE_IMPL_PACKAGE);
  // 生成的Controller存放路径
  private static final String PACKAGE_PATH_CONTROLLER =
      CodeGenerator.packageConvertPath(CONTROLLER_PACKAGE);

  // @author
  private static final String AUTHOR = "Zoctan";
  // @date
  private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
  private static final boolean isRestful = true;

  public static void main(final String[] args) {
    final Scanner scanner = new Scanner(System.in);
    System.out.print("可能已存在相关文件，请尽可能确保无误。y/n:");
    if (!scanner.next().equals("y")) {
      return;
    }
    CodeGenerator.genCode("report", "wechat");
    // genCodeByCustomModelName("输入表名","输入自定义Model名称");
  }

  /**
   * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。 如输入表名称 "t_user_detail" 将生成
   * TUserDetail、TUserDetailMapper、TUserDetailService ...
   *
   * @param tableNames 数据表名称...
   */
  private static void genCode(final String... tableNames) {
    for (final String tableName : tableNames) {
      CodeGenerator.genCodeByCustomModelName(tableName, null);
    }
  }

  /**
   * 通过数据表名称，和自定义的 Model 名称生成代码 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "sysUser" 将生成
   * sysUser、UserMapper、UserService ...
   *
   * @param tableName 数据表名称
   * @param modelName 自定义的 Model 名称
   */
  private static void genCodeByCustomModelName(final String tableName, final String modelName) {
    CodeGenerator.genModelAndMapper(tableName, modelName);
    CodeGenerator.genService(tableName, modelName);
    CodeGenerator.genController(tableName, modelName);
  }

  private static void genModelAndMapper(final String tableName, String modelName) {
    final Context context = new Context(ModelType.FLAT);
    context.setId("Potato");
    context.setTargetRuntime("MyBatis3Simple");
    context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
    context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

    final JDBCConnectionConfiguration jdbcConnectionConfiguration =
        new JDBCConnectionConfiguration();
    jdbcConnectionConfiguration.setConnectionURL(CodeGenerator.JDBC_URL);
    jdbcConnectionConfiguration.setUserId(CodeGenerator.JDBC_USERNAME);
    jdbcConnectionConfiguration.setPassword(CodeGenerator.JDBC_PASSWORD);
    jdbcConnectionConfiguration.setDriverClass(CodeGenerator.JDBC_DIVER_CLASS_NAME);
    context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

    final PluginConfiguration pluginConfiguration = new PluginConfiguration();
    pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
    pluginConfiguration.addProperty("mappers", MAPPER_INTERFACE_REFERENCE);
    context.addPluginConfiguration(pluginConfiguration);

    final JavaModelGeneratorConfiguration javaModelGeneratorConfiguration =
        new JavaModelGeneratorConfiguration();
    javaModelGeneratorConfiguration.setTargetProject(
        CodeGenerator.PROJECT_PATH + CodeGenerator.JAVA_PATH);
    javaModelGeneratorConfiguration.setTargetPackage(ENTITY_PACKAGE);
    context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

    final SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration =
        new SqlMapGeneratorConfiguration();
    sqlMapGeneratorConfiguration.setTargetProject(
        CodeGenerator.PROJECT_PATH + CodeGenerator.RESOURCES_PATH);
    sqlMapGeneratorConfiguration.setTargetPackage("mapper");
    context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

    final JavaClientGeneratorConfiguration javaClientGeneratorConfiguration =
        new JavaClientGeneratorConfiguration();
    javaClientGeneratorConfiguration.setTargetProject(
        CodeGenerator.PROJECT_PATH + CodeGenerator.JAVA_PATH);
    javaClientGeneratorConfiguration.setTargetPackage(MAPPER_PACKAGE);
    javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
    context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

    final TableConfiguration tableConfiguration = new TableConfiguration(context);
    tableConfiguration.setTableName(tableName);
    if (StringUtils.isNotEmpty(modelName)) {
      tableConfiguration.setDomainObjectName(modelName);
    }
    tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
    context.addTableConfiguration(tableConfiguration);

    final List<String> warnings;
    final MyBatisGenerator generator;
    try {
      final Configuration config = new Configuration();
      config.addContext(context);
      config.validate();

      final DefaultShellCallback callback = new DefaultShellCallback(true);
      warnings = new ArrayList<>();
      generator = new MyBatisGenerator(config, callback, warnings);
      generator.generate(null);
    } catch (final Exception e) {
      throw new RuntimeException("生成 Model 和 Mapper 失败", e);
    }

    if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
      throw new RuntimeException("生成 Model 和 Mapper 失败：" + warnings);
    }
    if (StringUtils.isEmpty(modelName)) {
      modelName = CodeGenerator.tableNameConvertUpperCamel(tableName);
    }
    System.out.println(modelName + ".java 生成成功");
    System.out.println(modelName + "MyMapper.java 生成成功");
    System.out.println(modelName + "MyMapper.xml 生成成功");
  }

  private static void genService(final String tableName, final String modelName) {
    try {
      final freemarker.template.Configuration cfg = CodeGenerator.getConfiguration();

      final Map<String, Object> data = new HashMap<>();
      data.put("date", CodeGenerator.DATE);
      data.put("author", CodeGenerator.AUTHOR);
      final String modelNameUpperCamel =
          StringUtils.isEmpty(modelName)
              ? CodeGenerator.tableNameConvertUpperCamel(tableName)
              : modelName;
      data.put("modelNameUpperCamel", modelNameUpperCamel);
      data.put("modelNameLowerCamel", CodeGenerator.tableNameConvertLowerCamel(tableName));
      data.put("basePackage", BASE_PACKAGE);

      final File file =
          CodeGenerator.createFileDir(
              CodeGenerator.PROJECT_PATH
                  + CodeGenerator.JAVA_PATH
                  + CodeGenerator.PACKAGE_PATH_SERVICE
                  + modelNameUpperCamel
                  + "Service.java");
      cfg.getTemplate("service.ftl").process(data, new FileWriter(file));
      System.out.println(modelNameUpperCamel + "Service.java 生成成功");

      final File file1 =
          CodeGenerator.createFileDir(
              CodeGenerator.PROJECT_PATH
                  + CodeGenerator.JAVA_PATH
                  + CodeGenerator.PACKAGE_PATH_SERVICE_IMPL
                  + modelNameUpperCamel
                  + "ServiceImpl.java");
      cfg.getTemplate("service-impl.ftl").process(data, new FileWriter(file1));
      System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
    } catch (final Exception e) {
      throw new RuntimeException("生成Service失败", e);
    }
  }

  private static File createFileDir(final String name) throws RuntimeException {
    final File file = new File(name);
    if (!file.getParentFile().exists()) {
      final boolean createSuccess = file.getParentFile().mkdirs();
      if (!createSuccess) {
        throw new RuntimeException("文件夹创建失败");
      }
    }
    return file;
  }

  private static void genController(final String tableName, final String modelName) {
    try {
      final freemarker.template.Configuration cfg = CodeGenerator.getConfiguration();

      final Map<String, Object> data = new HashMap<>();
      data.put("date", CodeGenerator.DATE);
      data.put("author", CodeGenerator.AUTHOR);
      final String modelNameUpperCamel =
          StringUtils.isEmpty(modelName)
              ? CodeGenerator.tableNameConvertUpperCamel(tableName)
              : modelName;
      data.put(
          "baseRequestMapping", CodeGenerator.modelNameConvertMappingPath(modelNameUpperCamel));
      data.put("modelNameUpperCamel", modelNameUpperCamel);
      data.put(
          "modelNameLowerCamel",
          CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
      data.put("basePackage", BASE_PACKAGE);

      final File file =
          CodeGenerator.createFileDir(
              CodeGenerator.PROJECT_PATH
                  + CodeGenerator.JAVA_PATH
                  + CodeGenerator.PACKAGE_PATH_CONTROLLER
                  + modelNameUpperCamel
                  + "Controller.java");

      if (CodeGenerator.isRestful) {
        cfg.getTemplate("controller-restful.ftl").process(data, new FileWriter(file));
      } else {
        cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));
      }
      System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
    } catch (final Exception e) {
      throw new RuntimeException("生成Controller失败", e);
    }
  }

  private static freemarker.template.Configuration getConfiguration() throws IOException {
    final freemarker.template.Configuration cfg =
        new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
    cfg.setDirectoryForTemplateLoading(new File(CodeGenerator.TEMPLATE_FILE_PATH));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
    return cfg;
  }

  private static String tableNameConvertLowerCamel(final String tableName) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
  }

  private static String tableNameConvertUpperCamel(final String tableName) {
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());
  }

  private static String tableNameConvertMappingPath(String tableName) {
    tableName = tableName.toLowerCase(); // 兼容使用大写的表名
    return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
  }

  private static String modelNameConvertMappingPath(final String modelName) {
    final String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
    return CodeGenerator.tableNameConvertMappingPath(tableName);
  }

  private static String packageConvertPath(final String packageName) {
    return String.format(
        "/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
  }
}
