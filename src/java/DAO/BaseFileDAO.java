package java.DAO;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.FileStorageUtil;

public abstract class BaseFileDAO<T extends Serializable, ID extends Serializable>
        implements GenericDAO<T, ID> {

    // 内存缓存
    protected Map<ID, T> memoryCache = new ConcurrentHashMap<>();
    protected AtomicLong idCounter = new AtomicLong(1);
    protected Class<T> entityClass;

    public BaseFileDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        initStorage();
        loadFromFile();
    }

    /**
     * 初始化存储
     */
    private void initStorage() {
        FileStorageUtil.initStorageDirectory();
    }

    /**
     * 从文件加载数据到内存
     */
    protected void loadFromFile() {
        try {
            List<T> entities = FileStorageUtil.readEntitiesFromFile(entityClass);
            memoryCache.clear();

            for (T entity : entities) {
                ID id = getId(entity);
                if (id != null) {
                    memoryCache.put(id, entity);

                    // 更新ID计数器
                    if (id instanceof Long) {
                        long idValue = (Long) id;
                        if (idValue >= idCounter.get()) {
                            idCounter.set(idValue + 1);
                        }
                    } else if (id instanceof Integer) {
                        int idValue = (Integer) id;
                        if (idValue >= idCounter.get()) {
                            idCounter.set(idValue + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("加载数据失败: " + e.getMessage());
        }
    }

    /**
     * 保存数据到文件
     */
    protected void saveToFile() {
        try {
            List<T> entities = new ArrayList<>(memoryCache.values());
            FileStorageUtil.writeEntitiesToFile(entityClass, entities);
        } catch (Exception e) {
            System.err.println("保存数据失败: " + e.getMessage());
        }
    }

    /**
     * 生成新的ID
     */
    protected ID generateNewId() {
        return (ID) Long.valueOf(idCounter.getAndIncrement());
    }

    /**
     * 抽象方法：获取实体的ID
     */
    protected abstract ID getId(T entity);

    /**
     * 抽象方法：设置实体的ID
     */
    protected abstract void setId(T entity, ID id);

    @Override
    public T save(T entity) {
        try {
            ID id = getId(entity);
            if (id == null) {
                id = generateNewId();
                setId(entity, id);
            }

            memoryCache.put(id, entity);
            saveToFile();
            return entity;
        } catch (Exception e) {
            System.err.println("保存实体失败: " + e.getMessage());
            return null;
        }
    }

    @Override
    public T update(T entity) {
        try {
            ID id = getId(entity);
            if (id == null || !memoryCache.containsKey(id)) {
                return save(entity);
            }

            memoryCache.put(id, entity);
            saveToFile();
            return entity;
        } catch (Exception e) {
            System.err.println("更新实体失败: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteById(ID id) {
        try {
            if (memoryCache.remove(id) != null) {
                saveToFile();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("删除实体失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean softDelete(ID id) {
        try {
            Optional<T> entityOpt = findById(id);
            if (entityOpt.isPresent()) {
                T entity = entityOpt.get();

                // 尝试设置逻辑删除标记
                try {
                    java.lang.reflect.Method setDeletedMethod = entity.getClass().getMethod("setDeleted", Boolean.class);
                    setDeletedMethod.invoke(entity, true);
                    memoryCache.put(id, entity);
                    saveToFile();
                    return true;
                } catch (NoSuchMethodException e) {
                    // 如果没有逻辑删除标记，则物理删除
                    return deleteById(id);
                } catch (Exception e) {
                    System.err.println("逻辑删除失败: " + e.getMessage());
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("逻辑删除失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(memoryCache.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(memoryCache.values());
    }

    @Override
    public List<T> findByCondition(Predicate<T> condition) {
        return memoryCache.values().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(ID id) {
        return memoryCache.containsKey(id);
    }

    @Override
    public long count() {
        return memoryCache.size();
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        List<T> savedEntities = new ArrayList<>();
        for (T entity : entities) {
            T saved = save(entity);
            if (saved != null) {
                savedEntities.add(saved);
            }
        }
        return savedEntities;
    }

    @Override
    public void reload() {
        loadFromFile();
    }

    @Override
    public void clearAll() {
        memoryCache.clear();
        FileStorageUtil.deleteEntityFile(entityClass);
    }
}