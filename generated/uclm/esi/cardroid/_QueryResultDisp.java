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
 * Interface to get query results.
 * 
 **/
public abstract class _QueryResultDisp extends Ice.ObjectImpl implements QueryResult
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
        "::cardroid::QueryResult"
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
     * Destroy the query result.
     * 
     **/
    public final void destroy()
    {
        destroy(null);
    }

    /**
     * Get more query results.
     * 
     * @param n The maximum number of results to return.
     * 
     * @param destroyed There are no more results, and the query has
     * been destroyed.
     * 
     * @returns A sequence of up to n results.
     * 
     **/
    public final java.util.List<Ice.ObjectPrx> next(int n, Ice.BooleanHolder destroyed)
    {
        return next(n, destroyed, null);
    }

    public static Ice.DispatchStatus ___next(QueryResult __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int n;
        n = __is.readInt();
        __inS.endReadParams();
        Ice.BooleanHolder destroyed = new Ice.BooleanHolder();
        java.util.List<Ice.ObjectPrx> __ret = __obj.next(n, destroyed, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(destroyed.value);
        ResultSeqHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___destroy(QueryResult __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.readEmptyParams();
        __obj.destroy(__current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "destroy",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "next"
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
                return ___destroy(this, in, __current);
            }
            case 1:
            {
                return ___ice_id(this, in, __current);
            }
            case 2:
            {
                return ___ice_ids(this, in, __current);
            }
            case 3:
            {
                return ___ice_isA(this, in, __current);
            }
            case 4:
            {
                return ___ice_ping(this, in, __current);
            }
            case 5:
            {
                return ___next(this, in, __current);
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
