package com.myee.tarot.djinn.service.impl;

import com.myee.djinn.dto.CommonResult;
import com.myee.djinn.dto.WaitToken;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.MealsService;
import com.myee.tarot.core.service.TransactionalAspectAware;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class MealsServiceImpl implements MealsService, TransactionalAspectAware {
    @Override
    public CommonResult takeNumber(WaitToken waitToken) throws RemoteException {
        return null;
    }

    @Override
    public boolean skipNumber(WaitToken waitToken) throws RemoteException {
        return false;
    }

	@Override
	public boolean cancelNumber(WaitToken waitToken, String reason) throws RemoteException {
		return false;
	}

	@Override
	public boolean doRepast(WaitToken waitToken, long tableId) throws RemoteException {
		return false;
	}

    @Override
    public List<WaitToken> listOfTableType(long l) throws RemoteException {
        return null;
    }
}
