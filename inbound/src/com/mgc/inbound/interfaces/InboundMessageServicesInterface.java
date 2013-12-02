/**
 * 
 */
package com.mgc.inbound.interfaces;

import com.mgc.inbound.pojo.GamingInfo;
import com.mgc.inbound.pojo.GamingRT;
import com.mgc.inbound.pojo.LoginInfo;
import com.mgc.inbound.pojo.LoginRT;

/**
 * @author Charles Chen
 *
 */
public interface InboundMessageServicesInterface {

	LoginRT doLogin(LoginInfo loginInfo);
	
	GamingRT doGaming(GamingInfo gamingInfo);
	
}
