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

public abstract class TripOfferTyp extends TripTyp
                                   implements _TripOfferTypOperations,
                                              _TripOfferTypOperationsNC
{
    public TripOfferTyp()
    {
        super();
    }

    public TripOfferTyp(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tDate, int nSeats, int tripType, UserTypPrx driver, java.util.List<WaypointTyp> waypoints, CarTypPrx tripOfferCar, java.util.List<PassengerTyp> passengers, double price, String[] allowed, DateTimeTyp tDateTime)
    {
        super(tripId, fromPlace, toPlace, tDate, nSeats, tripType);
        this.driver = driver;
        this.waypoints = waypoints;
        this.tripOfferCar = tripOfferCar;
        this.passengers = passengers;
        this.price = price;
        this.allowed = allowed;
        this.tDateTime = tDateTime;
    }

    public TripOfferTyp(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tDate, int nSeats, DateTyp tReturnDate, String[] tWeekDays, Periodicity tPeriodicity, int tripDistance, String tripCharacteristics, int tripType, UserTypPrx driver, java.util.List<WaypointTyp> waypoints, CarTypPrx tripOfferCar, java.util.List<PassengerTyp> passengers, double price, String[] allowed, DateTimeTyp tDateTime, DateTimeTyp tReturnDateTime)
    {
        super(tripId, fromPlace, toPlace, tDate, nSeats, tReturnDate, tWeekDays, tPeriodicity, tripDistance, tripCharacteristics, tripType);
        this.driver = driver;
        this.waypoints = waypoints;
        this.tripOfferCar = tripOfferCar;
        this.passengers = passengers;
        this.price = price;
        this.allowed = allowed;
        this.tDateTime = tDateTime;
        setTReturnDateTime(tReturnDateTime);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::cardroid::data::zerocice::TripOfferTyp",
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

    public final boolean addTripPassenger(UserTypPrx passenger, int seats)
    {
        return addTripPassenger(passenger, seats, null);
    }

    public final boolean addTripWaypoint(PlaceTyp waypoint)
    {
        return addTripWaypoint(waypoint, null);
    }

    public final void clearTripPassengers()
    {
        clearTripPassengers(null);
    }

    public final void clearTripWaypoints()
    {
        clearTripWaypoints(null);
    }

    public final String[] getAllowedFeatures()
    {
        return getAllowedFeatures(null);
    }

    public final int getNTripPassengers()
    {
        return getNTripPassengers(null);
    }

    public final int getNTripWaypoints()
    {
        return getNTripWaypoints(null);
    }

    public final double getPrice()
    {
        return getPrice(null);
    }

    public final CarTypPrx getTripCar()
    {
        return getTripCar(null);
    }

    public final DateTimeTyp getTripDateTime()
    {
        return getTripDateTime(null);
    }

    public final UserTypPrx getTripDriver()
    {
        return getTripDriver(null);
    }

    public final java.util.List<PassengerTyp> getTripPassengers()
    {
        return getTripPassengers(null);
    }

    public final DateTimeTyp getTripReturnDateTime()
    {
        return getTripReturnDateTime(null);
    }

    public final java.util.List<WaypointTyp> getTripWaypoints()
    {
        return getTripWaypoints(null);
    }

    public final boolean removeTripPassenger(UserTypPrx passenger)
    {
        return removeTripPassenger(passenger, null);
    }

    public final boolean removeTripWaypoint(int pos)
    {
        return removeTripWaypoint(pos, null);
    }

    public final void setAllowedFeatures(String[] allowed)
    {
        setAllowedFeatures(allowed, null);
    }

    public final void setPrice(double price)
    {
        setPrice(price, null);
    }

    public final void setTripCar(CarTypPrx tripCar)
    {
        setTripCar(tripCar, null);
    }

    public final void setTripDateTime(DateTimeTyp dt)
    {
        setTripDateTime(dt, null);
    }

    public final void setTripDriver(UserTypPrx driver)
    {
        setTripDriver(driver, null);
    }

    public final void setTripPassengers(java.util.List<PassengerTyp> passengers)
    {
        setTripPassengers(passengers, null);
    }

    public final void setTripReturnDateTime(DateTimeTyp rdt)
    {
        setTripReturnDateTime(rdt, null);
    }

    public final void setTripWaypoints(java.util.List<WaypointTyp> waypoints)
    {
        setTripWaypoints(waypoints, null);
    }

    public static Ice.DispatchStatus ___getTripDateTime(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        DateTimeTyp __ret = __obj.getTripDateTime(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripDateTime(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        DateTimeTypHolder dt = new DateTimeTypHolder();
        __is.readObject(dt);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripDateTime(dt.value, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripReturnDateTime(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        DateTimeTyp __ret = __obj.getTripReturnDateTime(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripReturnDateTime(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        DateTimeTypHolder rdt = new DateTimeTypHolder();
        __is.readObject(rdt);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripReturnDateTime(rdt.value, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripDriver(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        UserTypPrx __ret = __obj.getTripDriver(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        UserTypPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripDriver(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        UserTypPrx driver;
        driver = UserTypPrxHelper.__read(__is);
        __inS.endReadParams();
        __obj.setTripDriver(driver, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripWaypoints(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        java.util.List<WaypointTyp> __ret = __obj.getTripWaypoints(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        WaypointCollectionHelper.write(__os, __ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripWaypoints(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        java.util.List<WaypointTyp> waypoints;
        waypoints = WaypointCollectionHelper.read(__is);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripWaypoints(waypoints, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripCar(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        CarTypPrx __ret = __obj.getTripCar(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        CarTypPrxHelper.__write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripCar(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        CarTypPrx tripCar;
        tripCar = CarTypPrxHelper.__read(__is);
        __inS.endReadParams();
        __obj.setTripCar(tripCar, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getTripPassengers(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        java.util.List<PassengerTyp> __ret = __obj.getTripPassengers(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        PassengerCollectionHelper.write(__os, __ret);
        __os.writePendingObjects();
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setTripPassengers(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        java.util.List<PassengerTyp> passengers;
        passengers = PassengerCollectionHelper.read(__is);
        __is.readPendingObjects();
        __inS.endReadParams();
        __obj.setTripPassengers(passengers, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getPrice(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        double __ret = __obj.getPrice(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeDouble(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setPrice(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        double price;
        price = __is.readDouble();
        __inS.endReadParams();
        __obj.setPrice(price, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getAllowedFeatures(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String[] __ret = __obj.getAllowedFeatures(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        AllowedVHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setAllowedFeatures(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String[] allowed;
        allowed = AllowedVHelper.read(__is);
        __inS.endReadParams();
        __obj.setAllowedFeatures(allowed, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___addTripWaypoint(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        PlaceTypHolder waypoint = new PlaceTypHolder();
        __is.readObject(waypoint);
        __is.readPendingObjects();
        __inS.endReadParams();
        boolean __ret = __obj.addTripWaypoint(waypoint.value, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___removeTripWaypoint(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int pos;
        pos = __is.readInt();
        __inS.endReadParams();
        boolean __ret = __obj.removeTripWaypoint(pos, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getNTripWaypoints(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        int __ret = __obj.getNTripWaypoints(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___clearTripWaypoints(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        __obj.clearTripWaypoints(__current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___addTripPassenger(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        UserTypPrx passenger;
        int seats;
        passenger = UserTypPrxHelper.__read(__is);
        seats = __is.readInt();
        __inS.endReadParams();
        boolean __ret = __obj.addTripPassenger(passenger, seats, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___removeTripPassenger(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        UserTypPrx passenger;
        passenger = UserTypPrxHelper.__read(__is);
        __inS.endReadParams();
        boolean __ret = __obj.removeTripPassenger(passenger, __current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getNTripPassengers(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        int __ret = __obj.getNTripPassengers(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___clearTripPassengers(TripOfferTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        __obj.clearTripPassengers(__current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "addTripPassenger",
        "addTripWaypoint",
        "clearTripPassengers",
        "clearTripWaypoints",
        "getAllowedFeatures",
        "getCharacteristics",
        "getDistance",
        "getNSeats",
        "getNTripPassengers",
        "getNTripWaypoints",
        "getPlace1",
        "getPlace2",
        "getPrice",
        "getTripCar",
        "getTripDate",
        "getTripDateTime",
        "getTripDriver",
        "getTripId",
        "getTripPassengers",
        "getTripPeriodicity",
        "getTripReturnDate",
        "getTripReturnDateTime",
        "getTripType",
        "getTripWaypoints",
        "getTripWeekDays",
        "hasCharacteristics",
        "hasDistance",
        "hasTripReturnDate",
        "hasWeekDaysPeriodicity",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "removeTripPassenger",
        "removeTripWaypoint",
        "setAllowedFeatures",
        "setCharacteristics",
        "setDistance",
        "setNSeats",
        "setPlace1",
        "setPlace2",
        "setPrice",
        "setTripCar",
        "setTripDate",
        "setTripDateTime",
        "setTripDriver",
        "setTripId",
        "setTripPassengers",
        "setTripReturnDate",
        "setTripReturnDateTime",
        "setTripType",
        "setTripWaypoints",
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
                return ___addTripPassenger(this, in, __current);
            }
            case 1:
            {
                return ___addTripWaypoint(this, in, __current);
            }
            case 2:
            {
                return ___clearTripPassengers(this, in, __current);
            }
            case 3:
            {
                return ___clearTripWaypoints(this, in, __current);
            }
            case 4:
            {
                return ___getAllowedFeatures(this, in, __current);
            }
            case 5:
            {
                return TripTyp.___getCharacteristics(this, in, __current);
            }
            case 6:
            {
                return TripTyp.___getDistance(this, in, __current);
            }
            case 7:
            {
                return TripTyp.___getNSeats(this, in, __current);
            }
            case 8:
            {
                return ___getNTripPassengers(this, in, __current);
            }
            case 9:
            {
                return ___getNTripWaypoints(this, in, __current);
            }
            case 10:
            {
                return TripTyp.___getPlace1(this, in, __current);
            }
            case 11:
            {
                return TripTyp.___getPlace2(this, in, __current);
            }
            case 12:
            {
                return ___getPrice(this, in, __current);
            }
            case 13:
            {
                return ___getTripCar(this, in, __current);
            }
            case 14:
            {
                return TripTyp.___getTripDate(this, in, __current);
            }
            case 15:
            {
                return ___getTripDateTime(this, in, __current);
            }
            case 16:
            {
                return ___getTripDriver(this, in, __current);
            }
            case 17:
            {
                return TripTyp.___getTripId(this, in, __current);
            }
            case 18:
            {
                return ___getTripPassengers(this, in, __current);
            }
            case 19:
            {
                return TripTyp.___getTripPeriodicity(this, in, __current);
            }
            case 20:
            {
                return TripTyp.___getTripReturnDate(this, in, __current);
            }
            case 21:
            {
                return ___getTripReturnDateTime(this, in, __current);
            }
            case 22:
            {
                return TripTyp.___getTripType(this, in, __current);
            }
            case 23:
            {
                return ___getTripWaypoints(this, in, __current);
            }
            case 24:
            {
                return TripTyp.___getTripWeekDays(this, in, __current);
            }
            case 25:
            {
                return TripTyp.___hasCharacteristics(this, in, __current);
            }
            case 26:
            {
                return TripTyp.___hasDistance(this, in, __current);
            }
            case 27:
            {
                return TripTyp.___hasTripReturnDate(this, in, __current);
            }
            case 28:
            {
                return TripTyp.___hasWeekDaysPeriodicity(this, in, __current);
            }
            case 29:
            {
                return ___ice_id(this, in, __current);
            }
            case 30:
            {
                return ___ice_ids(this, in, __current);
            }
            case 31:
            {
                return ___ice_isA(this, in, __current);
            }
            case 32:
            {
                return ___ice_ping(this, in, __current);
            }
            case 33:
            {
                return ___removeTripPassenger(this, in, __current);
            }
            case 34:
            {
                return ___removeTripWaypoint(this, in, __current);
            }
            case 35:
            {
                return ___setAllowedFeatures(this, in, __current);
            }
            case 36:
            {
                return TripTyp.___setCharacteristics(this, in, __current);
            }
            case 37:
            {
                return TripTyp.___setDistance(this, in, __current);
            }
            case 38:
            {
                return TripTyp.___setNSeats(this, in, __current);
            }
            case 39:
            {
                return TripTyp.___setPlace1(this, in, __current);
            }
            case 40:
            {
                return TripTyp.___setPlace2(this, in, __current);
            }
            case 41:
            {
                return ___setPrice(this, in, __current);
            }
            case 42:
            {
                return ___setTripCar(this, in, __current);
            }
            case 43:
            {
                return TripTyp.___setTripDate(this, in, __current);
            }
            case 44:
            {
                return ___setTripDateTime(this, in, __current);
            }
            case 45:
            {
                return ___setTripDriver(this, in, __current);
            }
            case 46:
            {
                return TripTyp.___setTripId(this, in, __current);
            }
            case 47:
            {
                return ___setTripPassengers(this, in, __current);
            }
            case 48:
            {
                return TripTyp.___setTripReturnDate(this, in, __current);
            }
            case 49:
            {
                return ___setTripReturnDateTime(this, in, __current);
            }
            case 50:
            {
                return TripTyp.___setTripType(this, in, __current);
            }
            case 51:
            {
                return ___setTripWaypoints(this, in, __current);
            }
            case 52:
            {
                return TripTyp.___setTripWeekDaysPeriodicity(this, in, __current);
            }
            case 53:
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
        UserTypPrxHelper.__write(__os, driver);
        WaypointCollectionHelper.write(__os, waypoints);
        CarTypPrxHelper.__write(__os, tripOfferCar);
        PassengerCollectionHelper.write(__os, passengers);
        __os.writeDouble(price);
        AllowedVHelper.write(__os, allowed);
        __os.writeObject(tDateTime);
        if(__has_tReturnDateTime && __os.writeOpt(11, Ice.OptionalFormat.Class))
        {
            __os.writeObject(tReturnDateTime);
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
                __typeId = "::cardroid::data::zerocice::DateTimeTyp";
                if(v == null || v instanceof DateTimeTyp)
                {
                    tDateTime = (DateTimeTyp)v;
                }
                else
                {
                    IceInternal.Ex.throwUOE(type(), v);
                }
                break;
            case 5:
                __typeId = "::cardroid::data::zerocice::DateTimeTyp";
                if(v == null || v instanceof DateTimeTyp)
                {
                    setTReturnDateTime((DateTimeTyp)v);
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
        driver = UserTypPrxHelper.__read(__is);
        waypoints = WaypointCollectionHelper.read(__is);
        tripOfferCar = CarTypPrxHelper.__read(__is);
        passengers = PassengerCollectionHelper.read(__is);
        price = __is.readDouble();
        allowed = AllowedVHelper.read(__is);
        __is.readObject(new Patcher(4));
        if(__has_tReturnDateTime = __is.readOpt(11, Ice.OptionalFormat.Class))
        {
            __is.readObject(new Patcher(5));
        }
        __is.endReadSlice();
        super.__readImpl(__is);
    }

    public UserTypPrx driver;

    public java.util.List<WaypointTyp> waypoints;

    public CarTypPrx tripOfferCar;

    public java.util.List<PassengerTyp> passengers;

    public double price;

    public String[] allowed;

    public DateTimeTyp tDateTime;

    private DateTimeTyp tReturnDateTime;
    private boolean __has_tReturnDateTime;

    public DateTimeTyp
    getTReturnDateTime()
    {
        if(!__has_tReturnDateTime)
        {
            throw new java.lang.IllegalStateException("tReturnDateTime is not set");
        }
        return tReturnDateTime;
    }

    public void
    setTReturnDateTime(DateTimeTyp _tReturnDateTime)
    {
        __has_tReturnDateTime = true;
        tReturnDateTime = _tReturnDateTime;
    }

    public boolean
    hasTReturnDateTime()
    {
        return __has_tReturnDateTime;
    }

    public void
    clearTReturnDateTime()
    {
        __has_tReturnDateTime = false;
    }

    public void
    optionalTReturnDateTime(Ice.Optional<DateTimeTyp> __v)
    {
        if(__v == null || !__v.isSet())
        {
            __has_tReturnDateTime = false;
        }
        else
        {
            __has_tReturnDateTime = true;
            tReturnDateTime = __v.get();
        }
    }

    public Ice.Optional<DateTimeTyp>
    optionalTReturnDateTime()
    {
        if(__has_tReturnDateTime)
        {
            return new Ice.Optional<DateTimeTyp>(tReturnDateTime);
        }
        else
        {
            return new Ice.Optional<DateTimeTyp>();
        }
    }

    public static final long serialVersionUID = -872286346L;
}
