package com.subhadeep.e_food_authentication_service.constant;

import java.util.HashMap;
import java.util.Map;

import com.subhadeep.e_food_authentication_service.model.RoleEO;

public class RoleConstants {

    public static final Integer USER_ROLE_ID = 100;
    public static final Integer SHOP_OWNER_ID = 200;
    public static final Integer DELIVERY_PARTNER_ID = 300;
    public static final Integer ADMIN_ID = 400;

    public static final String USER_ROLE_NAME = "USER";
    public static final String SHOP_OWNER_NAME = "SHOP_OWNER";
    public static final String DELIVERY_PARTNER_NAME = "DELIVERY_PARTNER";
    public static final String ADMIN_NAME = "ADMIN";

    public static final Map<Integer, RoleEO> idRoleMap = new HashMap<>();
    static {
        idRoleMap.put(USER_ROLE_ID,

                RoleEO.builder()
                        .id(USER_ROLE_ID)
                        .roleName(USER_ROLE_NAME)
                        .build()

        );

        idRoleMap.put(SHOP_OWNER_ID,
                RoleEO.builder()
                        .id(SHOP_OWNER_ID)
                        .roleName(SHOP_OWNER_NAME)
                        .build()

        );


        idRoleMap.put(ADMIN_ID,
                RoleEO.builder()
                        .id(ADMIN_ID)
                        .roleName(ADMIN_NAME)
                        .build()

        );



        idRoleMap.put(DELIVERY_PARTNER_ID,
                RoleEO.builder()
                        .id(DELIVERY_PARTNER_ID)
                        .roleName(DELIVERY_PARTNER_NAME)
                        .build()

        );

    }

}
