package java.Util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileStorageUtil {
    private static final String BASE_PATH = "src/resource/Userdata/";

    /**
     * 初始化存储目录
     */
    public static void initStorageDirectory() {
        try {
            Path dirPath = Paths.get(BASE_PATH);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("存储目录创建成功: " + dirPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("创建存储目录失败: " + e.getMessage());
        }
    }

    /**
     * 获取实体类对应的存储文件路径
     */
    public static String getEntityFilePath(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        return BASE_PATH + className + ".dat";
    }

    /**
     * 写入实体对象列表到文件（使用 Java 原生序列化）
     */
    public static <T extends Serializable> boolean writeEntitiesToFile(Class<T> entityClass, List<T> entities) {
        try {
            String filePath = getEntityFilePath(entityClass);

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filePath))) {
                oos.writeObject(entities);
            }
            return true;
        } catch (IOException e) {
            System.err.println("写入文件失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从文件读取实体对象列表（使用 Java 原生反序列化）
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> readEntitiesFromFile(Class<T> entityClass) {
        try {
            String filePath = getEntityFilePath(entityClass);
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(filePath))) {
                return (List<T>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读取文件失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 向文件追加实体对象
     */
    public static <T extends Serializable> boolean appendEntityToFile(Class<T> entityClass, T entity) {
        try {
            List<T> entities = readEntitiesFromFile(entityClass);
            entities.add(entity);
            return writeEntitiesToFile(entityClass, entities);
        } catch (Exception e) {
            System.err.println("追加实体失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteEntityFile(Class<?> entityClass) {
        try {
            String filePath = getEntityFilePath(entityClass);
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("删除文件失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查文件是否存在
     */
    public static boolean entityFileExists(Class<?> entityClass) {
        String filePath = getEntityFilePath(entityClass);
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 获取所有实体文件列表
     */
    public static List<String> getAllEntityFiles() {
        try {
            Path dirPath = Paths.get(BASE_PATH);
            if (!Files.exists(dirPath)) {
                return new ArrayList<>();
            }

            return Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("获取文件列表失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 备份实体数据
     */
    public static <T extends Serializable> boolean backupEntities(Class<T> entityClass, String backupName) {
        try {
            String filePath = getEntityFilePath(entityClass);
            String backupPath = BASE_PATH + "backup/" + backupName + "/" + entityClass.getSimpleName() + ".dat";

            Path backupDir = Paths.get(BASE_PATH + "backup/" + backupName);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            Files.copy(Paths.get(filePath), Paths.get(backupPath),
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("备份失败: " + e.getMessage());
            return false;
        }
    }
}