package com.abuqool.sqool.controller;

import com.abuqool.sqool.service.mgmt.MgmtProductService;
import com.abuqool.sqool.service.mgmt.impl.MgmtProductServiceImpl;
import com.alipay.api.msg.Message;
import com.google.inject.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alipay")
public class AlipayController {

    @Inject
    private MgmtProductService mgmtProductServiceImpl;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
            }
        });
        Message message = new Message();
        MgmtProductServiceImpl instance = injector.getInstance(MgmtProductServiceImpl.class);
        Integer integer = instance.disableProduct(1);

    }
}
