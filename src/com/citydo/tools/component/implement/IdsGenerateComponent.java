//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.component.implement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.citydo.tools.bean.Option;
import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.IdsGenerate;
import com.citydo.tools.util.ResourceUtil;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.mkk.swing.JCalendarChooser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class IdsGenerateComponent extends JPanel implements MyComponent {
	private static final long serialVersionUID = 7816969859746729030L;
	Dimension size = null;
	private List<Option> provinces;
	private Map<String, List<Option>> cities;
	private Map<String, List<Option>> areas;
	private JCalendarChooser cc;
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	private JLabel remark;
	private JLabel labelLocation;
	private JPanel panel1;
	private JComboBox boxP;
	private JComboBox boxC;
	private JComboBox boxA;
	private JLabel labelTime;
	private JPanel panel2;
	private JTextField date;
	private JButton dateButton;
	private JLabel labelSex;
	private JPanel panel3;
	private JRadioButton radioG;
	private JRadioButton radioM;
	private JLabel labelNum;
	private JPanel panel4;
	private JTextField num;
	private JButton submit;
	private JScrollPane scrollPane1;
	private JTextArea result;

	public IdsGenerateComponent() {
		if (this.provinces == null) {
			this.initData();
		}

		this.initComponents();
		this.init();
		this.resized();
	}

	private void init() {
		this.cc = new JCalendarChooser(new Frame(), "获取时间");
		this.initComboBoxP();
		if (this.provinces.size() > 0) {
			String code = this.provinces.get(0).getCode();
			this.initComboBoxC(code);
			List<Option> list = this.cities.get(code);
			if (list != null && list.size() > 0) {
				code = list.get(0).getCode();
				this.initComboBoxA(code);
			}
		}

		ButtonGroup group = new ButtonGroup();
		group.add(this.radioG);
		group.add(this.radioM);
		this.date.setText(this.sf.format(new Date()));
	}

	private void initData() {
		BufferedReader reader = null;
		StringBuffer sb = null;
		ResourceUtil util = new ResourceUtil();
		this.provinces = new ArrayList();

		String line;
		Object object;
		Iterator var6;
		JSONObject obj;
		JSONArray array;
		try {
			sb = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(util.getResourceAsStream("provinces.json"), "utf-8"));

			while (true) {
				line = reader.readLine();
				if (line == null) {
					reader.close();
					reader = null;
					array = JSONArray.fromObject(sb.toString());
					var6 = array.iterator();

					while (var6.hasNext()) {
						object = var6.next();
						obj = (JSONObject) object;
						this.provinces.add(new Option(obj.getString("code"), obj.getString("name")));
					}
					break;
				}

				sb.append(line);
			}
		} catch (Exception var12) {
			var12.printStackTrace();
		}

		this.cities = new HashMap();

		String parentCode;
		ArrayList tmp;
		try {
			sb = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(util.getResourceAsStream("cities.json"), "utf-8"));

			while (true) {
				line = reader.readLine();
				if (line == null) {
					reader.close();
					reader = null;
					array = JSONArray.fromObject(sb.toString());
					var6 = array.iterator();

					while (var6.hasNext()) {
						object = var6.next();
						obj = (JSONObject) object;
						parentCode = obj.getString("parent_code");
						if (this.cities.containsKey(parentCode)) {
							((List) this.cities.get(parentCode))
									.add(new Option(obj.getString("code"), obj.getString("name")));
						} else {
							tmp = new ArrayList();
							tmp.add(new Option(obj.getString("code"), obj.getString("name")));
							this.cities.put(parentCode, tmp);
						}
					}
					break;
				}

				sb.append(line);
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}

		this.areas = new HashMap();

		try {
			sb = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(util.getResourceAsStream("areas.json"), "utf-8"));

			while (true) {
				line = reader.readLine();
				if (line == null) {
					reader.close();
					reader = null;
					array = JSONArray.fromObject(sb.toString());
					var6 = array.iterator();

					while (var6.hasNext()) {
						object = var6.next();
						obj = (JSONObject) object;
						parentCode = obj.getString("parent_code");
						if (this.areas.containsKey(parentCode)) {
							((List) this.areas.get(parentCode))
									.add(new Option(obj.getString("code"), obj.getString("name")));
						} else {
							tmp = new ArrayList();
							tmp.add(new Option(obj.getString("code"), obj.getString("name")));
							this.areas.put(parentCode, tmp);
						}
					}
					break;
				}

				sb.append(line);
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		}

	}

	private void initComboBoxA(String code) {
		this.boxA.removeAllItems();
		List<Option> list = this.areas.get(code);
		Iterator var4 = list.iterator();

		while (var4.hasNext()) {
			Option option = (Option) var4.next();
			this.boxA.addItem(option);
		}

	}

	private void initComboBoxC(String code) {
		this.boxC.removeAllItems();
		List<Option> list = this.cities.get(code);
		Iterator var4 = list.iterator();

		while (var4.hasNext()) {
			Option option = (Option) var4.next();
			this.boxC.addItem(option);
		}

	}

	private void initComboBoxP() {
		this.boxP.removeAllItems();
		Iterator var2 = this.provinces.iterator();

		while (var2.hasNext()) {
			Option option = (Option) var2.next();
			this.boxP.addItem(option);
		}

	}

	private void actionPerformed(ActionEvent e) {
		Option option;
		if (e.getSource() == this.boxP) {
			option = (Option) this.boxP.getSelectedItem();
			if (option != null) {
				this.initComboBoxC(option.getCode());
				List<Option> list = this.cities.get(option.getCode());
				if (list != null && list.size() > 0) {
					String code = list.get(0).getCode();
					this.initComboBoxA(code);
				}
			}
		} else if (e.getSource() == this.boxC) {
			option = (Option) this.boxC.getSelectedItem();
			if (option != null) {
				this.initComboBoxA(option.getCode());
			}
		} else if (e.getSource() == this.dateButton) {
			Calendar showCalendarDialog = this.cc.showCalendarDialog();
			this.date.setText(this.sf.format(showCalendarDialog.getTime()));
		} else if (e.getSource() == this.submit) {
			this.result.setText("");
			if (this.num.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请输入要生成多少个身份证号！", "提示", 2);
				return;
			}

			String code = ((Option) this.boxA.getSelectedItem()).getCode();
			String d = this.date.getText().replaceAll("-", "");
			int sex;
			if (this.radioG.isSelected()) {
				sex = 1;
			} else {
				sex = 2;
			}

			IdsGenerate g = new IdsGenerate();

			for (int i = 0; i < Integer.parseInt(this.num.getText()); ++i) {
				if (i > 0) {
					this.result.append("\n");
				}

				this.result.append(g.generate(code, d, sex));
			}
		}

	}

	private void initComponents() {
		this.remark = new JLabel();
		this.labelLocation = new JLabel();
		this.panel1 = new JPanel();
		this.boxP = new JComboBox();
		this.boxC = new JComboBox();
		this.boxA = new JComboBox();
		this.labelTime = new JLabel();
		this.panel2 = new JPanel();
		this.date = new JTextField();
		this.dateButton = new JButton();
		this.labelSex = new JLabel();
		this.panel3 = new JPanel();
		this.radioG = new JRadioButton();
		this.radioM = new JRadioButton();
		this.labelNum = new JLabel();
		this.panel4 = new JPanel();
		this.num = new JTextField();
		this.submit = new JButton();
		this.scrollPane1 = new JScrollPane();
		this.result = new JTextArea();
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
		this.setLayout(new FormLayout("left:pref, $lcgap, left:pref:grow", "5*(fill:pref, $lgap), fill:pref:grow"));
		this.remark.setText("仅供开发测试使用，切记不可用于非法活动，否则后果自负！");
		this.remark.setFont(new Font("微软雅黑", 0, 14));
		this.remark.setForeground(Color.red);
		this.add(this.remark, CC.xywh(1, 1, 3, 1, CC.LEFT, CC.FILL));
		this.labelLocation.setText("地点：");
		this.labelLocation.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.labelLocation, CC.xy(1, 3, CC.LEFT, CC.FILL));
		this.panel1.setLayout(new FormLayout("2*(left:[50dlu,pref], $lcgap), left:[50dlu,pref]", "default"));
		this.boxP.setFont(new Font("微软雅黑", 0, 12));
		this.boxP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdsGenerateComponent.this.actionPerformed(e);
			}
		});
		this.panel1.add(this.boxP, CC.xy(1, 1, CC.FILL, CC.FILL));
		this.boxC.setFont(new Font("微软雅黑", 0, 12));
		this.boxC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdsGenerateComponent.this.actionPerformed(e);
			}
		});
		this.panel1.add(this.boxC, CC.xy(3, 1, CC.FILL, CC.FILL));
		this.boxA.setFont(new Font("微软雅黑", 0, 12));
		this.panel1.add(this.boxA, CC.xy(5, 1, CC.FILL, CC.FILL));
		this.add(this.panel1, CC.xy(3, 3));
		this.labelTime.setText("时间：");
		this.labelTime.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.labelTime, CC.xy(1, 5, CC.LEFT, CC.FILL));
		this.panel2.setLayout(new FormLayout("[60dlu,pref], $lcgap, default", "default"));
		this.date.setFont(new Font("微软雅黑", 0, 14));
		this.date.setText("2017-07-31");
		this.date.setEditable(false);
		this.panel2.add(this.date, CC.xy(1, 1, CC.FILL, CC.FILL));
		this.dateButton.setText("获取时间");
		this.dateButton.setFont(new Font("微软雅黑", 0, 14));
		this.dateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdsGenerateComponent.this.actionPerformed(e);
			}
		});
		this.panel2.add(this.dateButton, CC.xy(3, 1, CC.FILL, CC.FILL));
		this.add(this.panel2, CC.xy(3, 5));
		this.labelSex.setText("性别：");
		this.labelSex.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.labelSex, CC.xy(1, 7, CC.LEFT, CC.FILL));
		this.panel3.setLayout(new FormLayout("left:[30dlu,pref], $lcgap, left:[30dlu,pref]", "default"));
		this.radioG.setText("男");
		this.radioG.setFont(new Font("微软雅黑", 0, 14));
		this.radioG.setSelected(true);
		this.panel3.add(this.radioG, CC.xy(1, 1, CC.FILL, CC.FILL));
		this.radioM.setText("女");
		this.radioM.setFont(new Font("微软雅黑", 0, 14));
		this.panel3.add(this.radioM, CC.xy(3, 1, CC.FILL, CC.FILL));
		this.add(this.panel3, CC.xy(3, 7));
		this.labelNum.setText("数量：");
		this.labelNum.setFont(new Font("微软雅黑", 0, 14));
		this.add(this.labelNum, CC.xy(1, 9, CC.LEFT, CC.FILL));
		this.panel4.setLayout(new FormLayout("left:[60dlu,pref], $lcgap, left:pref", "default"));
		this.num.setFont(new Font("微软雅黑", 0, 14));
		this.num.setText("5");
		this.panel4.add(this.num, CC.xy(1, 1, CC.FILL, CC.FILL));
		this.submit.setText("提交");
		this.submit.setFont(new Font("微软雅黑", 0, 14));
		this.submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IdsGenerateComponent.this.actionPerformed(e);
			}
		});
		this.panel4.add(this.submit, CC.xy(3, 1, CC.FILL, CC.FILL));
		this.add(this.panel4, CC.xy(3, 9));
		this.scrollPane1.setViewportView(this.result);
		this.add(this.scrollPane1, CC.xywh(1, 11, 3, 1));
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
		int wLoc = (int) this.labelLocation.getPreferredSize().getWidth();
		int wTime = (int) this.labelTime.getPreferredSize().getWidth();
		int wSex = (int) this.labelSex.getPreferredSize().getWidth();
		int wNum = (int) this.labelNum.getPreferredSize().getWidth();
		this.remark.setBounds(0, 0, width, 19);
		this.labelLocation.setBounds(0, 22, wLoc, 35);
		this.labelTime.setBounds(0, 60, wTime, 35);
		this.labelSex.setBounds(0, 98, wSex, 35);
		this.labelNum.setBounds(0, 136, wNum, 35);
		this.scrollPane1.setPreferredSize(new Dimension(width, height - 19 - 140 - 15));
		this.scrollPane1.revalidate();
		this.scrollPane1.repaint();
	}
}
