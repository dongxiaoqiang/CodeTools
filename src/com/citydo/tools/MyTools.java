//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;

import com.citydo.tools.bean.Menu;
import com.citydo.tools.component.MyComponent;
import com.citydo.tools.util.ResourceUtil;

public class MyTools extends JPanel implements ComponentListener {
    private static final long serialVersionUID = -9195756734254372373L;
    private final JSplitPane splitPane;
    private final JScrollPane left;
    private final JTabbedPane right;
    private JTree tree;
    private Map<String, Component> components = new HashMap();

    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = FrameBorderStyle.translucencySmallShadow;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (Exception var3) {
            ;
        }

        UIManager.put("RootPane.setupButtonVisible", false);
        JFrame frame = new JFrame("我的工具集");
        frame.setDefaultCloseOperation(3);
        MyTools tools = new MyTools();
        frame.getContentPane().add(tools);
        frame.setPreferredSize(new Dimension(900, 700));
        frame.pack();
        frame.setLocationRelativeTo((Component)null);
        frame.setVisible(true);
        frame.setExtendedState(6);
        frame.addComponentListener(tools);
        frame.setResizable(false);
    }

    public MyTools() {
        this.setLayout(new BorderLayout());
        this.left = new JScrollPane(this.createTree());
        this.right = new JTabbedPane();
        this.right.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Iterator iterator = MyTools.this.components.keySet().iterator();

                while(iterator.hasNext()) {
                    String key = (String)iterator.next();
                    if (MyTools.this.components.get(key) == MyTools.this.right.getSelectedComponent()) {
                        DefaultMutableTreeNode top = (DefaultMutableTreeNode)MyTools.this.tree.getModel().getRoot();
                        int childCount = top.getChildCount();

                        for(int i = 0; i < childCount; ++i) {
                            DefaultMutableTreeNode childAt = (DefaultMutableTreeNode)top.getChildAt(i);
                            Menu m = (Menu)childAt.getUserObject();
                            if (m.getMenuId().equals(key)) {
                                MyTools.this.tree.setSelectionPath(new TreePath(childAt.getPath()));
                            }
                        }

                        return;
                    }
                }

            }
        });
        this.splitPane = new JSplitPane(1, this.left, this.right);
        this.splitPane.setContinuousLayout(true);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setDividerLocation(200);
        this.add(this.splitPane, "Center");
        this.setBackground(Color.black);
    }

    private JTree createTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("工具集");

        try {
            InputStream is = (new ResourceUtil()).getResourceAsStream("menu.txt");
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();

            while(line != null) {
                if (line.startsWith("#")) {
                    line = reader.readLine();
                } else {
                    String[] split = line.split(",");
                    if (split.length == 3) {
                        String menuName = split[0];
                        String menuId = split[1];
                        String menuClass = split[2];
                        Menu m = new Menu();
                        m.setMenuId(menuId);
                        m.setMenuName(menuName);
                        m.setMenuClass(menuClass);
                        top.add(new DefaultMutableTreeNode(m));
                    }

                    line = reader.readLine();
                }
            }

            reader.close();
        } catch (IOException var11) {
            ;
        }

        this.tree = new JTree(top) {
            private static final long serialVersionUID = 662928015945004526L;

            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        this.tree.setEditable(true);
        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)MyTools.this.tree.getLastSelectedPathComponent();
                Object obj = selectedNode.getUserObject();
                if (obj instanceof Menu) {
                    Menu m = (Menu)obj;
                    Component pix = null;
                    if (MyTools.this.components.containsKey(m.getMenuId())) {
                        pix = (Component)MyTools.this.components.get(m.getMenuId());
                    } else {
                        pix = MyTools.this.generateComponent(m);
                        MyTools.this.components.put(m.getMenuId(), pix);
                    }

                    boolean b = false;
                    int count = MyTools.this.right.getComponentCount();

                    for(int index = 0; index < count; ++index) {
                        Component c = MyTools.this.right.getComponent(index);
                        if (c == pix) {
                            b = true;
                            break;
                        }
                    }

                    if (!b) {
                        MyTools.this.right.add(m.getMenuName(), pix);
                    }

                    MyTools.this.right.setSelectedComponent(pix);
                }
            }
        });
        return this.tree;
    }

    protected Component generateComponent(Menu menu) {
        try {
            Class<?> c = Class.forName(menu.getMenuClass());
            MyComponent newInstance = (MyComponent)c.newInstance();
            Component result = newInstance.create();
            if (result == null) {
                result = new JLabel(menu.getMenuName());
            }

            return (Component)result;
        } catch (Exception var5) {
            var5.printStackTrace();
            return new JLabel(menu.getMenuName());
        }
    }

    public void componentResized(ComponentEvent e) {
        Component[] components = this.right.getComponents();
        Component[] var6 = components;
        int var5 = components.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Component component = var6[var4];
            if (component instanceof MyComponent) {
                MyComponent m = (MyComponent)component;
                m.resized();
            }
        }

    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }
}
