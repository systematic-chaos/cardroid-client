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
// Generated from file `Cardroid.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package uclm.esi.cardroid;

/**
 * Get more query results.
 * 
 **/

public abstract class Callback_QueryResult_next extends Ice.TwowayCallback
{
    public abstract void response(java.util.List<Ice.ObjectPrx> __ret, boolean destroyed);

    public final void __completed(Ice.AsyncResult __result)
    {
        QueryResultPrx __proxy = (QueryResultPrx)__result.getProxy();
        java.util.List<Ice.ObjectPrx> __ret = null;
        Ice.BooleanHolder destroyed = new Ice.BooleanHolder();
        try
        {
            __ret = __proxy.end_next(destroyed, __result);
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret, destroyed.value);
    }
}
