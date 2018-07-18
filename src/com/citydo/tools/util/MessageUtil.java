//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.util;

import java.io.BufferedWriter;
import javax.swing.JTextArea;

public class MessageUtil {
    public MessageUtil() {
    }

    public static void appendMsg(JTextArea textarea, String msg) {
        textarea.append(msg + "\n");
        textarea.setCaretPosition(textarea.getText().length());
    }

    public static void appendLine(BufferedWriter br, String line) throws Exception {
        br.write(line);
        br.newLine();
    }
}
