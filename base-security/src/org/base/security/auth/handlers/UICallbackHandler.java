/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.base.security.auth.handlers;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.JDialog;

public class UICallbackHandler
  implements javax.security.auth.callback.CallbackHandler
{
    private String username = null;
    private char[] password = null;
    
    AbstractAuthDialog authDialog;
    
    public UICallbackHandler(AbstractAuthDialog authDialog) {
        this.authDialog = authDialog;
    }

    public void handle(Callback[] callbacks)
	throws IOException, UnsupportedCallbackException
    {
        prompt();

        for ( int i = 0; i < callbacks.length; i++ )
	{
	    Callback callback = callbacks[i];

	    if ( callback instanceof NameCallback )
	    {
	        NameCallback ncb = (NameCallback) callback;
		ncb.setName( username );
	    }
	    else if ( callback instanceof PasswordCallback )
	    {
	        PasswordCallback pcb = (PasswordCallback) callback;
                pcb.setPassword( password );
	    }
	    else
	    {
	        System.out.println(
			"Unsupported callback: " +
			callback.getClass().getName() );
	        throw new UnsupportedCallbackException( callback );
	    }
	}
    }

    /**
     * Prompts the user for username and password with the
     * AuthenticationDialog dialog. This method may only be
     * called once per instance.
     */
    private void prompt()
    {
	/*AuthenticationDialog dlg =
            new AuthenticationDialog(null, true);*/
        authDialog.setTitle(" Autenticar Usuario ");
        authDialog.setLocation(300, 220);
        authDialog.pack();
        authDialog.setResizable(false);        
        authDialog.show();        
	username = authDialog.getUserName();
        password = authDialog.getPassword();
    }
}
