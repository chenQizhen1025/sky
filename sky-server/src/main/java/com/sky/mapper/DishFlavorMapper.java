package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param dishFlavorList
     */
    void insertBatch(List<DishFlavor> dishFlavorList);



    /**
     * 根据菜品id删除相关口味
     * @param dishIds
     */
//    @Delete("delete from sky_take_out.dish_flavor where dish_id = #{dishId}")
//    void deleteByDishId(Long dishId);
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询关联口味
     * @param dishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
