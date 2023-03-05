package com.pn.service.Impi;

import com.pn.service.OrderSettlementService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.OrderSettlementMapper;
import com.pn.entily.OrderSettlement;

@Service
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService {

}
