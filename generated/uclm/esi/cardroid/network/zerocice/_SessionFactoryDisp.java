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
public abstract class _SessionFactoryDisp extends Ice.ObjectImpl implements SessionFactory
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::cardroid::network::zerocice::SessionFactory"
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
        return __ids[1];
    }

    public String ice_id(Ice.Current __current)
    {
        return __ids[1];
    }

    public static String ice_staticId()
    {
        return __ids[1];
    }

    /**
     * Create a session.
     * 
     * @return A proxy to the session.
     * 
     **/
    public final SessionPrx create()
    {
        return create(null);
    }

    /**
     * Get the value of the session timeout. Sessions are destroyed
     * if they see no activity for this period of time.
     * 
     * @return The timeout (in seconds).
     * 
     **/
    public final long getSessionTimeout()
    {
        return getSessionTimeout(null);
    }

    public static Ice.DispatchStatus ___create(SessionFactory __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.readEmptyParams();
        SessionPrx __ret = __obj.create(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        SessionPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getSessionTimeout(SessionFactory __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        long __ret = __obj.getSessionTimeout(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeLong(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "create",
        "getSessionTimeout",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
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
                return ___create(this, in, __current);
            }
            case 1:
            {
                return ___getSessionTimeout(this, in, __current);
            }
            case 2:
            {
                return ___ice_id(this, in, __current);
            }
            case 3:
            {
                return ___ice_ids(this, in, __current);
            }
            case 4:
            {
                return ___ice_isA(this, in, __current);
            }
            case 5:
            {
                return ___ice_ping(this, in, __current);
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
