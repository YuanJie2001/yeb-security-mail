package com.vector.service;

import com.vector.pojo.Position;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.vo.RespVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
public interface IPositionService extends IService<Position> {

    RespVO addPosition(Position position);

    RespVO updatePosition(Position position);

    RespVO deletePosition(Integer id);

    RespVO deletePositionBatchByIds(Integer[] ids);
}
