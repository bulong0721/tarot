package com.myee.tarot.djinn.service.impl;

import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.VersionInfo;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.CommonService;
import com.myee.tarot.core.service.TransactionalAspectAware;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class CommonServiceImpl implements CommonService, TransactionalAspectAware {
	@Override
	public Boolean isConnection() throws RemoteException {
		return true;
	}

	@Override
	public VersionInfo latestVersion(String jsonArgs) throws RemoteException {
		return null;
	}

	@Override
    public VersionInfo latestVersion() throws RemoteException {
        return null;
    }

    @Override
    public VersionInfo latestVersion(long l) throws RemoteException {
        return null;
    }

    @Override
    public ShopDetail ownerShop() throws RemoteException {
        return null;
    }

    @Override
    public ShopDetail ownerShop(String s) throws RemoteException {
        return null;
    }
}
