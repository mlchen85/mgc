package com.mgc.inbound;

import java.util.UUID;

import com.mgc.inbound.interfaces.DoGaming;
import com.mgc.inbound.interfaces.DoGamingResponse;
import com.mgc.inbound.interfaces.DoLogin;
import com.mgc.inbound.interfaces.DoLoginResponse;
import com.mgc.inbound.interfaces.InboundMessageServicesSkeleton;
import com.mgc.inbound.pojo.xsd.ErrorInfo;
import com.mgc.inbound.pojo.xsd.LoginRT;

public class InboundMessageServices extends InboundMessageServicesSkeleton {

	@Override
	public DoLoginResponse doLogin(DoLogin doLogin) {
		DoLoginResponse res = new DoLoginResponse();
		LoginRT loginRT = new LoginRT();
		String userName = doLogin.getLoginInfo().getUserName();
		String correctPswd = "1234";
		System.out.println("username: " + userName);
		System.out.println("password: " + doLogin.getLoginInfo().getPassword());
		if (!doLogin.getLoginInfo().getPassword().equals(correctPswd)) {
			loginRT.setStatusId(new Integer(2));

			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setErrorId(new Integer(101));
			errorInfo.setErrorMessage("Invalid Password!");
			loginRT.setErrorInfo(errorInfo);
		} else {
			String sessionId = generateUUID();
			loginRT.setSessionId(sessionId);
			loginRT.setStatusId(new Integer(1));
		}
		res.setLoginRT(loginRT);
		return res;
	}

	@Override
	public DoGamingResponse doGaming(DoGaming doGaming) {
		// TODO Auto-generated method stub
		return super.doGaming(doGaming);
	}

	private String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

}
