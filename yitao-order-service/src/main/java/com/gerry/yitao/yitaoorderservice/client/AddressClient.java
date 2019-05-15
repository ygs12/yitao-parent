package com.gerry.yitao.yitaoorderservice.client;

import com.gerry.yitao.order.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:47
 * @Description:
 */
public abstract class AddressClient {
    public static final List<AddressDTO> addressList = new ArrayList<AddressDTO>(){
        {
            AddressDTO address = new AddressDTO();
            address.setId(1L);
            address.setAddress("武汉市洪山区凯乐桂圆 W号楼");
            address.setCity("武汉");
            address.setDistrict("洪山区");
            address.setName("gerry");
            address.setPhone("15855500000");
            address.setState("武汉");
            address.setZipCode("100010");
            address.setIsDefault(true);
            add(address);

            AddressDTO address2 = new AddressDTO();
            address2.setId(2L);
            address2.setAddress("朝阳路 A号楼");
            address2.setCity("北京");
            address2.setDistrict("朝阳区");
            address2.setName("张三");
            address2.setPhone("13600000000");
            address2.setState("北京");
            address2.setZipCode("100000");
            address2.setIsDefault(false);
            add(address2);
        }
    };

    public static AddressDTO findById(Long id){
        for (AddressDTO addressDTO : addressList) {
            if(addressDTO.getId() == id) return addressDTO;
        }
        return null;
    }
}