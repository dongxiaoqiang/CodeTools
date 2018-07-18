//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.component.implement;

import com.citydo.tools.component.MyComponent;
import com.citydo.tools.service.JsonFormat;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class JsonFormatComponent extends JPanel implements MyComponent {
    Dimension size = null;
    private static final long serialVersionUID = -6023909736862779482L;
    private DefaultTreeModel root = new DefaultTreeModel(new DefaultMutableTreeNode("JSON"));
    private JLabel label1;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea textArea1;
    private JScrollPane scrollPane1;
    private JTree tree;
    private JButton button;
    private JLabel label2;
    private JScrollPane scrollPane3;
    private JTextArea textArea2;

    public JsonFormatComponent() {
        this.initComponents();
        this.tree.setModel(this.root);
        this.tree.setEditable(true);
        this.resized();
    }

    private void actionPerformed(ActionEvent e) {
        JsonFormat jf = new JsonFormat();
        jf.setResult(this.textArea2);
        jf.setTree(this.tree);
        boolean b = jf.format(this.textArea1.getText());
        if (!b) {
            JOptionPane.showMessageDialog(this, "JSON格式错误！", "提示", 2);
        }

    }

    private void initComponents() {
        this.label1 = new JLabel();
        this.label3 = new JLabel();
        this.scrollPane2 = new JScrollPane();
        this.textArea1 = new JTextArea();
        this.scrollPane1 = new JScrollPane();
        this.tree = new JTree();
        this.button = new JButton();
        this.label2 = new JLabel();
        this.scrollPane3 = new JScrollPane();
        this.textArea2 = new JTextArea();
        this.setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "JFormDesigner Evaluation", 2, 5, new Font("Dialog", 1, 12), Color.red), this.getBorder()));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if ("border".equals(e.getPropertyName())) {
                    throw new RuntimeException();
                }
            }
        });
        this.setLayout(new FormLayout("pref:grow, $lcgap, pref:grow", "default, $lgap, pref:grow, 2*($lgap, default), $lgap, pref:grow"));
        this.label1.setText("原始JSON：");
        this.label1.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label1, CC.xy(1, 1, CC.LEFT, CC.FILL));
        this.label3.setText("JSON视图树：");
        this.label3.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label3, CC.xy(3, 1, CC.LEFT, CC.FILL));
        this.scrollPane2.setViewportView(this.textArea1);
        this.add(this.scrollPane2, CC.xy(1, 3, CC.FILL, CC.FILL));
        this.tree.setFont(new Font("微软雅黑", 0, 12));
        this.scrollPane1.setViewportView(this.tree);
        this.add(this.scrollPane1, CC.xywh(3, 3, 1, 7, CC.FILL, CC.FILL));
        this.button.setText("格式化");
        this.button.setFont(new Font("微软雅黑", 0, 14));
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JsonFormatComponent.this.actionPerformed(e);
            }
        });
        this.add(this.button, CC.xy(1, 5, CC.LEFT, CC.FILL));
        this.label2.setText("格式化后的JSON：");
        this.label2.setFont(new Font("微软雅黑", 0, 14));
        this.add(this.label2, CC.xy(1, 7, CC.LEFT, CC.FILL));
        this.scrollPane3.setViewportView(this.textArea2);
        this.add(this.scrollPane3, CC.xy(1, 9, CC.FILL, CC.FILL));
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
        int width2 = width / 2;
        int width1 = width - width2;
        this.scrollPane1.setPreferredSize(new Dimension(width1, height - 19 - 3));
        int height3 = (height - 35 - 6) / 2;
        int height4 = height - height3 - 35 - 6;
        this.button.setBounds(0, height3 + 3, this.button.getWidth(), 35);
        this.scrollPane2.setPreferredSize(new Dimension(width2, height3 - 19 - 3));
        this.scrollPane3.setPreferredSize(new Dimension(width2, height4 - 19 - 3));
        this.scrollPane1.revalidate();
        this.scrollPane3.revalidate();
        this.scrollPane2.revalidate();
        this.scrollPane1.repaint();
        this.scrollPane3.repaint();
        this.scrollPane2.repaint();
    }
}
