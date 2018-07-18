package com.citydo.tools.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import javax.swing.JButton;
import javax.swing.JTextArea;
import org.apache.commons.lang.StringUtils;

public class LoggerSearchThread implements Runnable {
    private String fileDir;
    private String resultDir;
    private String searchKey;
    private JButton button;
    private JButton button2;
    private JButton start;
    private JTextArea textarea;
    ExecutorService cachedThreadPool = Executors.newFixedThreadPool(4);

    public LoggerSearchThread() {
    }

    public String getFileDir() {
        return this.fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getResultDir() {
        return this.resultDir;
    }

    public void setResultDir(String resultDir) {
        this.resultDir = resultDir;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public JButton getButton() {
        return this.button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public JButton getButton2() {
        return this.button2;
    }

    public void setButton2(JButton button2) {
        this.button2 = button2;
    }

    public JButton getStart() {
        return this.start;
    }

    public void setStart(JButton start) {
        this.start = start;
    }

    public JTextArea getTextarea() {
        return this.textarea;
    }

    public void setTextarea(JTextArea textarea) {
        this.textarea = textarea;
    }

    public void run() {
        if (StringUtils.isEmpty(this.resultDir)) {
            this.resultDir = this.fileDir;
        }

        List<String> searchList = Arrays.asList(this.searchKey.split(","));
        File f = new File(this.fileDir);
        File[] listFiles = f.listFiles();
        File outFile = new File(this.resultDir + File.separator + "search-result.log");
        if (outFile.exists()) {
            outFile.delete();
        }

        BufferedWriter out = null;
        this.appendMsg("开始检索");

        try {
            File[] var9 = listFiles;
            int var8 = listFiles.length;

            File file;
            int var7;
            for(var7 = 0; var7 < var8; ++var7) {
                file = var9[var7];
                this.unzip(file);
            }

            this.cachedThreadPool.shutdown();

            while(!this.cachedThreadPool.isTerminated()) {
                Thread.sleep(2000L);
            }

            listFiles = f.listFiles();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            var9 = listFiles;
            var8 = listFiles.length;

            for(var7 = 0; var7 < var8; ++var7) {
                file = var9[var7];
                this.search(file, out, searchList);
            }
        } catch (Exception var20) {
            var20.printStackTrace();
            this.appendMsg("\n检索异常：" + var20.getLocalizedMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var18) {
                    var18.printStackTrace();
                }

                out = null;
            }

        }

        this.start.setEnabled(true);
        this.button.setEnabled(true);
        this.button2.setEnabled(true);
        this.appendMsg("\n检索结束");

        try {
            Runtime.getRuntime().exec("cmd.exe /c start " + outFile.getCanonicalPath());
        } catch (IOException var19) {
            var19.printStackTrace();
        }

    }

    private void appendMsg(String msg) {
        this.textarea.append(msg);
        this.textarea.setCaretPosition(this.textarea.getText().length());
    }

    private void unzip(File file) throws Exception {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            File[] var6 = listFiles;
            int var5 = listFiles.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                File file2 = var6[var4];
                this.unzip(file2);
            }
        } else if (file.getCanonicalPath().endsWith(".log.gz")) {
            this.cachedThreadPool.execute(new LoggerSearchThread.UnzipThread(file));
        }

    }

    private void search(File file, BufferedWriter out, List<String> searchList) throws Exception {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            File[] var8 = listFiles;
            int var7 = listFiles.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                File file2 = var8[var6];
                this.search(file2, out, searchList);
            }
        } else {
            String prefix = "";
            String fileName = file.getName();
            if (fileName.equals("search-result.log")) {
                return;
            }

            if (fileName.indexOf(".") != -1) {
                String str = fileName.split("\\.")[1];
                if (str.length() >= 10) {
                    prefix = str.substring(0, 10);
                }
            }

            this.appendMsg("\n" + file.getAbsolutePath() + "开始检索");
            out.write(file.getAbsolutePath() + "开始检索\r\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line = in.readLine();

            while(true) {
                while(line != null) {
                    boolean flag = true;
                    Iterator var10 = searchList.iterator();

                    while(var10.hasNext()) {
                        String str = (String)var10.next();
                        if (line.indexOf(str) == -1) {
                            flag = false;
                            break;
                        }
                    }

                    if (!flag) {
                        line = in.readLine();
                    } else {
                        out.write(line + "\r\n");
                        out.flush();

                        while(true) {
                            line = in.readLine();
                            if (line == null || line.startsWith(prefix)) {
                                break;
                            }

                            out.write(line + "\r\n");
                        }
                    }
                }

                in.close();
                out.write(file.getAbsolutePath() + "检索结束\r\n\r\n");
                this.appendMsg("\n" + file.getAbsolutePath() + "检索结束");
                break;
            }
        }

    }

    public class UnzipThread implements Runnable {
        private File file;

        public UnzipThread() {
        }

        public UnzipThread(File file) {
            this.file = file;
        }

        public void run() {
            try {
                LoggerSearchThread.this.appendMsg("\n" + this.file.getAbsolutePath() + "开始解压");
                String name = this.file.getName();
                String dirName = name.substring(0, name.indexOf(".log.gz"));
                String fileName = name.substring(0, name.indexOf(".gz"));
                String path = this.file.getParentFile().getCanonicalPath();
                String destDir = path + File.separator + dirName;
                File tmp = new File(destDir);
                if (!tmp.exists()) {
                    tmp.mkdir();
                }

                String destFile = destDir + File.separator + fileName;
                tmp = new File(destFile);
                if (tmp.exists()) {
                    tmp.delete();
                }

                FileInputStream fin = new FileInputStream(this.file);
                GZIPInputStream gzin = new GZIPInputStream(fin);
                FileOutputStream fout = new FileOutputStream(destFile);
                byte[] buf = new byte[1024];

                int num;
                while((num = gzin.read(buf, 0, buf.length)) != -1) {
                    fout.write(buf, 0, num);
                }

                gzin.close();
                fout.close();
                fin.close();
                this.file.delete();
                LoggerSearchThread.this.appendMsg("\n" + this.file.getAbsolutePath() + "解压完成");
            } catch (Exception var13) {
                ;
            }

        }
    }
}
