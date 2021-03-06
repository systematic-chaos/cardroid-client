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
 * Interface to create new sessions.
 * 
 **/
public interface SessionFactoryPrx extends Ice.ObjectPrx
{
    /**
     * Create a session.
     * 
     * @return A proxy to the session.
     * 
     **/
    public SessionPrx create();

    /**
     * Create a session.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @return A proxy to the session.
     * 
     **/
    public SessionPrx create(java.util.Map<String, String> __ctx);

    /**
     * Create a session.
     * 
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create();

    /**
     * Create a session.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create(java.util.Map<String, String> __ctx);

    /**
     * Create a session.
     * 
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create(Ice.Callback __cb);

    /**
     * Create a session.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * Create a session.
     * 
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create(Callback_SessionFactory_create __cb);

    /**
     * Create a session.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_create(java.util.Map<String, String> __ctx, Callback_SessionFactory_create __cb);

    /**
     * Create a session.
     * 
     * @param __result The asynchronous result object.
     * @return A proxy to the session.
     * 
     **/
    public SessionPrx end_create(Ice.AsyncResult __result);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @return The timeout (in seconds).
     * 
     **/
    public long getSessionTimeout();

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @return The timeout (in seconds).
     * 
     **/
    public long getSessionTimeout(java.util.Map<String, String> __ctx);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout();

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout(java.util.Map<String, String> __ctx);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout(Ice.Callback __cb);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout(Callback_SessionFactory_getSessionTimeout __cb);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __ctx The Context map to send with the invocation.
     * @param __cb The asynchronous callback object.
     * @return The asynchronous result object.
     **/
    public Ice.AsyncResult begin_getSessionTimeout(java.util.Map<String, String> __ctx, Callback_SessionFactory_getSessionTimeout __cb);

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @param __result The asynchronous result object.
     * @return The timeout (in seconds).
     * 
     **/
    public long end_getSessionTimeout(Ice.AsyncResult __result);
}
