package com.pn.service.Impi;

import com.pn.service.OrderRefundService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.dao.OrderRefundMapper;
import com.pn.entily.OrderRefund;

@Service
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {

}
