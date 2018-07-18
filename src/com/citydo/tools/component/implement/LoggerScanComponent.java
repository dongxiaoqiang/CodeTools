//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.component.implement;

import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.LoggerScanThread;
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

public class LoggerScanComponent extends JPanel implements MyComponent {
    Dimension size = null;
    private static final long serialVersionUID = 183980533477295265L;
    private JFileChooser fileChooser;
    private JLabel label1;
    private JTextField textField;
    private JButton button;
    private JButton start;
    private JScrollPane scrollPane1;
    private JTextArea textArea;

    public LoggerScanComponent() {
        this.initComponents();
        this.resized();
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.button) {
            int state = this.fileChooser.showOpenDialog(this);
            if (state == 1) {
                return;
            }

            File f = this.fileChooser.getSelectedFile();
            this.textField.setText(f.getAbsolutePath());
        } else if (e.getSource() == this.start) {
            if (this.textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择待扫描的文件目录！", "提示", 2);
                return;
            }

            this.start.setEnabled(false);
            this.button.setEnabled(false);
            LoggerScanThread lt = new LoggerScanThread();
            lt.setButton(this.button);
            lt.setStart(this.start);
            lt.setResult(this.textArea);
            lt.setFile(this.textField);
            Thread t = new Thread(lt);
            t.start();
        }

    }

    private void initComponents() {
        this.fileChooser = new JFileChooser("D:\\");
        this.fileChooser.setFileSelectionMode(1);
        this.label1 = new JLabel();
        this.textField = new JTextField();
        this.button = new JButton();
        this.start = new JButton();
        this.scrollPane1 = new JScrollPane();
        this.textArea = new JTextArea();
        this.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", 2, 5, new Font("Dialog", 1, 12), Color.red), this.getBorder()));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) {
                    throw new RuntimeException();
                }
            }
        });
        this.setLayout(new FormLayout("left:pref:grow, 2*($lcgap, left:pref)", "2*(fill:pref, $lgap), fill:pref:grow"));
        this.label1.setText("请选择待扫描的文件目录：");
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
                LoggerScanComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button, CC.xy(3, 3, CC.LEFT, CC.FILL));
        this.start.setText("开始扫描");
        this.start.setFont(new Font("微软雅黑", 0, 14));
        this.start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoggerScanComponent.this.actionPerformed(e);
            }
        });
        this.add(this.start, CC.xy(5, 3, CC.LEFT, CC.FILL));
        this.scrollPane1.setViewportView(this.textArea);
        this.add(this.scrollPane1, CC.xywh(1, 5, 5, 1, CC.FILL, CC.FILL));
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
        this.scrollPane1.setPreferredSize(new Dimension(width, height - 35 - 19 - 6));
        this.scrollPane1.revalidate();
        this.scrollPane1.repaint();
    }
}
