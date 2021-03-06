// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.1
//
// <auto-generated>
//
// Generated from file `Session.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package uclm.esi.cardroid.network.zerocice;

/**
 * Get the value of the session timeout. Sessions are destroyed
 * if they see no activity for this period of time.
 * 
 **/

public abstract class Callback_SessionFactory_getSessionTimeout extends Ice.TwowayCallback
{
    public abstract void response(long __ret);

    public final void __completed(Ice.AsyncResult __result)
    {
        SessionFactoryPrx __proxy = (SessionFactoryPrx)__result.getProxy();
        long __ret = 0;
        try
        {
            __ret = __proxy.end_getSessionTimeout(__result);
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
