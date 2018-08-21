package com.citydo.tools.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.citydo.tools.bean.Column;
import com.citydo.tools.util.MessageUtil;
import com.citydo.tools.util.ResourceUtil;

public class GenerateCodeThread implements Runnable {
	private String dbUrl;
	private String dbUserName;
	private String dbPassword;
	private String tableName;
	private String tableNamePrefix;
	private String mycatColumn;
	private JTextArea result;
	private String dirPath;
	private String staffNo;
	private String entityPackage;
	private String daoPackage;
	private String servicePackage;
	private String namespace;
	private JButton dirButton;
	private JButton start;
	private Connection con = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private String schema = "";
	private String tableComment = "";
	private List<Column> columns = new ArrayList();
	private String tableName2 = "";
	private String entityName = "";
	private String daoName = "";
	private String serviceName = "";
	private String mapperName = "";

	public GenerateCodeThread() {
	}

	@Override
	public void run() {
		this.init();

		try {
			this.con = this.getConnection();
			if (this.con != null) {
				this.stat = this.con.createStatement();
				if (this.stat != null) {
					this.generateCode();
					this.saveParam();
				}
			}
		} catch (Exception var5) {
			var5.printStackTrace();
			MessageUtil.appendMsg(this.result, "系统异常：" + var5.getLocalizedMessage());
		} finally {
			this.closeResultSet(this.rs);
			this.closeStatement(this.stat);
			this.closeConnection(this.con);
		}

		this.dirButton.setEnabled(true);
		this.start.setEnabled(true);
	}

	private void saveParam() {
		ResourceUtil util = new ResourceUtil();
		Properties config = util.getConfig();
		config.setProperty("GenerateCode.dbUrl", this.dbUrl);
		config.setProperty("GenerateCode.dbUsername", this.dbUserName);
		config.setProperty("GenerateCode.dbPassword", this.dbPassword);
		config.setProperty("GenerateCode.tableName", this.tableName);
		config.setProperty("GenerateCode.tableNamePrefix", this.tableNamePrefix);
		config.setProperty("GenerateCode.mycat", this.mycatColumn);
		config.setProperty("GenerateCode.dirPath", this.dirPath);
		config.setProperty("GenerateCode.entity", this.entityPackage);
		config.setProperty("GenerateCode.dao", this.daoPackage);
		config.setProperty("GenerateCode.service", this.servicePackage);
		config.setProperty("GenerateCode.namespace", this.namespace);
		config.setProperty("GenerateCode.user", this.staffNo);
		util.updateConfig(config);
	}

	private void init() {
		String str = this.tableName;
		if (StringUtils.isNotEmpty(this.tableNamePrefix)) {
			this.tableNamePrefix = this.tableNamePrefix.toLowerCase();
			str = str.substring(this.tableNamePrefix.length());
		}

		String[] split = str.split("_");
		str = "";
		String[] var6 = split;
		int var5 = split.length;

		for (int var4 = 0; var4 < var5; ++var4) {
			String tmp = var6[var4];
			str = str + this.change(tmp);
		}

		this.tableName2 = str;
		this.entityName = str + "Entity";
		this.daoName = str + "Mapper";
		this.serviceName = str + "Service";
		this.mapperName = this.daoName;
		this.namespace = this.daoPackage + "." + this.daoName;
	}

	private void generateCode() throws Exception {
		MessageUtil.appendMsg(this.result, "开始生成代码");
		this.queryTableInfo();
		File dir = new File(this.dirPath);
		if (dir.exists()) {
			dir.delete();
		}

		dir.mkdir();
		this.generateEntity(dir);
		this.generateDaoInterface(dir);
		// this.generateDaoImpl(dir);
		this.generateServiceInterface(dir);
		this.generateServiceImpl(dir);
		this.generateMapper(dir);
		MessageUtil.appendMsg(this.result, "代码生成完毕");

		try {
			Runtime.getRuntime().exec("cmd.exe /c start " + dir.getCanonicalPath());
		} catch (IOException var3) {
			var3.printStackTrace();
		}

	}

