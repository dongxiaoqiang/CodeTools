//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.component.implement;

import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.LoggerSearchThread;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
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

public class LoggerSearchComponent extends JPanel implements MyComponent {
    Dimension size = null;
    private static final long serialVersionUID = 183980533477295265L;
    private JFileChooser fileChooser;
    private JFileChooser fileChooser2;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JTextField textField;
    private JTextField textField2;
    private JButton button;
    private JButton button2;
    private JButton start;
    private JScrollPane scrollPane1;
    private JTextArea textArea;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;

    public LoggerSearchComponent() {
        this.initComponents();
        this.resized();
    }

    private void actionPerformed(ActionEvent e) {
        int state;
        File f;
        if (e.getSource() == this.button) {
            state = this.fileChooser.showOpenDialog(this);
            if (state == 1) {
                return;
            }

            f = this.fileChooser.getSelectedFile();
            this.textField.setText(f.getAbsolutePath());
        } else if (e.getSource() == this.button2) {
            state = this.fileChooser2.showOpenDialog(this);
            if (state == 1) {
                return;
            }

            f = this.fileChooser2.getSelectedFile();
            this.textField2.setText(f.getAbsolutePath());
        } else if (e.getSource() == this.start) {
            if (this.textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择日志文件所在目录！", "提示", 2);
                return;
            }

            if (this.textArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先输入检索关键字！", "提示", 2);
                return;
            }

            this.start.setEnabled(false);
            this.button.setEnabled(false);
            this.button2.setEnabled(false);
            this.textArea2.setText("");
            this.textArea2.setCaretPosition(this.textArea2.getText().length());
            LoggerSearchThread lt = new LoggerSearchThread();
            lt.setFileDir(this.textField.getText());
            lt.setResultDir(this.textField2.getText());
            lt.setSearchKey(this.textArea.getText());
            lt.setButton(this.button);
            lt.setButton2(this.button2);
            lt.setStart(this.start);
            lt.setTextarea(this.textArea2);
            Thread t = new Thread(lt);
            t.start();
        }

    }

    private void initComponents() {
        String usrHome = System.getProperty("user.home");
        String dir = usrHome + File.separator + "Downloads";
        this.fileChooser = new JFileChooser(dir);
        this.fileChooser.setFileSelectionMode(1);
        this.fileChooser2 = new JFileChooser(dir);
        this.fileChooser2.setFileSelectionMode(1);
        this.label1 = new JLabel();
        this.label2 = new JLabel();
        this.label3 = new JLabel();
        this.label4 = new JLabel();
        this.textField = new JTextField();
        this.textField2 = new JTextField();
        this.button = new JButton();
        this.button2 = new JButton();
        this.start = new JButton();
        this.scrollPane1 = new JScrollPane();
        this.textArea = new JTextArea();
        this.scrollPane2 = new JScrollPane();
        this.textArea2 = new JTextArea();
        this.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", 2, 5, new Font("Dialog", 1, 12), Color.red), this.getBorder()));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) {
                    throw new RuntimeException();
                }
            }
        });
        this.setLayout(new FormLayout("left:pref:grow, 2*($lcgap, left:pref)", "8*(fill:pref, $lgap), fill:pref:grow"));
        this.label1.setText("请选择待检索日志文件所在目录(系统检索该目录下的所有文件，包含子目录)");
        this.label1.setFont(new Font("微软雅黑", 0, 14));
        this.label1.setForeground(Color.red);
        this.add(this.label1, CC.xywh(1, 1, 5, 1, CC.LEFT, CC.FILL));
        this.textField.setFont(new Font("微软雅黑", 0, 14));
        this.textField.setEditable(false);
        this.add(this.textField, CC.xy(1, 3, CC.FILL, CC.FILL));
        this.button.setText("浏览");
        this.button.setFont(new Font("微软雅黑", 0, 14));
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoggerSearchComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button, CC.xy(3, 3, CC.LEFT, CC.FILL));
        this.label2.setText("请选择检索结果存储目录，默认日志文件所在目录(系统在该目录下生成search-result.log)");
        this.label2.setFont(new Font("微软雅黑", 0, 14));
        this.label2.setForeground(Color.red);
        this.add(this.label2, CC.xywh(1, 5, 5, 1, CC.LEFT, CC.FILL));
        this.textField2.setFont(new Font("微软雅黑", 0, 14));
        this.textField2.setEditable(false);
        this.add(this.textField2, CC.xy(1, 7, CC.FILL, CC.FILL));
        this.button2.setText("浏览");
        this.button2.setFont(new Font("微软雅黑", 0, 14));
        this.button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoggerSearchComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button2, CC.xy(3, 7, CC.LEFT, CC.FILL));
        this.start.setText("开始检索");
        this.start.setFont(new Font("微软雅黑", 0, 14));
        this.start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoggerSearchComponent.this.actionPerformed(e);
            }
        });
        this.add(this.start, CC.xy(5, 7, CC.LEFT, CC.FILL));
        this.label3.setText("请输入检索关键字(逗号分隔)");
        this.label3.setFont(new Font("微软雅黑", 0, 14));
        this.label3.setForeground(Color.red);
        this.add(this.label3, CC.xywh(1, 9, 5, 1, CC.LEFT, CC.FILL));
        this.scrollPane1.setViewportView(this.textArea);
        this.add(this.scrollPane1, CC.xywh(1, 11, 5, 1, CC.FILL, CC.FILL));
        this.label4.setText("检索过程输出");
        this.label4.setFont(new Font("微软雅黑", 0, 14));
        this.label4.setForeground(Color.red);
        this.add(this.label4, CC.xywh(1, 13, 5, 1, CC.LEFT, CC.FILL));
        this.textArea2.setEditable(false);
        this.scrollPane2.setViewportView(this.textArea2);
        this.add(this.scrollPane2, CC.xywh(1, 15, 5, 1, CC.FILL, CC.FILL));
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
        this.label1.setBounds(0, 0, width, 19);
        int width1 = (int)this.button.getPreferredSize().getWidth();
        int width2 = (int)this.start.getPreferredSize().getWidth();
        int w = width - width1 - width2;
        this.textField.setBounds(0, 22, w, 35);
        this.button.setBounds(w, 22, width1, 35);
        this.start.setBounds(w + width1, 22, width2, 35);
        this.scrollPane1.setPreferredSize(new Dimension(width, height - 140 - 19 - 6));
        this.scrollPane1.revalidate();
        this.scrollPane1.repaint();
        this.scrollPane2.setPreferredSize(new Dimension(width, height + 9));
        this.scrollPane2.revalidate();
        this.scrollPane2.repaint();
    }
}
