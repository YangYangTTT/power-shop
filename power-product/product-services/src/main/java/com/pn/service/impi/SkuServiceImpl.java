package com.pn.service.impi;

import com.pn.service.SkuService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.SkuMapper;
import com.pn.entily.Sku;

@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

}
