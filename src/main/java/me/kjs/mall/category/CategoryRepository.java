package me.kjs.mall.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentCategory(Category category);

    List<Category> findAllByName(String name);
}
