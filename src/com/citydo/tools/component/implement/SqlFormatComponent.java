//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.component.implement;

import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.SqlFormat;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class SqlFormatComponent extends JPanel implements MyComponent {
    private static final long serialVersionUID = -8550441190060576341L;
    Dimension preferredSize = null;
    private JPanel panel1;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button;
    private JPanel panel2;
    private JLabel label2;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;

    public SqlFormatComponent() {
        this.initComponents();
        this.resized();
    }

    private void actionPerformed(ActionEvent e) {
        String old = this.textArea1.getText();
        String newSQL = (new SqlFormat()).format(old);
        this.textArea2.setText(newSQL);
    }

    private void initComponents() {
        this.panel1 = new JPanel();
        this.label1 = new JLabel();
        this.scrollPane1 = new JScrollPane();
        this.textArea1 = new JTextArea();
        this.button = new JButton();
        this.panel2 = new JPanel();
        this.label2 = new JLabel();
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
        this.setLayout(new FormLayout("pref:grow", "fill:pref:grow, $lgap, fill:pref, $lgap, fill:pref:grow"));
        this.panel1.setLayout(new FormLayout("pref:grow", "fill:pref, $lgap, fill:pref:grow"));
        this.label1.setText("原始SQL：");
        this.label1.setFont(new Font("微软雅黑", 0, 14));
        this.panel1.add(this.label1, CC.xy(1, 1));
        this.scrollPane1.setViewportView(this.textArea1);
        this.panel1.add(this.scrollPane1, CC.xy(1, 3));
        this.add(this.panel1, CC.xy(1, 1, CC.FILL, CC.FILL));
        this.button.setText("格式化");
        this.button.setFont(new Font("微软雅黑", 0, 14));
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SqlFormatComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button, CC.xy(1, 3, CC.LEFT, CC.FILL));
        this.panel2.setLayout(new FormLayout("pref:grow", "fill:pref, $lgap, fill:pref:grow"));
        this.label2.setText("格式化后的SQL：");
        this.label2.setFont(new Font("微软雅黑", 0, 14));
        this.panel2.add(this.label2, CC.xy(1, 1));
        this.scrollPane2.setViewportView(this.textArea2);
        this.panel2.add(this.scrollPane2, CC.xy(1, 3));
        this.add(this.panel2, CC.xy(1, 5));
    }

    public Component create() {
        return this;
    }

    public void resized() {
        if (this.preferredSize == null) {
            this.preferredSize = this.getPreferredSize();
        }

        int width = (int)this.preferredSize.getWidth();
        int height = (int)this.preferredSize.getHeight();
        int height1 = (height - 35 - 6) / 2;
        int height2 = height - height1 - 35 - 6;
        this.panel1.setBounds(0, 0, width, height1);
        this.button.setBounds(0, height1 + 3, this.button.getWidth(), 35);
        this.panel2.setBounds(0, height1 + 35 + 6, width, height2);
        this.scrollPane1.setPreferredSize(new Dimension(width, height1 - 19 - 3));
        this.scrollPane2.setPreferredSize(new Dimension(width, height2 - 19 - 3));
        this.scrollPane1.revalidate();
        this.scrollPane2.revalidate();
        this.scrollPane1.repaint();
        this.scrollPane2.repaint();
    }
}
