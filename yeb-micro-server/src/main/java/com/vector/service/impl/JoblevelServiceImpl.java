package com.vector.service.impl;

import com.vector.pojo.Joblevel;
import com.vector.mapper.JoblevelMapper;
import com.vector.service.IJoblevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.vo.RespVO;
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
public class JoblevelServiceImpl extends ServiceImpl<JoblevelMapper, Joblevel> implements IJoblevelService {

    @Override
    public RespVO getAllJoblevels() {
        return RespVO.ok().put(this.list());
    }
}
