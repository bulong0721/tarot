package com.myee.tarot.djinn.service.impl;

import com.myee.djinn.dto.DataUploadInfoDTO;
import com.myee.djinn.dto.UploadResourceType;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.DataStoreService;
import com.myee.tarot.core.service.TransactionalAspectAware;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class DataStoreServiceImpl implements DataStoreService, TransactionalAspectAware {

	@Override
	public int receiveLog(long orgId, UploadResourceType fileType, String logText) throws RemoteException {
		return 0;
	}

	@Override
    public int receiveText(long l, String s) throws RemoteException {
        return 0;
    }

	@Override
	public boolean uploadData(DataUploadInfoDTO dataUploadInfoDTO) throws RemoteException {
		return false;
	}
}
