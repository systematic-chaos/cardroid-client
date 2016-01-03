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
 * The session object. This is used to retrieve a CarDroid session
 * on behalf of the client. If the session is not refreshed on a
 * periodic basis, it will be automatically destroyed.
 * 
 **/
public abstract class _Glacier2SessionDisp extends Ice.ObjectImpl implements Glacier2Session
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Glacier2::Session",
        "::Ice::Object",
        "::cardroid::network::zerocice::Glacier2Session"
    };

    public boolean ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[] ice_ids()
    {
        return __ids;
    }

    public String[] ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String ice_id()
    {
        return __ids[2];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[2];
    }

    public static String ice_staticId()
    {
        return __ids[2];
    }

    /**
     * Destroy the session. This is called automatically when the
     * {@link Router} is destroyed.
     * 
     **/
    public final void destroy()
    {
        destroy(null);
    }

    /**
     * Get the CarDroid manager object.
     * 
     * @return A proxy for the new CarDroid manager.
     * 
     **/
    public final uclm.esi.cardroid.zerocice.CardroidManagerPrx getCardroidManager()
    {
        return getCardroidManager(null);
    }

    /**
     * Get the topic manager object used to subscribe to events via 
     * the IceStorm service.
     * 
     * @return A proxy to the topic manager used by the server 
     * to publish events.
     * 
     **/
    public final IceStorm.TopicPrx getTopic()
    {
        return getTopic(null);
    }

    /**
     * Refresh a session. If a session is not refreshed on a regular
     * basis by the client, it will be automatically destroyed.
     * 
     **/
    public final void refresh()
    {
        refresh(null);
    }

    public static Ice.DispatchStatus ___getCardroidManager(Glacier2Session __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.readEmptyParams();
        uclm.esi.cardroid.zerocice.CardroidManagerPrx __ret = __obj.getCardroidManager(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        uclm.esi.cardroid.zerocice.CardroidManagerPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTopic(Glacier2Session __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.readEmptyParams();
        IceStorm.TopicPrx __ret = __obj.getTopic(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        IceStorm.TopicPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___refresh(Glacier2Session __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        __obj.refresh(__current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "destroy",
        "getCardroidManager",
        "getTopic",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "refresh"
    };

    public Ice.DispatchStatus __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return Glacier2._SessionDisp.___destroy(this, in, __current);
            }
            case 1:
            {
                return ___getCardroidManager(this, in, __current);
            }
            case 2:
            {
                return ___getTopic(this, in, __current);
            }
            case 3:
            {
                return ___ice_id(this, in, __current);
            }
            case 4:
            {
                return ___ice_ids(this, in, __current);
            }
            case 5:
            {
                return ___ice_isA(this, in, __current);
            }
            case 6:
            {
                return ___ice_ping(this, in, __current);
            }
            case 7:
            {
                return ___refresh(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        __is.endReadSlice();
    }

    public static final long serialVersionUID = 0L;
}