	private void generateMapper(File dir) throws Exception {
		MessageUtil.appendMsg(this.result, "生成SQL文件");
		// File file = new File(dir.getCanonicalPath() + File.separator +
		// "mapper");
		// file.mkdirs();
		// this.delete(file);
		BufferedWriter br = null;

		try {
			File tmp = new File(
					dir.getCanonicalPath() + File.separator + "mapper" + File.separator + this.mapperName + ".xml");
			if (tmp.exists()) {
				tmp.delete();
			}

			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
			this.generateMapper(br);
		} catch (Exception var8) {
			var8.printStackTrace();
			MessageUtil.appendMsg(this.result, "生成SQL文件异常：" + var8.getLocalizedMessage());
			throw var8;
		} finally {
			br.close();
		}

	}

	private void generateMapper(BufferedWriter br) throws Exception {
		String entityPath = this.entityPackage + "." + this.entityName;
		MessageUtil.appendLine(br, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		MessageUtil.appendLine(br,
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		MessageUtil.appendLine(br, "<mapper namespace=\"" + this.namespace + "\">");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    <sql id=\"" + this.tableName2.toLowerCase() + "_column\">");

		for (int i = 0; i < this.columns.size(); ++i) {
			Column column = this.columns.get(i);
			if (i != this.columns.size() - 1) {
				MessageUtil.appendLine(br, "        " + column.getName() + " " + column.getJavaName() + ",");
			} else {
				MessageUtil.appendLine(br, "        " + column.getName() + " " + column.getJavaName());
			}
		}

		MessageUtil.appendLine(br, "    </sql>");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    <!-- 保存记录 -->");
		MessageUtil.appendLine(br,
				"    <insert id=\"add" + this.tableName2 + "\" parameterType=\"" + entityPath + "\">");
		MessageUtil.appendLine(br, "        insert into " + this.tableName);
		MessageUtil.appendLine(br, "        (");
		StringBuffer sb = new StringBuffer();

		Column column;
		for (int i = 0; i < this.columns.size(); ++i) {
			column = this.columns.get(i);
			if (!column.isAutoIncr()) {
				sb.append("            " + column.getName() + ",\r\n");
			}
		}

		String str = sb.toString();
		str = str.substring(0, str.lastIndexOf(","));
		MessageUtil.appendLine(br, str);
		MessageUtil.appendLine(br, "        )");
		MessageUtil.appendLine(br, "        values");
		MessageUtil.appendLine(br, "        (");
		sb = new StringBuffer();

		for (int i = 0; i < this.columns.size(); ++i) {
			column = this.columns.get(i);
			if (!column.isAutoIncr()) {
				sb.append("            #{" + column.getJavaName() + "},\r\n");
			}
		}

		str = sb.toString();
		str = str.substring(0, str.lastIndexOf(","));
		MessageUtil.appendLine(br, str);
		MessageUtil.appendLine(br, "        )");
		MessageUtil.appendLine(br, "    </insert>");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    <!-- 更新记录 -->");
		MessageUtil.appendLine(br,
				"    <update id=\"update" + this.tableName2 + "\" parameterType=\"" + entityPath + "\">");
		MessageUtil.appendLine(br, "        update " + this.tableName);
		MessageUtil.appendLine(br, "        <set>");
		Iterator var11 = this.columns.iterator();

		while (var11.hasNext()) {
			column = (Column) var11.next();
			if (!column.isAutoIncr() && !column.isKey()) {
				MessageUtil.appendLine(br, "            <if test=\"" + column.getJavaName() + " != null and "
						+ column.getJavaName() + " != '' \">");
				MessageUtil.appendLine(br,
						"                " + column.getName() + " = #{" + column.getJavaName() + "},");
				MessageUtil.appendLine(br, "            </if>");
			}
		}

		MessageUtil.appendLine(br, "        </set>");
		MessageUtil.appendLine(br, "        where");
		// if (StringUtils.isNotEmpty(this.mycatColumn)) {
		// var11 = this.columns.iterator();
		//
		// while (var11.hasNext()) {
		// column = (Column) var11.next();
		// if (this.mycatColumn.equalsIgnoreCase(column.getName())) {
		// MessageUtil.appendLine(br,
		// " " + this.mycatColumn + " = #{" + column.getJavaName() + "}");
		// break;
		// }
		// }
		// } else {
		// MessageUtil.appendLine(br, " 1 = 1");
		// }

		var11 = this.columns.iterator();

		while (var11.hasNext()) {
			column = (Column) var11.next();
			// && !column.getName().equalsIgnoreCase(this.mycatColumn)
			if (column.isIndex()) {
				// MessageUtil.appendLine(br, " <if test=\"" +
				// column.getJavaName() + " != null and "
				// + column.getJavaName() + " != '' \">");
				MessageUtil.appendLine(br, "              " + column.getName() + " = #{" + column.getJavaName() + "}");
				// MessageUtil.appendLine(br, " </if>");
			}
		}

		MessageUtil.appendLine(br, "    </update>");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    <!-- 查询符合条件的记录 -->");
		MessageUtil.appendLine(br, "    <select id=\"query" + this.tableName2 + "\" parameterType=\"" + entityPath
				+ "\" resultType=\"" + entityPath + "\">");
		MessageUtil.appendLine(br, "        select <include refid=\"" + this.tableName2.toLowerCase() + "_column\" />");
		MessageUtil.appendLine(br, "        from " + this.tableName);
		// if (StringUtils.isNotEmpty(this.mycatColumn)) {
		// var11 = this.columns.iterator();
		//
		// while (var11.hasNext()) {
		// column = (Column) var11.next();
		// if (this.mycatColumn.equalsIgnoreCase(column.getName())) {
		// MessageUtil.appendLine(br,
		// " where " + this.mycatColumn + " = #{" + column.getJavaName() + "}");
		// break;
		// }
		// }
		//
		// } else {
		// MessageUtil.appendLine(br, " where 1 = 1");
		// }
		MessageUtil.appendLine(br, "        where 1 = 1");

		var11 = this.columns.iterator();

		while (var11.hasNext()) {
			column = (Column) var11.next();
			// && !column.getName().equalsIgnoreCase(this.mycatColumn)
			if (column.isIndex()) {
				MessageUtil.appendLine(br, "        <if test=\"" + column.getJavaName() + " != null and "
						+ column.getJavaName() + " != '' \">");
				MessageUtil.appendLine(br,
						"            and " + column.getName() + " = #{" + column.getJavaName() + "}");
				MessageUtil.appendLine(br, "        </if>");
			}
		}

		MessageUtil.appendLine(br, "    </select>");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "</mapper>");
	}

