package java.DAO;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface GenericDAO<T, ID extends Serializable> {

    // 保存实体
    T save(T entity);

    // 更新实体
    T update(T entity);

    // 根据ID删除
    boolean deleteById(ID id);

    // 逻辑删除
    boolean softDelete(ID id);

    // 根据ID查找
    Optional<T> findById(ID id);

    // 查找所有
    List<T> findAll();

    // 根据条件查找
    List<T> findByCondition(Predicate<T> condition);

    // 是否存在
    boolean existsById(ID id);

    // 统计数量
    long count();

    // 批量保存
    List<T> saveAll(List<T> entities);

    // 重新加载数据
    void reload();

    // 清空数据
    void clearAll();
}