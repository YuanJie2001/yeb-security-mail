package com.vector.service.impl;

import com.vector.pojo.Oplog;
import com.vector.mapper.OplogMapper;
import com.vector.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuanJie
 * @since 2022-09-07
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
