package com.vector.controller;


import com.vector.pojo.Position;
import com.vector.service.IPositionService;
import com.vector.vo.RespVO;
import javafx.geometry.Pos;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Resource
    private IPositionService positionService;
    /**
     * 获取所有职位信息
     * @return
     */
    @GetMapping("/")
    public RespVO getAllPosition() {
        List<Position> list = positionService.list();
        return RespVO.ok().put(list);
    }

    /**
     * 添加职位信息
     * @param position
     * @return
     */
    @PostMapping("/")
    public RespVO addPosition(@RequestBody Position position){
        return positionService.addPosition(position);
    }

    /**
     * 更新职位信息
     */
    @PutMapping("/")
    public RespVO updatePosition(@RequestBody Position position){
        return positionService.updatePosition(position);
    }

    /**
     * 删除职位信息
     */
    @DeleteMapping("/{id}")
    public RespVO deletePosition(@PathVariable Integer id){
        return positionService.deletePosition(id);
    }

    /**
     * 批量删除职位信息
     */
    @DeleteMapping("/")
    public RespVO deletePositionBatchByIds(Integer[] ids){
        return positionService.deletePositionBatchByIds(ids);
    }
}
