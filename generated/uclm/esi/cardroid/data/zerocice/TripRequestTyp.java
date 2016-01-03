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

package uclm.esi.cardroid.data.zerocice;

public abstract class TripRequestTyp extends TripTyp
                                     implements _TripRequestTypOperations,
                                                _TripRequestTypOperationsNC
{
    public TripRequestTyp()
    {
        super();
    }

    public TripRequestTyp(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tDate, int nSeats, int tripType, UserTypPrx requester, DateTimePrefsTyp tDateTimePrefs)
    {
        super(tripId, fromPlace, toPlace, tDate, nSeats, tripType);
        this.requester = requester;
        this.tDateTimePrefs = tDateTimePrefs;
    }

    public TripRequestTyp(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tDate, int nSeats, DateTyp tReturnDate, String[] tWeekDays, Periodicity tPeriodicity, int tripDistance, String tripCharacteristics, int tripType, UserTypPrx requester, DateTimePrefsTyp tDateTimePrefs, DateTimePrefsTyp tReturnDateTimePrefs)
    {
        super(tripId, fromPlace, toPlace, tDate, nSeats, tReturnDate, tWeekDays, tPeriodicity, tripDistance, tripCharacteristics, tripType);
        this.requester = requester;
        this.tDateTimePrefs = tDateTimePrefs;
        setTReturnDateTimePrefs(tReturnDateTimePrefs);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::cardroid::data::zerocice::TripRequestTyp",
        "::cardroid::data::zerocice::TripTyp"
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

    public final DateTimePrefsTyp getTripDateTimePrefs()
    {
        return getTripDateTimePrefs(null);
    }

    public final UserTypPrx getTripRequester()
    {
        return getTripRequester(null);
    }

    public final DateTimePrefsTyp getTripReturnDateTimePrefs()
    {
        return getTripReturnDateTimePrefs(null);
    }

    public final void setTripDateTimePrefs(DateTimePrefsTyp dtp)
    {
        setTripDateTimePrefs(dtp, null);
    }

    public final void setTripRequester(UserTypPrx requester)
    {
        setTripRequester(requester, null);
    }

    public final void setTripReturnDateTimePrefs(DateTimePrefsTyp rdtp)
    {
        setTripReturnDateTimePrefs(rdtp, null);
    }

    public static Ice.DispatchStatus ___getTripDateTimePrefs(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        DateTimePrefsTyp __ret = __obj.getTripDateTimePrefs(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripDateTimePrefs(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        DateTimePrefsTypHolder dtp = new DateTimePrefsTypHolder();
        __is.readObject(dtp);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripDateTimePrefs(dtp.value, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripReturnDateTimePrefs(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        DateTimePrefsTyp __ret = __obj.getTripReturnDateTimePrefs(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripReturnDateTimePrefs(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        DateTimePrefsTypHolder rdtp = new DateTimePrefsTypHolder();
        __is.readObject(rdtp);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripReturnDateTimePrefs(rdtp.value, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripRequester(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        UserTypPrx __ret = __obj.getTripRequester(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        UserTypPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripRequester(TripRequestTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        UserTypPrx requester;
        requester = UserTypPrxHelper.__read(__is);
        __inS.endReadParams();
        __obj.setTripRequester(requester, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getCharacteristics",
        "getDistance",
        "getNSeats",
        "getPlace1",
        "getPlace2",
        "getTripDate",
        "getTripDateTimePrefs",
        "getTripId",
        "getTripPeriodicity",
        "getTripRequester",
        "getTripReturnDate",
        "getTripReturnDateTimePrefs",
        "getTripType",
        "getTripWeekDays",
        "hasCharacteristics",
        "hasDistance",
        "hasTripReturnDate",
        "hasWeekDaysPeriodicity",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "setCharacteristics",
        "setDistance",
        "setNSeats",
        "setPlace1",
        "setPlace2",
        "setTripDate",
        "setTripDateTimePrefs",
        "setTripId",
        "setTripRequester",
        "setTripReturnDate",
        "setTripReturnDateTimePrefs",
        "setTripType",
        "setTripWeekDaysPeriodicity",
        "toString"
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
                return TripTyp.___getCharacteristics(this, in, __current);
            }
            case 1:
            {
                return TripTyp.___getDistance(this, in, __current);
            }
            case 2:
            {
                return TripTyp.___getNSeats(this, in, __current);
            }
            case 3:
            {
                return TripTyp.___getPlace1(this, in, __current);
            }
            case 4:
            {
                return TripTyp.___getPlace2(this, in, __current);
            }
            case 5:
            {
                return TripTyp.___getTripDate(this, in, __current);
            }
            case 6:
            {
                return ___getTripDateTimePrefs(this, in, __current);
            }
            case 7:
            {
                return TripTyp.___getTripId(this, in, __current);
            }
            case 8:
            {
                return TripTyp.___getTripPeriodicity(this, in, __current);
            }
            case 9:
            {
                return ___getTripRequester(this, in, __current);
            }
            case 10:
            {
                return TripTyp.___getTripReturnDate(this, in, __current);
            }
            case 11:
            {
                return ___getTripReturnDateTimePrefs(this, in, __current);
            }
            case 12:
            {
                return TripTyp.___getTripType(this, in, __current);
            }
            case 13:
            {
                return TripTyp.___getTripWeekDays(this, in, __current);
            }
            case 14:
            {
                return TripTyp.___hasCharacteristics(this, in, __current);
            }
            case 15:
            {
                return TripTyp.___hasDistance(this, in, __current);
            }
            case 16:
            {
                return TripTyp.___hasTripReturnDate(this, in, __current);
            }
            case 17:
            {
                return TripTyp.___hasWeekDaysPeriodicity(this, in, __current);
            }
            case 18:
            {
                return ___ice_id(this, in, __current);
            }
            case 19:
            {
                return ___ice_ids(this, in, __current);
            }
            case 20:
            {
                return ___ice_isA(this, in, __current);
            }
            case 21:
            {
                return ___ice_ping(this, in, __current);
            }
            case 22:
            {
                return TripTyp.___setCharacteristics(this, in, __current);
            }
            case 23:
            {
                return TripTyp.___setDistance(this, in, __current);
            }
            case 24:
            {
                return TripTyp.___setNSeats(this, in, __current);
            }
            case 25:
            {
                return TripTyp.___setPlace1(this, in, __current);
            }
            case 26:
            {
                return TripTyp.___setPlace2(this, in, __current);
            }
            case 27:
            {
                return TripTyp.___setTripDate(this, in, __current);
            }
            case 28:
            {
                return ___setTripDateTimePrefs(this, in, __current);
            }
            case 29:
            {
                return TripTyp.___setTripId(this, in, __current);
            }
            case 30:
            {
                return ___setTripRequester(this, in, __current);
            }
            case 31:
            {
                return TripTyp.___setTripReturnDate(this, in, __current);
            }
            case 32:
            {
                return ___setTripReturnDateTimePrefs(this, in, __current);
            }
            case 33:
            {
                return TripTyp.___setTripType(this, in, __current);
            }
            case 34:
            {
                return TripTyp.___setTripWeekDaysPeriodicity(this, in, __current);
            }
            case 35:
            {
                return TripTyp.___toString(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, false);
        UserTypPrxHelper.__write(__os, requester);
        __os.writeObject(tDateTimePrefs);
        if(__has_tReturnDateTimePrefs && __os.writeOpt(11, Ice.OptionalFormat.Class))
        {
            __os.writeObject(tReturnDateTimePrefs);
        }
        __os.endWriteSlice();
        super.__writeImpl(__os);
    }

    private class Patcher implements IceInternal.Patcher
    {
        Patcher(int member)
        {
            __member = member;
        }

        public void
        patch(Ice.Object v)
        {
            switch(__member)
            {
            case 0:
                __typeId = "::cardroid::data::zerocice::PlaceTyp";
                if(v == null || v instanceof PlaceTyp)
                {
                    fromPlace = (PlaceTyp)v;
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 1:
                __typeId = "::cardroid::data::zerocice::PlaceTyp";
                if(v == null || v instanceof PlaceTyp)
                {
                    toPlace = (PlaceTyp)v;
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 2:
                __typeId = "::cardroid::data::zerocice::DateTyp";
                if(v == null || v instanceof DateTyp)
                {
                    tDate = (DateTyp)v;
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 3:
                __typeId = "::cardroid::data::zerocice::DateTyp";
                if(v == null || v instanceof DateTyp)
                {
                    setTReturnDate((DateTyp)v);
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 4:
                __typeId = "::cardroid::data::zerocice::DateTimePrefsTyp";
                if(v == null || v instanceof DateTimePrefsTyp)
                {
                    tDateTimePrefs = (DateTimePrefsTyp)v;
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 5:
                __typeId = "::cardroid::data::zerocice::DateTimePrefsTyp";
                if(v == null || v instanceof DateTimePrefsTyp)
                {
                    setTReturnDateTimePrefs((DateTimePrefsTyp)v);
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            }
        }

        public String
        type()
        {
            return __typeId;
        }

        private int __member;
        private String __typeId;
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        requester = UserTypPrxHelper.__read(__is);
        __is.readObject(new Patcher(4));
        if(__has_tReturnDateTimePrefs = __is.readOpt(11, Ice.OptionalFormat.Class))
        {
            __is.readObject(new Patcher(5));
        }
        __is.endReadSlice();
        super.__readImpl(__is);
    }

    public UserTypPrx requester;

    public DateTimePrefsTyp tDateTimePrefs;

    private DateTimePrefsTyp tReturnDateTimePrefs;
    private boolean __has_tReturnDateTimePrefs;

    public DateTimePrefsTyp
    getTReturnDateTimePrefs()
    {
        if(!__has_tReturnDateTimePrefs)
        {
            throw new java.lang.IllegalStateException("tReturnDateTimePrefs is not set");
        }
        return tReturnDateTimePrefs;
    }

    public void
    setTReturnDateTimePrefs(DateTimePrefsTyp _tReturnDateTimePrefs)
    {
        __has_tReturnDateTimePrefs = true;
        tReturnDateTimePrefs = _tReturnDateTimePrefs;
    }

    public boolean
    hasTReturnDateTimePrefs()
    {
        return __has_tReturnDateTimePrefs;
    }

    public void
    clearTReturnDateTimePrefs()
    {
        __has_tReturnDateTimePrefs = false;
    }

    public void
    optionalTReturnDateTimePrefs(Ice.Optional<DateTimePrefsTyp> __v)
    {
        if(__v == null || !__v.isSet())
        {
            __has_tReturnDateTimePrefs = false;
        }
        else
        {
            __has_tReturnDateTimePrefs = true;
            tReturnDateTimePrefs = __v.get();
        }
    }

    public Ice.Optional<DateTimePrefsTyp>
    optionalTReturnDateTimePrefs()
    {
        if(__has_tReturnDateTimePrefs)
        {
            return new Ice.Optional<DateTimePrefsTyp>(tReturnDateTimePrefs);
        }
        else
        {
            return new Ice.Optional<DateTimePrefsTyp>();
        }
    }

    public static final long serialVersionUID = -86011253L;
}
