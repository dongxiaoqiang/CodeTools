package com.citydo.tools.component.implement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.GenerateCodeThread;
import com.citydo.tools.util.ResourceUtil;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class CodeGenerateComponent extends JPanel implements MyComponent {
	Dimension size = null;
	private static final long serialVersionUID = 183980533477295265L;
	private JLabel dbLabel;
	private JLabel dbUrlLabel;
	private JLabel dbUsernameLabel;
	private JLabel dbPasswordLabel;
	private JTextField dbUrl;
	private JTextField dbUsername;
	private JTextField dbPassword;
	private JLabel tableLabel;
	private JLabel tableNameLabel;
	private JLabel tableNamePrefixLabel;
	private JLabel mycatLabel;
	private JTextField tableName;
	private JTextField tableNamePrefix;
	private JTextField mycat;
	private JLabel resultLabel;
	private JTextArea result;
	private JScrollPane scrollPane;
	private JFileChooser fileChooser;
	private JLabel dirLabel;
	private JTextField dirPath;
	private JButton dirButton;
	private JButton start;
	private JLabel packageLabel;
	private JLabel entityLabel;
	private JLabel daoLabel;
	private JLabel serviceLabel;
	private JTextField entity;
	private JTextField dao;
	private JTextField service;
	private JLabel otherLabel;
	private JLabel namespaceLabel;
	private JLabel userLabel;
	private JTextField namespace;
	private JTextField user;

	public CodeGenerateComponent() {
		this.initComponents();
		this.resized();
	}

	private void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.dirButton) {
			int state = this.fileChooser.showOpenDialog(this);
			if (state == 1) {
				return;
			}

			File f = this.fileChooser.getSelectedFile();
			this.dirPath.setText(f.getAbsolutePath());
		} else if (e.getSource() == this.start) {
			if (this.dbUrl.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入数据库URL！", "提示", 2);
				return;
			}

			if (this.dbUsername.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入数据库用户名！", "提示", 2);
				return;
			}

			if (this.dbPassword.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入数据库密码！", "提示", 2);
				return;
			}

			if (this.tableName.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入表名！", "提示", 2);
				return;
			}

			if (this.entity.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入ENTITY所在包路径！", "提示", 2);
				return;
			}

			if (this.dao.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入DAO所在包路径！", "提示", 2);
				return;
			}

			if (this.service.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入SERVICE所在包路径！", "提示", 2);
				return;
			}

			if (this.namespace.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入SQL命名空间！", "提示", 2);
				return;
			}

			if (this.user.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先输入员工工号！", "提示", 2);
				return;
			}

			if (this.dirPath.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请先选择文件存储目录！", "提示", 2);
				return;
			}

			this.start.setEnabled(false);
			this.dirButton.setEnabled(false);
			this.result.setText("");
			this.result.setCaretPosition(this.result.getText().length());
			GenerateCodeThread t = new GenerateCodeThread();
			t.setDbUrl(this.dbUrl.getText());
			t.setDbUserName(this.dbUsername.getText());
			t.setDbPassword(this.dbPassword.getText());
			t.setTableName(this.tableName.getText());
			System.out.println(this.tableName.getText());
			t.setTableNamePrefix(this.tableNamePrefix.getText());
			t.setMycatColumn(this.mycat.getText());
			t.setResult(this.result);
			t.setDirPath(this.dirPath.getText());
			t.setEntityPackage(this.entity.getText());
			t.setDaoPackage(this.dao.getText());
			t.setServicePackage(this.service.getText());
			t.setNamespace(this.namespace.getText());
			t.setStaffNo(this.user.getText());
			t.setDirButton(this.dirButton);
			t.setStart(this.start);
			Thread lt = new Thread(t);
			lt.start();
		}

	}

	private void initComponents() {
		ResourceUtil util = new ResourceUtil();
		Properties config = util.getConfig();
		this.fileChooser = new JFileChooser(config.getProperty("GenerateCode.dirPath", "D:\\"));
		this.fileChooser.setFileSelectionMode(1);
		this.dbLabel = new JLabel();
		this.dbUrlLabel = new JLabel();
		this.dbUsernameLabel = new JLabel();
		this.dbPasswordLabel = new JLabel();
		this.dbUrl = new JTextField();
		this.dbUsername = new JTextField();
		this.dbPassword = new JTextField();
		this.tableLabel = new JLabel();
		this.tableNameLabel = new JLabel();
		this.tableNamePrefixLabel = new JLabel();
		this.mycatLabel = new JLabel();
		this.tableName = new JTextField();
		this.tableNamePrefix = new JTextField();
		this.mycat = new JTextField();
		this.resultLabel = new JLabel();
		this.result = new JTextArea();
		this.scrollPane = new JScrollPane();
		this.dirLabel = new JLabel();
		this.dirPath = new JTextField();
		this.dirButton = new JButton();
		this.start = new JButton();
		this.packageLabel = new JLabel();
		this.entityLabel = new JLabel();
		this.daoLabel = new JLabel();
		this.serviceLabel = new JLabel();
		this.entity = new JTextField();
		this.dao = new JTextField();
		this.service = new JTextField();
		this.otherLabel = new JLabel();
		this.namespaceLabel = new JLabel();
		this.userLabel = new JLabel();
		this.namespace = new JTextField();
		this.user = new JTextField();
		this.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", 2,
				5, new Font("Dialog", 1, 12), Color.red), this.getBorder()));
		this.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if ("border".equals(e.getPropertyName())) {
					throw new RuntimeException();
				}
			}
		});
		this.setLayout(new FormLayout(
				"[50dlu,pref],pref,[150dlu,pref],[10dlu,pref],[50dlu,pref],pref,[150dlu,pref],[10dlu,pref],[50dlu,pref],pref,[150dlu,pref]",
				"14*(fill:pref, $lgap), fill:pref:grow"));
		this.dbLabel.setText("数据库信息");
		this.dbLabel.setFont(new Font("微软雅黑", 0, 14));
		this.dbLabel.setForeground(Color.red);
		this.add(this.dbLabel, CC.xywh(1, 1, 11, 1, CC.LEFT, CC.FILL));
		this.dbUrlLabel.setText("url");
		this.dbUrlLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.dbUrlLabel, CC.xy(1, 3, CC.FILL, CC.FILL));
		this.dbUrl.setFont(new Font("微软雅黑", 0, 14));
		this.dbUrl.setEditable(true);
		this.dbUrl.setText(config.getProperty("GenerateCode.dbUrl", "jdbc:mysql://10.37.7.170:3306/crmsssit1"));
		this.add(this.dbUrl, CC.xy(3, 3, CC.FILL, CC.FILL));
		this.dbUsernameLabel.setText("用户名");
		this.dbUsernameLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.dbUsernameLabel, CC.xy(5, 3, CC.FILL, CC.FILL));
		this.dbUsername.setFont(new Font("微软雅黑", 0, 14));
		this.dbUsername.setEditable(true);
		this.dbUsername.setText(config.getProperty("GenerateCode.dbUsername", "selffabu"));
		this.add(this.dbUsername, CC.xy(7, 3, CC.FILL, CC.FILL));
		this.dbPasswordLabel.setText("密码");
		this.dbPasswordLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.dbPasswordLabel, CC.xy(9, 3, CC.FILL, CC.FILL));
		this.dbPassword.setFont(new Font("微软雅黑", 0, 14));
		this.dbPassword.setEditable(true);
		this.dbPassword.setText(config.getProperty("GenerateCode.dbPassword", "hnevnZlU1o"));
		this.add(this.dbPassword, CC.xy(11, 3, CC.FILL, CC.FILL));
		this.tableLabel.setText("表相关信息");
		this.tableLabel.setFont(new Font("微软雅黑", 0, 14));
		this.tableLabel.setForeground(Color.red);
		this.add(this.tableLabel, CC.xywh(1, 5, 11, 1, CC.LEFT, CC.FILL));
		this.tableNameLabel.setText("表名");
		this.tableNameLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.tableNameLabel, CC.xy(1, 7, CC.FILL, CC.FILL));
		this.tableName.setFont(new Font("微软雅黑", 0, 14));
		this.tableName.setEditable(true);
		this.tableName.setText(config.getProperty("GenerateCode.tableName", "lam_loan_main"));
		this.add(this.tableName, CC.xy(3, 7, CC.FILL, CC.FILL));
		this.tableNamePrefixLabel.setText("表名前缀");
		this.tableNamePrefixLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.tableNamePrefixLabel, CC.xy(5, 7, CC.FILL, CC.FILL));
		this.tableNamePrefix.setFont(new Font("微软雅黑", 0, 14));
		this.tableNamePrefix.setEditable(true);
		this.tableNamePrefix.setText(config.getProperty("GenerateCode.tableNamePrefix", "lam_"));
		this.add(this.tableNamePrefix, CC.xy(7, 7, CC.FILL, CC.FILL));
		this.mycatLabel.setText("分表字段");
		this.mycatLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.mycatLabel, CC.xy(9, 7, CC.FILL, CC.FILL));
		this.mycat.setFont(new Font("微软雅黑", 0, 14));
		this.mycat.setEditable(true);
		this.mycat.setText(config.getProperty("GenerateCode.mycat", "customer_no"));
		this.add(this.mycat, CC.xy(11, 7, CC.FILL, CC.FILL));
		this.packageLabel.setText("包路径信息");
		this.packageLabel.setFont(new Font("微软雅黑", 0, 14));
		this.packageLabel.setForeground(Color.red);
		this.add(this.packageLabel, CC.xywh(1, 9, 11, 1, CC.LEFT, CC.FILL));
		this.entityLabel.setText("ENTITY包");
		this.entityLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.entityLabel, CC.xy(1, 11, CC.FILL, CC.FILL));
		this.entity.setFont(new Font("微软雅黑", 0, 14));
		this.entity.setEditable(true);
		this.entity.setText(config.getProperty("GenerateCode.entity", "com.suning.plslam.entity.business.loan"));
		this.add(this.entity, CC.xy(3, 11, CC.FILL, CC.FILL));
		this.daoLabel.setText("DAO包");
		this.daoLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.daoLabel, CC.xy(5, 11, CC.FILL, CC.FILL));
		this.dao.setFont(new Font("微软雅黑", 0, 14));
		this.dao.setEditable(true);
		this.dao.setText(config.getProperty("GenerateCode.dao", "com.suning.plslam.dao.business.loan"));
		this.add(this.dao, CC.xy(7, 11, CC.FILL, CC.FILL));
		this.serviceLabel.setText("SERVICE包");
		this.serviceLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.serviceLabel, CC.xy(9, 11, CC.FILL, CC.FILL));
		this.service.setFont(new Font("微软雅黑", 0, 14));
		this.service.setEditable(true);
		this.service.setText(config.getProperty("GenerateCode.service", "com.suning.plslam.service.business.loan"));
		this.add(this.service, CC.xy(11, 11, CC.FILL, CC.FILL));
		this.otherLabel.setText("其他信息");
		this.otherLabel.setFont(new Font("微软雅黑", 0, 14));
		this.otherLabel.setForeground(Color.red);
		this.add(this.otherLabel, CC.xywh(1, 13, 11, 1, CC.LEFT, CC.FILL));
		this.namespaceLabel.setText("命名空间");
		this.namespaceLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.namespaceLabel, CC.xy(1, 15, CC.FILL, CC.FILL));
		this.namespace.setFont(new Font("微软雅黑", 0, 14));
		this.namespace.setEditable(true);
		this.namespace.setText(config.getProperty("GenerateCode.namespace", "business.loan.loanmain"));
		this.add(this.namespace, CC.xy(3, 15, CC.FILL, CC.FILL));
		this.userLabel.setText("员工工号");
		this.userLabel.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.userLabel, CC.xy(5, 15, CC.FILL, CC.FILL));
		this.user.setFont(new Font("微软雅黑", 0, 14));
		this.user.setEditable(true);
		this.user.setText(config.getProperty("GenerateCode.user", "17050528"));
		this.add(this.user, CC.xy(7, 15, CC.FILL, CC.FILL));
		this.dirLabel.setText("文件存储目录");
		this.dirLabel.setFont(new Font("微软雅黑", 0, 14));
		this.dirLabel.setForeground(Color.red);
		this.add(this.dirLabel, CC.xywh(1, 17, 11, 1, CC.LEFT, CC.FILL));
		this.dirPath.setFont(new Font("微软雅黑", 0, 14));
		if (config.containsKey("GenerateCode.dirPath")) {
			this.dirPath.setText(config.getProperty("GenerateCode.dirPath"));
		}

		this.dirPath.setEditable(false);
		this.add(this.dirPath, CC.xywh(1, 19, 7, 1, CC.FILL, CC.FILL));
		this.dirButton.setText("浏览");
		this.dirButton.setFont(new Font("微软雅黑", 0, 14));
		this.dirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CodeGenerateComponent.this.actionPerformed(e);
			}
		});
		this.add(this.dirButton, CC.xy(9, 19, CC.LEFT, CC.FILL));
		this.start.setText("开始生成");
		this.start.setFont(new Font("微软雅黑", 0, 14));
		this.start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CodeGenerateComponent.this.actionPerformed(e);
			}
		});
		this.add(this.start, CC.xy(11, 19, CC.LEFT, CC.FILL));
		this.resultLabel.setText("执行过程输出");
		this.resultLabel.setFont(new Font("微软雅黑", 0, 14));
		this.resultLabel.setForeground(Color.red);
		this.add(this.resultLabel, CC.xywh(1, 21, 11, 1, CC.LEFT, CC.FILL));
		this.scrollPane.setViewportView(this.result);
		this.add(this.scrollPane, CC.xywh(1, 23, 11, 1, CC.FILL, CC.FILL));
	}

	@Override
	public Component create() {
		return this;
	}

	@Override
	public void resized() {
		if (this.size == null) {
			this.size = this.getPreferredSize();
		}

		int width = (int) this.size.getWidth();
		int height = (int) this.size.getHeight();
		this.scrollPane.setPreferredSize(new Dimension(width, height - 70 - 36));
		this.scrollPane.revalidate();
		this.scrollPane.repaint();
		Dimension d = new Dimension(280, 30);
		this.dbUrl.setPreferredSize(d);
		this.dbPassword.setPreferredSize(d);
		this.dbUsername.setPreferredSize(d);
		this.tableName.setPreferredSize(d);
		this.tableNamePrefix.setPreferredSize(d);
		this.mycat.setPreferredSize(d);
		this.entity.setPreferredSize(d);
		this.dao.setPreferredSize(d);
		this.service.setPreferredSize(d);
		this.namespace.setPreferredSize(d);
		this.user.setPreferredSize(d);
	}
}
