// FileStorageUtil.java - 文件存储通用工具类
package java.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileStorageUtil {
    private static final String BASE_PATH = "src/resource/Userdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 配置ObjectMapper支持Java 8时间类型
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 获取实体类对应的存储文件路径
     */
    public static String getEntityFilePath(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        return BASE_PATH + className + ".txt";
    }

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
     * 写入实体对象列表到文件
     */
    public static <T> boolean writeEntitiesToFile(Class<T> entityClass, List<T> entities) {
        try {
            String filePath = getEntityFilePath(entityClass);
            String jsonContent = objectMapper.writeValueAsString(entities);

            Files.write(Paths.get(filePath), jsonContent.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("写入文件失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 从文件读取实体对象列表
     */
    public static <T> List<T> readEntitiesFromFile(Class<T> entityClass) {
        try {
            String filePath = getEntityFilePath(entityClass);
            File file = new File(filePath);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // 处理空文件
            if (jsonContent.trim().isEmpty()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(jsonContent,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, entityClass));
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 向文件追加实体对象
     */
    public static <T> boolean appendEntityToFile(Class<T> entityClass, T entity) {
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
     * 清空实体文件
     */
    public static boolean clearEntityFile(Class<?> entityClass) {
        try {
            String filePath = getEntityFilePath(entityClass);
            Files.write(Paths.get(filePath), "[]".getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("清空文件失败: " + e.getMessage());
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
     * 获取文件大小
     */
    public static long getEntityFileSize(Class<?> entityClass) {
        try {
            String filePath = getEntityFilePath(entityClass);
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 删除实体文件
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
}