	private void generateServiceImpl(File dir) throws Exception {
		MessageUtil.appendMsg(this.result, "生成SERVICE实现类");
		File file = new File(dir.getCanonicalPath() + File.separator + "service" + File.separator + "impl");
		file.mkdirs();
		BufferedWriter br = null;

		try {
			File tmp = new File(file.getCanonicalPath() + File.separator + this.serviceName + "Impl.java");
			if (tmp.exists()) {
				tmp.delete();
			}

			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
			this.generateServiceImpl(br);
		} catch (Exception var8) {
			var8.printStackTrace();
			MessageUtil.appendMsg(this.result, "生成SERVICE实现类异常：" + var8.getLocalizedMessage());
			throw var8;
		} finally {
			br.close();
		}

	}

	private void generateServiceImpl(BufferedWriter br) throws Exception {
		String fileName = this.serviceName + "Impl";
		this.generateFileComment(br, fileName, "对应的SERVICE实现类");
		MessageUtil.appendLine(br, "package " + this.servicePackage + ".impl;");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "import java.util.List;");
		MessageUtil.appendLine(br, "import org.springframework.beans.factory.annotation.Autowired;");
		MessageUtil.appendLine(br, "import org.springframework.stereotype.Service;");
		MessageUtil.appendLine(br, "import " + this.daoPackage + this.daoName + ";");
		MessageUtil.appendLine(br, "import " + this.entityPackage + "." + this.entityName + ";");
		MessageUtil.appendLine(br, "import " + this.servicePackage + ".I" + this.serviceName + ";");
		MessageUtil.appendLine(br, "");
		this.generateClassComment(br, "对应的SERVICE实现类");
		MessageUtil.appendLine(br, "@Service");
		MessageUtil.appendLine(br, "public class " + fileName + " implements I" + this.serviceName + " {");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    @Autowired");
		MessageUtil.appendLine(br, "    " + this.daoName + " mapper;");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    @Override");
		MessageUtil.appendLine(br, "    public int add" + this.tableName2 + "(" + this.entityName + " entity) {");
		MessageUtil.appendLine(br, "        return mapper.add" + this.tableName2 + "(entity);");
		MessageUtil.appendLine(br, "    }");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    @Override");
		MessageUtil.appendLine(br, "    public int update" + this.tableName2 + "(" + this.entityName + " entity) {");
		MessageUtil.appendLine(br, "        return mapper.update" + this.tableName2 + "(entity);");
		MessageUtil.appendLine(br, "    }");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    @Override");
		MessageUtil.appendLine(br, "    public List<" + this.entityName + "> query" + this.tableName2 + "("
				+ this.entityName + " entity) {");
		MessageUtil.appendLine(br, "        return mapper.query" + this.tableName2 + "(entity);");
		MessageUtil.appendLine(br, "    }");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "}");
	}

	private void generateServiceInterface(File dir) throws Exception {
		MessageUtil.appendMsg(this.result, "生成SERVICE接口");
		File file = new File(dir.getCanonicalPath() + File.separator + "service");
		file.mkdirs();
		this.delete(file);
		BufferedWriter br = null;

		try {
			File tmp = new File(file.getCanonicalPath() + File.separator + "I" + this.serviceName + ".java");
			if (tmp.exists()) {
				tmp.delete();
			}

			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
			this.generateServiceInterface(br);
		} catch (Exception var8) {
			var8.printStackTrace();
			MessageUtil.appendMsg(this.result, "生成SERVICE接口异常：" + var8.getLocalizedMessage());
			throw var8;
		} finally {
			br.close();
		}

	}

	private void generateServiceInterface(BufferedWriter br) throws Exception {
		String fileName = "I" + this.serviceName;
		this.generateFileComment(br, fileName, "对应的SERVICE接口");
		MessageUtil.appendLine(br, "package " + this.servicePackage + ";");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "import java.util.List;");
		MessageUtil.appendLine(br, "import " + this.entityPackage + "." + this.entityName + ";");
		MessageUtil.appendLine(br, "");
		this.generateClassComment(br, "对应的SERVICE接口");
		MessageUtil.appendLine(br, "public interface " + fileName + " {");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈保存" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br, "    int add" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈更新" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br, "    int update" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈查询" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br,
				"    List<" + this.entityName + "> query" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "}");
	}

	// private void generateDaoImpl(File dir) throws Exception {
	// MessageUtil.appendMsg(this.result, "生成DAO实现类");
	// File file = new File(dir.getCanonicalPath() + File.separator + "dao" +
	// File.separator + "impl");
	// file.mkdirs();
	// BufferedWriter br = null;
	//
	// try {
	// File tmp = new File(file.getCanonicalPath() + File.separator +
	// this.daoName + "Impl.java");
	// if (tmp.exists()) {
	// tmp.delete();
	// }
	//
	// br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp),
	// "UTF-8"));
	// this.generateDaoImpl(br);
	// } catch (Exception var8) {
	// var8.printStackTrace();
	// MessageUtil.appendMsg(this.result, "生成DAO实现类异常：" +
	// var8.getLocalizedMessage());
	// throw var8;
	// } finally {
	// br.close();
	// }
	//
	// }

	// private void generateDaoImpl(BufferedWriter br) throws Exception {
	// String fileName = this.daoName + "Impl";
	// this.generateFileComment(br, fileName, "对应的DAO实现类");
	// MessageUtil.appendLine(br, "package " + this.daoPackage + ".impl;");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br, "import java.util.List;");
	// MessageUtil.appendLine(br, "import com.suning.framework.spring.Dao;");
	// MessageUtil.appendLine(br, "import " + this.daoPackage + ".I" +
	// this.daoName + ";");
	// MessageUtil.appendLine(br, "import " + this.entityPackage + "." +
	// this.entityName + ";");
	// StringBuffer sb = new StringBuffer(1024);
	// Iterator var5 = this.columns.iterator();
	//
	// while (true) {
	// while (true) {
	// Column column;
	// do {
	// if (!var5.hasNext()) {
	// if (sb.length() > 0) {
	// MessageUtil.appendLine(br, "import
	// org.apache.commons.lang.StringUtils;");
	// }
	//
	// MessageUtil.appendLine(br, "");
	// this.generateClassComment(br, "对应的DAO实现类");
	// MessageUtil.appendLine(br, "@Dao");
	// MessageUtil.appendLine(br, "public class " + fileName
	// + " extends AbstractDaoSupport implements I" + this.daoName + " {");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br,
	// " private static final String NAMESPACE = \"" + this.namespace + ".\";");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br, " @Override");
	// MessageUtil.appendLine(br,
	// " public int add" + this.tableName2 + "(" + this.entityName + " entity)
	// {");
	// MessageUtil.appendLine(br,
	// " return getGenericResult(getSqlSession().insert(NAMESPACE + \"add"
	// + this.tableName2 + "\", entity) > 0 ? Boolean.TRUE : Boolean.FALSE);");
	// MessageUtil.appendLine(br, " }");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br, " @Override");
	// MessageUtil.appendLine(br,
	// " public int update" + this.tableName2 + "(" + this.entityName + "
	// entity) {");
	// if (sb.length() > 0) {
	// MessageUtil.appendLine(br, " if(" + sb.toString() + ") {");
	// MessageUtil.appendLine(br, " // 更新条件没有，返回失败");
	// MessageUtil.appendLine(br, " return getGenericResult(Boolean.FALSE);");
	// MessageUtil.appendLine(br, " }");
	// }
	//
	// MessageUtil.appendLine(br,
	// " return getGenericResult(getSqlSession().update(NAMESPACE + \"update"
	// + this.tableName2 + "\", entity) > 0 ? Boolean.TRUE : Boolean.FALSE);");
	// MessageUtil.appendLine(br, " }");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br, " @Override");
	// MessageUtil.appendLine(br, " @SuppressWarnings(\"unchecked\")");
	// MessageUtil.appendLine(br, " public List<" + this.entityName + "> query"
	// + this.tableName2
	// + "(" + this.entityName + " entity) {");
	// MessageUtil.appendLine(br, " List<" + this.entityName + "> list =
	// null;");
	// MessageUtil.appendLine(br, " list = getSqlSession().selectList(NAMESPACE
	// + \"query"
	// + this.tableName2 + "\", entity);");
	// MessageUtil.appendLine(br, " }");
	// MessageUtil.appendLine(br, " return list;");
	// MessageUtil.appendLine(br, " }");
	// MessageUtil.appendLine(br, "");
	// MessageUtil.appendLine(br, "}");
	// return;
	// }
	//
	// column = (Column) var5.next();
	// } while (!column.isIndex());
	//
	// if ("String".equals(column.getJavaType())) {
	// if (sb.length() > 0) {
	// sb.append(" && ");
	// }
	//
	// sb.append("StringUtils.isEmpty(entity.get" +
	// this.change(column.getJavaName()) + "())");
	// } else if ("Date".equals(column.getJavaType()) ||
	// "Timestamp".equals(column.getJavaType())
	// || "BigDecimal".equals(column.getJavaType())) {
	// sb.append("null == entity.get" + this.change(column.getJavaName()) +
	// "()");
	// }
	// }
	// }
	// }

	private void generateDaoInterface(File dir) throws Exception {
		MessageUtil.appendMsg(this.result, "生成DAO接口");
		File file = new File(dir.getCanonicalPath() + File.separator + "mapper");
		file.mkdirs();
		this.delete(file);
		BufferedWriter br = null;

		try {
			File tmp = new File(file.getCanonicalPath() + File.separator + this.daoName + ".java");
			if (tmp.exists()) {
				tmp.delete();
			}

			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
			this.generateDaoInterface(br);
		} catch (Exception var8) {
			var8.printStackTrace();
			MessageUtil.appendMsg(this.result, "生成DAO接口异常：" + var8.getLocalizedMessage());
			throw var8;
		} finally {
			br.close();
		}

	}

	private void generateDaoInterface(BufferedWriter br) throws Exception {
		String fileName = this.daoName;
		this.generateFileComment(br, fileName, "对应的Mapper接口");
		MessageUtil.appendLine(br, "package " + this.daoPackage + ";");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "import java.util.List;");
		MessageUtil.appendLine(br, "import org.apache.ibatis.annotations.Mapper;");
		MessageUtil.appendLine(br, "import " + this.entityPackage + "." + this.entityName + ";");
		MessageUtil.appendLine(br, "");
		this.generateClassComment(br, "对应的Mapper接口");
		MessageUtil.appendLine(br, "@Mapper");
		MessageUtil.appendLine(br, "public interface " + fileName + " {");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈保存" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br, "    int add" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈更新" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br, "    int update" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "    /**");
		MessageUtil.appendLine(br, "     * ");
		MessageUtil.appendLine(br, "     * 功能描述: <br>");
		MessageUtil.appendLine(br, "     * 〈查询" + this.tableComment + "记录〉");
		MessageUtil.appendLine(br, "     *");
		MessageUtil.appendLine(br, "     * @param entity");
		MessageUtil.appendLine(br, "     * @return");
		MessageUtil.appendLine(br, "     */");
		MessageUtil.appendLine(br,
				"    List<" + this.entityName + "> query" + this.tableName2 + "(" + this.entityName + " entity);");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "}");
	}

	private void generateEntity(File dir) throws Exception {
		MessageUtil.appendMsg(this.result, "生成实体类");
		File file = new File(dir.getCanonicalPath() + File.separator + "entity");
		file.mkdirs();
		this.delete(file);
		BufferedWriter br = null;

		try {
			File entity = new File(file.getCanonicalPath() + File.separator + this.entityName + ".java");
			if (entity.exists()) {
				entity.delete();
			}

			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(entity), "UTF-8"));
			this.generateEntity(br);
		} catch (Exception var8) {
			var8.printStackTrace();
			MessageUtil.appendMsg(this.result, "生成实体类异常：" + var8.getLocalizedMessage());
			throw var8;
		} finally {
			br.close();
		}

	}

	private void delete(File file) {
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			File[] var6 = listFiles;
			int var5 = listFiles.length;

			for (int var4 = 0; var4 < var5; ++var4) {
				File f = var6[var4];
				this.delete(f);
			}
		} else {
			file.delete();
		}

	}

	private void generateFileComment(BufferedWriter br, String fileName, String fileDesc) throws Exception {
		MessageUtil.appendLine(br, "/*");
		MessageUtil.appendLine(br, " * Copyright (C), 2017-2020, 杭州城市大数据运营有限公司");
		MessageUtil.appendLine(br, " * FileName: " + fileName + ".java");
		MessageUtil.appendLine(br, " * Author:   " + this.staffNo);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		MessageUtil.appendLine(br, " * Date:     " + sf.format(new Date()));
		MessageUtil.appendLine(br, " * Description: " + this.tableComment + fileDesc);
		MessageUtil.appendLine(br, " */");
	}

	private void generateClassComment(BufferedWriter br, String classDesc) throws Exception {
		MessageUtil.appendLine(br, "/**");
		MessageUtil.appendLine(br, " * <" + this.tableComment + classDesc + "><br> ");
		MessageUtil.appendLine(br, " *");
		MessageUtil.appendLine(br, " * @author " + this.staffNo);
		MessageUtil.appendLine(br, " */");
	}

	private void generateEntity(BufferedWriter br) throws Exception {
		this.generateFileComment(br, this.entityName, "对应的实体类 ");
		MessageUtil.appendLine(br, "package " + this.entityPackage + ";");
		MessageUtil.appendLine(br, "");
		Set<String> type = new HashSet();
		Iterator var4 = this.columns.iterator();

		Column column;
		while (var4.hasNext()) {
			column = (Column) var4.next();
			if ("Date".equals(column.getJavaType()) && !type.contains("Date")) {
				type.add("Date");
				MessageUtil.appendLine(br, "import java.util.Date;");
			}

			if ("Timestamp".equals(column.getJavaType()) && !type.contains("Timestamp")) {
				type.add("Timestamp");
				MessageUtil.appendLine(br, "import java.sql.Timestamp;");
			}

			if ("BigDecimal".equals(column.getJavaType()) && !type.contains("BigDecimal")) {
				type.add("BigDecimal");
				MessageUtil.appendLine(br, "import java.math.BigDecimal;");
			}

			if ("Integer".equals(column.getJavaType()) && !type.contains("Integer")) {
				type.add("Integer");
				MessageUtil.appendLine(br, "import java.lang.Integer;");
			}

			if ("Long".equals(column.getJavaType()) && !type.contains("Long")) {
				type.add("Long");
				MessageUtil.appendLine(br, "import java.lang.Long;");
			}
		}
		MessageUtil.appendLine(br, "import lombok.Data;");
		MessageUtil.appendLine(br, "");
		this.generateClassComment(br, "对应的实体类");
		MessageUtil.appendLine(br, "@Data");
		MessageUtil.appendLine(br, "public class " + this.entityName + " {");
		var4 = this.columns.iterator();

		while (var4.hasNext()) {
			column = (Column) var4.next();
			MessageUtil.appendLine(br, "");
			MessageUtil.appendLine(br, "    /**");
			MessageUtil.appendLine(br, "     * " + column.getComment());
			MessageUtil.appendLine(br, "     */");
			MessageUtil.appendLine(br, "    private " + column.getJavaType() + " " + column.getJavaName() + ";");
		}

		var4 = this.columns.iterator();

		// while (var4.hasNext()) {
		// column = (Column) var4.next();
		// String tmp = this.change(column.getJavaName());
		// MessageUtil.appendLine(br, "");
		// MessageUtil.appendLine(br, " /**");
		// MessageUtil.appendLine(br, " * @return the " + column.getJavaName());
		// MessageUtil.appendLine(br, " */");
		// MessageUtil.appendLine(br, " public " + column.getJavaType() + " get"
		// + tmp + "() {");
		// MessageUtil.appendLine(br, " return " + column.getJavaName() + ";");
		// MessageUtil.appendLine(br, " }");
		// MessageUtil.appendLine(br, "");
		// MessageUtil.appendLine(br, " /**");
		// MessageUtil.appendLine(br,
		// " * @param " + column.getJavaName() + " the " + column.getJavaName()
		// + " to set");
		// MessageUtil.appendLine(br, " */");
		// MessageUtil.appendLine(br,
		// " public void set" + tmp + "(" + column.getJavaType() + " " +
		// column.getJavaName() + ") {");
		// MessageUtil.appendLine(br, " this." + column.getJavaName() + " = " +
		// column.getJavaName() + ";");
		// MessageUtil.appendLine(br, " }");
		// }

		// MessageUtil.appendLine(br, "");
		// MessageUtil.appendLine(br, " /* (non-Javadoc)");
		// MessageUtil.appendLine(br, " * @see java.lang.Object#toString()");
		// MessageUtil.appendLine(br, " */");
		// MessageUtil.appendLine(br, " @Override");
		// MessageUtil.appendLine(br, " public String toString() {");
		// StringBuffer sb = new StringBuffer(1024);

		// for (int i = 0; i < this.columns.size(); ++i) {
		// if (i % 3 == 0 && i != 0) {
		// sb.append("\" + \r\n \"");
		// }
		//
		// if (i != 0) {
		// sb.append(", ");
		// }
		//
		// sb.append("\\\"" + this.columns.get(i).getJavaName() + "\\\":\\\"\" +
		// " + this.columns.get(i).getJavaName()
		// + " + \"\\\"");
		// }
		//
		// sb.append("}\";");
		// MessageUtil.appendLine(br, " return \"{" + sb.toString());
		// MessageUtil.appendLine(br, " }");
		MessageUtil.appendLine(br, "");
		MessageUtil.appendLine(br, "}");
	}

	private void queryTableInfo() throws Exception {
		MessageUtil.appendMsg(this.result, "查询表信息");
		this.schema = con.getCatalog();
		String sql = "select TABLE_COMMENT from information_schema.tables where table_name = '" + this.tableName
				+ "' and TABLE_SCHEMA = '" + this.schema + "'";
		System.out.println(sql);
		this.rs = this.stat.executeQuery(sql);
		if (!this.rs.next()) {
			throw new Exception("表[" + this.tableName + "]不存在");
		} else {
			String columnName = this.rs.getString("TABLE_COMMENT");
			if (StringUtils.isNotEmpty(columnName)) {
				this.tableComment = columnName;
			} else {
				this.tableComment = this.tableName;
			}

			this.closeResultSet(this.rs);
			MessageUtil.appendMsg(this.result, "查询表字段信息");
			sql = "select COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, EXTRA, COLUMN_KEY from information_schema.columns where table_name = '"
					+ this.tableName + "' and TABLE_SCHEMA = '" + schema + "'";

			Column column;
			for (this.rs = this.stat.executeQuery(sql); this.rs.next(); this.columns.add(column)) {
				column = new Column();
				column.setName(this.rs.getString("COLUMN_NAME"));
				column.setType(this.rs.getString("DATA_TYPE"));
				column.setComment(this.rs.getString("COLUMN_COMMENT"));
				if (StringUtils.isEmpty(column.getComment())) {
					column.setComment(column.getName());
				}

				if ("auto_increment".equalsIgnoreCase(this.rs.getString("EXTRA"))) {
					column.setAutoIncr(true);
				}

				if ("PRI".equalsIgnoreCase(this.rs.getString("COLUMN_KEY"))) {
					column.setKey(true);
				}
			}

			if (CollectionUtils.isEmpty(this.columns)) {
				throw new Exception("表[" + this.tableName + "]没有定义字段");
			} else {
				this.closeResultSet(this.rs);
				MessageUtil.appendMsg(this.result, "查询表索引信息");
				sql = "show index from " + this.tableName;
				this.rs = this.stat.executeQuery(sql);

				while (true) {
					while (this.rs.next()) {
						columnName = this.rs.getString("COLUMN_NAME");
						Iterator var4 = this.columns.iterator();

						while (var4.hasNext()) {
							column = (Column) var4.next();
							if (column.getName().equals(columnName)) {
								column.setIndex(true);
								break;
							}
						}
					}

					this.closeResultSet(this.rs);
					return;
				}
			}
		}
	}

	private Connection getConnection() throws Exception {
		MessageUtil.appendMsg(this.result, "连接数据库");
		Connection c = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(this.dbUrl, this.dbUserName, this.dbPassword);
			return c;
		} catch (Exception var3) {
			MessageUtil.appendMsg(this.result, "连接数据库出现异常：" + var3.getLocalizedMessage());
			throw var3;
		}
	}

	private void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException var3) {
				var3.printStackTrace();
			}
		}

	}

	private void closeStatement(Statement stat) {
		if (stat != null) {
			try {
				stat.close();
				stat = null;
			} catch (SQLException var3) {
				var3.printStackTrace();
			}
		}

	}

	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException var3) {
				var3.printStackTrace();
			}
		}

	}

	private String change(String str) {
		return StringUtils.isEmpty(str) ? "" : str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public String getDbUrl() {
		return this.dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUserName() {
		return this.dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return this.dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableNamePrefix() {
		return this.tableNamePrefix;
	}

	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}

	public String getMycatColumn() {
		return this.mycatColumn;
	}

	public void setMycatColumn(String mycatColumn) {
		this.mycatColumn = mycatColumn;
	}

	public JTextArea getResult() {
		return this.result;
	}

	public void setResult(JTextArea result) {
		this.result = result;
	}

	public String getDirPath() {
		return this.dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}

	public String getStaffNo() {
		return this.staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}

	public String getEntityPackage() {
		return this.entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public String getDaoPackage() {
		return this.daoPackage;
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public String getServicePackage() {
		return this.servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public JButton getDirButton() {
		return this.dirButton;
	}

	public void setDirButton(JButton dirButton) {
		this.dirButton = dirButton;
	}

	public JButton getStart() {
		return this.start;
	}

	public void setStart(JButton start) {
		this.start = start;
	}

	public static void main(String[] args) {
		GenerateCodeThread t = new GenerateCodeThread();
		t.setDbUrl("jdbc:mysql://119.3.2.85:3306/eastjg");
		t.setDbUserName("root");
		t.setDbPassword("mCEVgbZv2V0z1");
		t.setTableName("label_table");
		t.setTableNamePrefix("");
		t.setMycatColumn("");
		JTextArea result = new JTextArea();
		t.setResult(result);
		t.setDirPath("D:\\output");
		t.setEntityPackage("cn.com.citydo.web.domain");
		t.setDaoPackage("cn.com.citydo.web.mapper");
		t.setServicePackage("cn.com.citydo.web.service");
		t.setNamespace("cn.com.citydo.web.mapper");
		t.setStaffNo("DongXiaoqiang0115");
		t.setDirButton(new JButton());
		t.setStart(new JButton());
		t.run();
		System.out.println(result.getText());
	}
}
