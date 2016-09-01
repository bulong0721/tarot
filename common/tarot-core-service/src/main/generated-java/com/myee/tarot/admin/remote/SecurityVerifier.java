package com.myee.tarot.admin.remote;

import com.myee.tarot.admin.domain.AdminUser;

/**
 * Created by Martin on 2016/4/18.
 */
public interface SecurityVerifier {

    AdminUser getPersistentAdminUser();
}
