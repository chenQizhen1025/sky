package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import org.springframework.stereotype.Service;


public interface SetmealService {
    public void saveWithDish(SetmealDTO setmealDTO);
}
