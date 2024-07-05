package com.kimura.netty.test.tcp;

import java.io.File;

public class ReadFileName {
    public static void main(String[] args) {
        String folderPath = "D:\\project\\gansu\\新建文件夹";    // 替换为文件夹的实际路径
        File folder = new File(folderPath);

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    String fileNameWithoutExtension = file.getName().split("\\.")[0];

                    System.out.println(fileNameWithoutExtension);
                }
            }
        }
    }
}
