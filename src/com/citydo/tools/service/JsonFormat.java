package com.citydo.tools.service;

import java.util.Iterator;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonFormat {
    private JTextArea result;
    private JTree tree;

    public JsonFormat() {
    }

    public JTextArea getResult() {
        return this.result;
    }

    public void setResult(JTextArea result) {
        this.result = result;
    }

    public JTree getTree() {
        return this.tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    public boolean format(String str) {
        try {
            StringBuffer sb = new StringBuffer();
            DefaultMutableTreeNode rootNode = null;
            if (str.startsWith("{")) {
                JSONObject root = JSONObject.fromObject(str);
                rootNode = new DefaultMutableTreeNode("{}JSON");
                this.format((JSONObject)root, sb, 0, "", rootNode);
            } else {
                if (!str.startsWith("[")) {
                    return false;
                }

                JSONArray root = JSONArray.fromObject(str);
                rootNode = new DefaultMutableTreeNode("[]JSON");
                this.format((JSONArray)root, sb, 0, "", rootNode);
            }

            this.result.setText(sb.toString());
            this.tree.setModel(new DefaultTreeModel(rootNode));
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    private void format(JSONArray root, StringBuffer sb, int index, String key, DefaultMutableTreeNode rootNode) {
        if (index > 0) {
            sb.append("\n");
            sb.append(this.addSpace(index));
            if (!key.isEmpty()) {
                sb.append("\"" + key + "\": ");
            }
        }

        sb.append("[");
        int i = 0;
        boolean first = true;

        for(Iterator var9 = root.iterator(); var9.hasNext(); ++i) {
            Object value = var9.next();
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            DefaultMutableTreeNode node = null;
            if (value instanceof JSONObject) {
                JSONObject obj = (JSONObject)value;
                node = new DefaultMutableTreeNode("{}" + i);
                this.format(obj, sb, index + 1, "", node);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray)value;
                node = new DefaultMutableTreeNode("[]" + i);
                this.format(array, sb, index + 1, "", node);
            } else {
                String str = (String)value;
                node = new DefaultMutableTreeNode(i + ": \"" + str + "\"");
                this.format(str, sb, index + 1, "");
            }

            rootNode.add(node);
        }

        sb.append("\n");
        if (index > 0) {
            sb.append(this.addSpace(index));
        }

        sb.append("]");
    }

    private void format(JSONObject root, StringBuffer sb, int index, String key, DefaultMutableTreeNode rootNode) {
        if (index > 0) {
            sb.append("\n");
            sb.append(this.addSpace(index));
            if (!key.isEmpty()) {
                sb.append("\"" + key + "\": ");
            }
        }

        sb.append("{");
        boolean first = true;

        DefaultMutableTreeNode node;
        for(Iterator keys = root.keys(); keys.hasNext(); rootNode.add(node)) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            String k = (String)keys.next();
            Object value = root.get(k);
            node = null;
            if (value instanceof JSONObject) {
                JSONObject obj = (JSONObject)value;
                node = new DefaultMutableTreeNode("{}" + k);
                this.format(obj, sb, index + 1, k, node);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray)value;
                node = new DefaultMutableTreeNode("[]" + k);
                this.format(array, sb, index + 1, k, node);
            } else {
                String str = value.toString();
                node = new DefaultMutableTreeNode(k + ": \"" + str + "\"");
                this.format(str, sb, index + 1, k);
            }
        }

        sb.append("\n");
        if (index > 0) {
            sb.append(this.addSpace(index));
        }

        sb.append("}");
    }

    private void format(String str, StringBuffer sb, int index, String key) {
        if (index > 0) {
            sb.append("\n");
            sb.append(this.addSpace(index));
        }

        if (!key.isEmpty()) {
            sb.append("\"" + key + "\": ");
        }

        sb.append("\"" + str + "\"");
    }

    private String addSpace(int index) {
        StringBuffer sb = new StringBuffer();

        for(int i = 1; i <= index; ++i) {
            sb.append("    ");
        }

        return sb.toString();
    }
}
