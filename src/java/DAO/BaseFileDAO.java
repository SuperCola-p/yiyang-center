package java.DAO;

import java.Util.FileStorageUtil;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class BaseFileDAO<T extends Serializable, ID extends Serializable>
        implements GenericDAO<T, ID> {

    protected final Map<ID, T> memoryCache = new ConcurrentHashMap<>();
    protected final AtomicLong idCounter = new AtomicLong(1);
    protected final Class<T> entityClass;

    public BaseFileDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        FileStorageUtil.initStorageDirectory();
        loadFromFile();
    }

    protected void loadFromFile() {
        try {
            List<T> entities = FileStorageUtil.readEntitiesFromFile(entityClass);
            memoryCache.clear();
            for (T entity : entities) {
                ID id = getId(entity);
                if (id == null) {
                    continue;
                }
                memoryCache.put(id, entity);
                updateCounterById(id);
            }
        } catch (Exception e) {
            System.err.println("加载数据失败: " + e.getMessage());
        }
    }

    protected void saveToFile() {
        try {
            FileStorageUtil.writeEntitiesToFile(entityClass, new ArrayList<>(memoryCache.values()));
        } catch (Exception e) {
            System.err.println("保存数据失败: " + e.getMessage());
        }
    }

    protected void updateCounterById(ID id) {
        if (id instanceof Integer) {
            int value = (Integer) id;
            if (value >= idCounter.get()) {
                idCounter.set(value + 1L);
            }
        } else if (id instanceof Long) {
            long value = (Long) id;
            if (value >= idCounter.get()) {
                idCounter.set(value + 1L);
            }
        }
    }

    protected ID generateNewId() {
        long nextId = idCounter.getAndIncrement();
        Class<?> idType = resolveIdType();
        if (Integer.class.equals(idType) || int.class.equals(idType)) {
            return (ID) Integer.valueOf((int) nextId);
        }
        if (String.class.equals(idType)) {
            return (ID) String.valueOf(nextId);
        }
        return (ID) Long.valueOf(nextId);
    }

    private Class<?> resolveIdType() {
        try {
            Method getIdMethod = entityClass.getMethod("getId");
            return getIdMethod.getReturnType();
        } catch (Exception ignored) {
            return Long.class;
        }
    }

    protected abstract ID getId(T entity);

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
            updateCounterById(id);
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
        if (memoryCache.remove(id) != null) {
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean softDelete(ID id) {
        Optional<T> entityOpt = findById(id);
        if (!entityOpt.isPresent()) {
            return false;
        }

        T entity = entityOpt.get();
        if (invokeBooleanDelete(entity) || invokeIntegerDelete(entity)) {
            memoryCache.put(id, entity);
            saveToFile();
            return true;
        }
        return deleteById(id);
    }

    private boolean invokeBooleanDelete(T entity) {
        try {
            Method method = entity.getClass().getMethod("setDeleted", Boolean.class);
            method.invoke(entity, true);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean invokeIntegerDelete(T entity) {
        try {
            Method method = entity.getClass().getMethod("setIsDeleted", Integer.class);
            method.invoke(entity, 1);
            return true;
        } catch (Exception ignored) {
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
        return memoryCache.values().stream().filter(condition).collect(Collectors.toList());
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
        List<T> saved = new ArrayList<>();
        for (T entity : entities) {
            T result = save(entity);
            if (result != null) {
                saved.add(result);
            }
        }
        return saved;
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
