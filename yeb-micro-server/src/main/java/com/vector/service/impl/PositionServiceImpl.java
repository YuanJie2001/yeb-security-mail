package com.vector.service.impl;

import com.vector.pojo.Position;
import com.vector.mapper.PositionMapper;
import com.vector.service.IPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.vo.RespVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

    @Override
    public RespVO addPosition(Position position) {
        position.setCreateDate(LocalDateTime.now());
        if(this.save(position)){
            return RespVO.ok("添加成功!");
        }
        return RespVO.error("添加失败!");
    }

    @Override
    public RespVO updatePosition(Position position) {
        if(this.updateById(position)){
            return RespVO.ok("更新成功!");
        }
        return RespVO.error("更新失败!");
    }

    @Override
    public RespVO deletePosition(Integer id) {
        if(this.removeById(id)){
            return RespVO.ok("删除成功");
        }
        return RespVO.error("删除失败!");
    }

    @Override
    public RespVO deletePositionBatchByIds(Integer[] ids) {
        if(this.removeByIds(Arrays.asList(ids))){
            return RespVO.ok("批量删除成功!");
        }
        return RespVO.error("批量删除失败!");
    }
}
