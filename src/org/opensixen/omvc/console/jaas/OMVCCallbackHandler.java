package org.opensixen.omvc.console.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.opensixen.omvc.console.ConfigUtil;


public class OMVCCallbackHandler implements CallbackHandler {

	/* (non-Javadoc)
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(Callback[] callbacks) throws IOException,	UnsupportedCallbackException {
		
		// Leemos la configuracion
		ConfigUtil config = ConfigUtil.getConfig();
		
		((NameCallback) callbacks[0]).setName(config.getUser());
		((PasswordCallback) callbacks[1]).setPassword(config.getPassword().toCharArray());
	}


}
