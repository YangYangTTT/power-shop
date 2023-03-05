package com.pn.service.impi;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pn.entily.Transport;
import com.pn.dao.TransportMapper;
import com.pn.service.TransportService;
@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements TransportService{

}
