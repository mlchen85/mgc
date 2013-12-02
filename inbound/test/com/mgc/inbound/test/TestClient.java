package com.mgc.inbound.test;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.mgc.inbound.interfaces.DoLogin;
import com.mgc.inbound.interfaces.DoLoginResponse;
import com.mgc.inbound.interfaces.InboundMessageServicesStub;
import com.mgc.inbound.pojo.xsd.LoginInfo;
import com.mgc.inbound.pojo.xsd.LoginRT;

public class TestClient {

	static InboundMessageServicesStub stub;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			DoLogin req = new DoLogin();
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setUserName("abcd");
			loginInfo.setPassword("1234");

			req.setLoginInfo(loginInfo);

			stub = new InboundMessageServicesStub("http://localhost:8080/MGC/services/InboundMessageServices");

			DoLoginResponse res = stub.doLogin(req);

			LoginRT loginRT = res.getLoginRT();
			if (loginRT.getErrorInfo() == null) {
				System.out.println("status: " + loginRT.getStatusId());
				System.out.println("session id: " + loginRT.getSessionId());
			} else {
				System.out.println("status: " + loginRT.getStatusId());
				System.out.println("error id: " + loginRT.getErrorInfo().getErrorId());
				System.out.println("error message: " + loginRT.getErrorInfo().getErrorMessage());
			}
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
