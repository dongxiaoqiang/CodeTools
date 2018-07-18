package com.citydo.tools.component.implement;

import com.citydo.tools.component.MyComponent;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import com.suning.epps.merchantsignature.SignatureUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ParamSignComponent extends JPanel implements MyComponent {
    private static final long serialVersionUID = -956181746488126454L;
    Dimension size = null;
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JPanel panel1;
    private JLabel label9;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button1;
    private JLabel label3;
    private JTextField textField2;
    private JLabel label4;
    private JTextField textField3;

    public ParamSignComponent() {
        this.initComponents();
        this.resized();
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.button1) {
            if (this.textField1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "私钥不能为空！", "提示", 2);
                return;
            }

            if (this.textArea1.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "参数不能为空！", "提示", 2);
                return;
            }

            this.textField2.setText("");
            this.textField3.setText("");
            String paramStr = this.textArea1.getText();
            String[] params = paramStr.split("\n");
            Map<String, String> paramMap = new HashMap();
            String[] var8 = params;
            int var7 = params.length;
            int var6 = 0;

            while(true) {
                if (var6 >= var7) {
                    List<String> excudeKeylist = new ArrayList();
                    excudeKeylist.add("sign_type");
                    excudeKeylist.add("signkey_index");
                    excudeKeylist.add("sign");

                    String md5;
                    try {
                        md5 = SignatureUtil.digest(paramMap, excudeKeylist);
                    } catch (Exception var11) {
                        JOptionPane.showMessageDialog(this, "MD5计算出错：" + var11.getMessage() + "！", "提示", 2);
                        return;
                    }

                    this.textField2.setText(md5);

                    String sign;
                    try {
                        sign = SignatureUtil.sign(md5, this.textField1.getText());
                    } catch (Exception var10) {
                        JOptionPane.showMessageDialog(this, "签名出错：" + var10.getMessage() + "！", "提示", 2);
                        return;
                    }

                    this.textField3.setText(sign);
                    break;
                }

                String str = var8[var6];
                String[] strs = str.split("=");
                if (strs.length != 2 || strs[0].isEmpty() || strs[1].isEmpty()) {
                    JOptionPane.showMessageDialog(this, "参数格式错误！", "提示", 2);
                    return;
                }

                paramMap.put(strs[0], strs[1]);
                ++var6;
            }
        }

    }

    private void initComponents() {
        this.label1 = new JLabel();
        this.textField1 = new JTextField();
        this.label2 = new JLabel();
        this.panel1 = new JPanel();
        this.label9 = new JLabel();
        this.scrollPane1 = new JScrollPane();
        this.textArea1 = new JTextArea();
        this.button1 = new JButton();
        this.label3 = new JLabel();
        this.textField2 = new JTextField();
        this.label4 = new JLabel();
        this.textField3 = new JTextField();
        this.setFont(new Font("微软雅黑", 0, 14));
        this.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", 2, 5, new Font("Dialog", 1, 12), Color.red), this.getBorder()));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) {
                    throw new RuntimeException();
                }
            }
        });
        this.setLayout(new FormLayout("pref, $lcgap, pref:grow", "pref, $lgap, pref:grow, 3*($lgap, pref)"));
        this.label1.setText("私钥：");
        this.label1.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label1, CC.xy(1, 1, CC.LEFT, CC.CENTER));
        this.textField1.setFont(new Font("微软雅黑", 0, 12));
        this.add(this.textField1, CC.xy(3, 1, CC.FILL, CC.FILL));
        this.label2.setText("参数：");
        this.label2.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label2, CC.xy(1, 3, CC.LEFT, CC.CENTER));
        this.panel1.setLayout(new FormLayout("pref:grow", "pref, $lgap, pref:grow"));
        this.label9.setText("每行内容格式：key=value");
        this.label9.setFont(new Font("微软雅黑", 0, 14));
        this.label9.setForeground(Color.red);
        this.panel1.add(this.label9, CC.xy(1, 1, CC.LEFT, CC.FILL));
        this.scrollPane1.setViewportView(this.textArea1);
        this.panel1.add(this.scrollPane1, CC.xy(1, 3, CC.FILL, CC.FILL));
        this.add(this.panel1, CC.xy(3, 3, CC.FILL, CC.FILL));
        this.button1.setText("签名");
        this.button1.setFont(new Font("微软雅黑", 0, 14));
        this.button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ParamSignComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button1, CC.xywh(1, 5, 3, 1, CC.LEFT, CC.CENTER));
        this.label3.setText("MD5：");
        this.label3.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label3, CC.xy(1, 7, CC.LEFT, CC.CENTER));
        this.textField2.setFont(new Font("微软雅黑", 0, 12));
        this.textField2.setEditable(false);
        this.add(this.textField2, CC.xy(3, 7, CC.FILL, CC.FILL));
        this.label4.setText("SIGN：");
        this.label4.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label4, CC.xy(1, 9, CC.LEFT, CC.CENTER));
        this.textField3.setFont(new Font("微软雅黑", 0, 12));
        this.textField3.setEditable(false);
        this.add(this.textField3, CC.xy(3, 9, CC.FILL, CC.FILL));
    }

    public Component create() {
        return this;
    }

    public void resized() {
        if (this.size == null) {
            this.size = this.getPreferredSize();
        }

        int width = (int)this.size.getWidth();
        int height = (int)this.size.getHeight();
        int h = height - 35 - 57 - 12;
        this.label1.setBounds(0, 0, this.label1.getWidth(), 19);
        this.label2.setBounds(0, 22, this.label2.getWidth(), h);
        this.button1.setBounds(0, 19 + h + 6, this.button1.getWidth(), 35);
        this.label3.setBounds(0, 19 + h + 35 + 9, this.label3.getWidth(), 19);
        this.label4.setBounds(0, 38 + h + 35 + 12, this.label4.getWidth(), 19);
        this.scrollPane1.setPreferredSize(new Dimension(width - this.label2.getWidth() - 3, h - 19 - 3));
        this.scrollPane1.revalidate();
        this.scrollPane1.repaint();
    }
}
