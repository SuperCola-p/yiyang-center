package java.Util;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DataBackupUtil {

    /**
     * 备份所有数据
     */
    public static boolean backupAllData(String backupName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupPath = "src/resource/Userdata/backup/" +
                    (backupName != null ? backupName : "backup_" + timestamp) + "/";

            Files.createDirectories(Paths.get(backupPath));

            // 获取所有数据文件
            List<String> files = FileStorageUtil.getAllEntityFiles();

            for (String file : files) {
                String sourceFile = "src/resource/Userdata/" + file;
                String targetFile = backupPath + file;

                Files.copy(Paths.get(sourceFile), Paths.get(targetFile),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return true;
        } catch (IOException e) {
            System.err.println("备份失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 恢复备份
     */
    public static boolean restoreBackup(String backupName) {
        try {
            String backupPath = "src/resource/Userdata/backup/" + backupName + "/";

            if (!Files.exists(Paths.get(backupPath))) {
                System.err.println("备份不存在: " + backupName);
                return false;
            }

            // 获取备份文件
            List<String> backupFiles = Files.list(Paths.get(backupPath))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

            for (String file : backupFiles) {
                String sourceFile = backupPath + file;
                String targetFile = "src/resource/Userdata/" + file;

                Files.copy(Paths.get(sourceFile), Paths.get(targetFile),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return true;
        } catch (IOException e) {
            System.err.println("恢复失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 列出所有备份
     */
    public static List<String> listBackups() {
        try {
            String backupDir = "src/resource/Userdata/backup/";
            if (!Files.exists(Paths.get(backupDir))) {
                return List.of();
            }

            return Files.list(Paths.get(backupDir))
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("获取备份列表失败: " + e.getMessage());
            return List.of();
        }
    }
}