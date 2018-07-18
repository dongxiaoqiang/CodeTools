//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ResourceUtil {
    public static final String GenerateCode_dbUrl = "GenerateCode.dbUrl";
    public static final String GenerateCode_dbUsername = "GenerateCode.dbUsername";
    public static final String GenerateCode_dbPassword = "GenerateCode.dbPassword";
    public static final String GenerateCode_tableName = "GenerateCode.tableName";
    public static final String GenerateCode_tableNamePrefix = "GenerateCode.tableNamePrefix";
    public static final String GenerateCode_mycat = "GenerateCode.mycat";
    public static final String GenerateCode_dirPath = "GenerateCode.dirPath";
    public static final String GenerateCode_entity = "GenerateCode.entity";
    public static final String GenerateCode_dao = "GenerateCode.dao";
    public static final String GenerateCode_service = "GenerateCode.service";
    public static final String GenerateCode_namespace = "GenerateCode.namespace";
    public static final String GenerateCode_user = "GenerateCode.user";
    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public ResourceUtil() {
    }

    public InputStream getResourceAsStream(String resourceName) {
        InputStream is = this.getClass().getResourceAsStream("resource/" + resourceName);
        return is;
    }

    public Properties getConfig() {
        rwl.readLock().lock();

        Properties var9;
        try {
            String usrHome = System.getProperty("user.home");
            File dir = new File(usrHome + File.separator + "myTools");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            BufferedReader in = null;
            Properties p = new Properties();

            try {
                File file = new File(dir.getCanonicalPath() + File.separator + "myTools.properties");
                if (file.exists()) {
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    p.load(in);
                }
            } catch (IOException var23) {
                ;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var22) {
                        ;
                    }
                }

            }

            var9 = p;
        } finally {
            rwl.readLock().unlock();
        }

        return var9;
    }

    public void updateConfig(Properties p) {
        rwl.writeLock().lock();

        try {
            String usrHome = System.getProperty("user.home");
            File dir = new File(usrHome + File.separator + "myTools");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            BufferedWriter out = null;
            BufferedReader in = null;

            try {
                Properties config = new Properties();
                File file = new File(dir.getCanonicalPath() + File.separator + "myTools.properties");
                if (file.exists()) {
                    in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    config.load(in);
                    in.close();
                    in = null;
                    file.delete();
                }

                Enumeration e = p.keys();

                while(e.hasMoreElements()) {
                    String key = (String)e.nextElement();
                    String val = (String)p.get(key);
                    config.put(key, val);
                }

                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                config.store(out, "store");
            } catch (IOException var34) {
                ;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException var33) {
                        ;
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var32) {
                        var32.printStackTrace();
                    }
                }

            }
        } finally {
            rwl.writeLock().unlock();
        }

    }
}
