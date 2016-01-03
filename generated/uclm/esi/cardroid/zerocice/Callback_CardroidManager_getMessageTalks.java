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

package uclm.esi.cardroid.zerocice;

public abstract class Callback_CardroidManager_getMessageTalks extends Ice.TwowayCallback
{
    public abstract void response(java.util.List<Ice.ObjectPrx> first, int nrows, uclm.esi.cardroid.QueryResultPrx result);

    public final void __completed(Ice.AsyncResult __result)
    {
        CardroidManagerPrx __proxy = (CardroidManagerPrx)__result.getProxy();
        uclm.esi.cardroid.ResultSeqHolder first = new uclm.esi.cardroid.ResultSeqHolder();
        Ice.IntHolder nrows = new Ice.IntHolder();
        uclm.esi.cardroid.QueryResultPrxHolder result = new uclm.esi.cardroid.QueryResultPrxHolder();
        try
        {
            __proxy.end_getMessageTalks(first, nrows, result, __result);
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(first.value, nrows.value, result.value);
    }
}
