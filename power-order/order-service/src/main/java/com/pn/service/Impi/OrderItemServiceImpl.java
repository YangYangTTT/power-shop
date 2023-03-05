package com.pn.service.Impi;

import com.pn.service.OrderItemService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.OrderItem;
import com.pn.dao.OrderItemMapper;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
