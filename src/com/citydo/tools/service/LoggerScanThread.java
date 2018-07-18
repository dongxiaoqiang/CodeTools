//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LoggerScanThread implements Runnable {
    private JButton start;
    private JButton button;
    private JTextArea result;
    private JTextField file;
    private static Pattern packageReg = Pattern.compile("package(.*);");
    private static Pattern loggerReg = Pattern.compile("LoggerFactory.getLogger\\((.*)\\)");

    public LoggerScanThread() {
    }

    public JTextField getFile() {
        return this.file;
    }

    public JButton getButton() {
        return this.button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public void setFile(JTextField file) {
        this.file = file;
    }

    public JButton getStart() {
        return this.start;
    }

    public void setStart(JButton start) {
        this.start = start;
    }

    public JTextArea getResult() {
        return this.result;
    }

    public void setResult(JTextArea result) {
        this.result = result;
    }

    public void run() {
        this.result.setText("扫描开始...");
        File f = new File(this.file.getText());
        if (!f.exists()) {
            this.result.append("\n目录不存在，请重新选择！");
            this.result.setCaretPosition(this.result.getText().length());
        } else {
            String resultFile = "result.txt";
            this.doScan(f, resultFile);
            this.result.append("\n扫描结束，扫描结果见文件：" + this.file.getText() + File.separator + resultFile);
            this.result.setCaretPosition(this.result.getText().length());
        }

        this.start.setEnabled(true);
        this.button.setEnabled(true);
    }

    private void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            c = null;
        }

    }

    private void doScan(File f, String resultFile) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(f.getCanonicalPath() + File.separator + resultFile));
            this.doScanFile(f, out);
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            this.close(out);
        }

    }

    private void doScanFile(File f, BufferedWriter out) throws Exception {
        File[] listFiles = f.listFiles();
        if (listFiles != null) {
            File[] var7 = listFiles;
            int var6 = listFiles.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                File file = var7[var5];
                if (file.isDirectory()) {
                    this.doScanFile(file, out);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".java")) {
                        this.result.append("\n扫描文件：" + fileName);
                        this.result.setCaretPosition(this.result.getText().length());
                        String packageName = "";
                        String className = fileName.substring(0, fileName.length() - 5);
                        String loggerLine = "";
                        String line = "";
                        BufferedReader in = null;

                        try {
                            in = new BufferedReader(new FileReader(file));
                            line = in.readLine();

                            while(line != null) {
                                Matcher m = packageReg.matcher(line);
                                if (m.find()) {
                                    packageName = m.group(1).trim();
                                    line = in.readLine();
                                } else {
                                    m = loggerReg.matcher(line);
                                    if (m.find()) {
                                        loggerLine = m.group(1).trim();
                                        line = in.readLine();
                                        break;
                                    }

                                    line = in.readLine();
                                }
                            }

                            boolean flag = true;
                            if (!loggerLine.isEmpty()) {
                                if (loggerLine.startsWith("\"")) {
                                    loggerLine = loggerLine.substring(1, loggerLine.length() - 1);
                                    if (!loggerLine.equals(packageName + "." + className)) {
                                        flag = false;
                                    }
                                } else if (loggerLine.endsWith(".class")) {
                                    loggerLine = loggerLine.substring(0, loggerLine.length() - 6);
                                    if (!loggerLine.equals(className)) {
                                        flag = false;
                                    }
                                }
                            }

                            if (!flag) {
                                out.write("package[" + packageName + "], class[" + className + "], logger[" + loggerLine + "]\n");
                            }
                        } catch (Exception var18) {
                            var18.printStackTrace();
                            this.result.append("，解析出错：行[" + line + "]");
                        } finally {
                            this.close(in);
                        }
                    }
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File("C:\\Users\\17050528\\Downloads\\plsadm-biz.2017-12-25-03.log")));
        BufferedWriter out = new BufferedWriter(new FileWriter(new File("C:\\Users\\17050528\\Downloads\\plsadm-biz.2017-12-25-03-result.log")));
        String line = null;
        HashMap cache = new HashMap();

        while(true) {
            String[] split;
            String threadName;
            String sql;
            do {
                if ((line = in.readLine()) == null) {
                    in.close();
                    out.close();
                    return;
                }

                if (line.indexOf("==> Parameters: ") > -1) {
                    split = line.split("\\|");
                    if (split.length > 6) {
                        threadName = split[1];
                        sql = split[5];
                        if (cache.containsKey(threadName)) {
                            out.write(line + "\r\n");
                            out.write(threadName + "||DEBUG|user:|" + sql + "\r\n");
                            out.flush();
                        }

                        cache.put(threadName, sql);
                    }
                }
            } while(line.indexOf("<==      Total: ") <= -1 && line.indexOf("<==    Updates: ") <= -1);

            split = line.split("\\|");
            if (split.length > 6) {
                threadName = split[1];
                sql = split[5];
                if (cache.containsKey(threadName)) {
                    cache.remove(threadName);
                }

                if (line.indexOf("<==      Total: ") > -1) {
                    String tmp = split[split.length - 1];
                    tmp = tmp.substring(tmp.lastIndexOf(":") + 2).trim();

                    try {
                        long l = Long.parseLong(tmp);
                        if (l > 1000L) {
                            out.write(line + "\r\n");
                            out.write(threadName + "||DEBUG|user:|" + sql + "|" + l + "\r\n");
                            out.flush();
                        }
                    } catch (Exception var11) {
                        out.write(line + "\r\n");
                        out.flush();
                    }
                }
            }
        }
    }
}
