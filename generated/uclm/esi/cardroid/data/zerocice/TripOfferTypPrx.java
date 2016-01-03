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

public interface TripOfferTypPrx extends TripTypPrx
{
    public DateTimeTyp getTripDateTime();

    public DateTimeTyp getTripDateTime(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripDateTime();

    public Ice.AsyncResult begin_getTripDateTime(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripDateTime(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripDateTime(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripDateTime(Callback_TripOfferTyp_getTripDateTime __cb);

    public Ice.AsyncResult begin_getTripDateTime(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripDateTime __cb);

    public DateTimeTyp end_getTripDateTime(Ice.AsyncResult __result);

    public void setTripDateTime(DateTimeTyp dt);

    public void setTripDateTime(DateTimeTyp dt, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt, Callback_TripOfferTyp_setTripDateTime __cb);

    public Ice.AsyncResult begin_setTripDateTime(DateTimeTyp dt, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripDateTime __cb);

    public void end_setTripDateTime(Ice.AsyncResult __result);

    public DateTimeTyp getTripReturnDateTime();

    public DateTimeTyp getTripReturnDateTime(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripReturnDateTime();

    public Ice.AsyncResult begin_getTripReturnDateTime(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripReturnDateTime(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripReturnDateTime(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripReturnDateTime(Callback_TripOfferTyp_getTripReturnDateTime __cb);

    public Ice.AsyncResult begin_getTripReturnDateTime(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripReturnDateTime __cb);

    public DateTimeTyp end_getTripReturnDateTime(Ice.AsyncResult __result);

    public void setTripReturnDateTime(DateTimeTyp rdt);

    public void setTripReturnDateTime(DateTimeTyp rdt, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt, Callback_TripOfferTyp_setTripReturnDateTime __cb);

    public Ice.AsyncResult begin_setTripReturnDateTime(DateTimeTyp rdt, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripReturnDateTime __cb);

    public void end_setTripReturnDateTime(Ice.AsyncResult __result);

    public UserTypPrx getTripDriver();

    public UserTypPrx getTripDriver(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripDriver();

    public Ice.AsyncResult begin_getTripDriver(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripDriver(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripDriver(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripDriver(Callback_TripOfferTyp_getTripDriver __cb);

    public Ice.AsyncResult begin_getTripDriver(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripDriver __cb);

    public UserTypPrx end_getTripDriver(Ice.AsyncResult __result);

    public void setTripDriver(UserTypPrx driver);

    public void setTripDriver(UserTypPrx driver, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver, Callback_TripOfferTyp_setTripDriver __cb);

    public Ice.AsyncResult begin_setTripDriver(UserTypPrx driver, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripDriver __cb);

    public void end_setTripDriver(Ice.AsyncResult __result);

    public java.util.List<WaypointTyp> getTripWaypoints();

    public java.util.List<WaypointTyp> getTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripWaypoints();

    public Ice.AsyncResult begin_getTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripWaypoints(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripWaypoints(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripWaypoints(Callback_TripOfferTyp_getTripWaypoints __cb);

    public Ice.AsyncResult begin_getTripWaypoints(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripWaypoints __cb);

    public java.util.List<WaypointTyp> end_getTripWaypoints(Ice.AsyncResult __result);

    public void setTripWaypoints(java.util.List<WaypointTyp> waypoints);

    public void setTripWaypoints(java.util.List<WaypointTyp> waypoints, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints, Callback_TripOfferTyp_setTripWaypoints __cb);

    public Ice.AsyncResult begin_setTripWaypoints(java.util.List<WaypointTyp> waypoints, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripWaypoints __cb);

    public void end_setTripWaypoints(Ice.AsyncResult __result);

    public CarTypPrx getTripCar();

    public CarTypPrx getTripCar(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripCar();

    public Ice.AsyncResult begin_getTripCar(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripCar(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripCar(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripCar(Callback_TripOfferTyp_getTripCar __cb);

    public Ice.AsyncResult begin_getTripCar(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripCar __cb);

    public CarTypPrx end_getTripCar(Ice.AsyncResult __result);

    public void setTripCar(CarTypPrx tripCar);

    public void setTripCar(CarTypPrx tripCar, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar, Callback_TripOfferTyp_setTripCar __cb);

    public Ice.AsyncResult begin_setTripCar(CarTypPrx tripCar, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripCar __cb);

    public void end_setTripCar(Ice.AsyncResult __result);

    public java.util.List<PassengerTyp> getTripPassengers();

    public java.util.List<PassengerTyp> getTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripPassengers();

    public Ice.AsyncResult begin_getTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getTripPassengers(Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripPassengers(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getTripPassengers(Callback_TripOfferTyp_getTripPassengers __cb);

    public Ice.AsyncResult begin_getTripPassengers(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getTripPassengers __cb);

    public java.util.List<PassengerTyp> end_getTripPassengers(Ice.AsyncResult __result);

    public void setTripPassengers(java.util.List<PassengerTyp> passengers);

    public void setTripPassengers(java.util.List<PassengerTyp> passengers, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers, Callback_TripOfferTyp_setTripPassengers __cb);

    public Ice.AsyncResult begin_setTripPassengers(java.util.List<PassengerTyp> passengers, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setTripPassengers __cb);

    public void end_setTripPassengers(Ice.AsyncResult __result);

    public double getPrice();

    public double getPrice(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getPrice();

    public Ice.AsyncResult begin_getPrice(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getPrice(Ice.Callback __cb);

    public Ice.AsyncResult begin_getPrice(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getPrice(Callback_TripOfferTyp_getPrice __cb);

    public Ice.AsyncResult begin_getPrice(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getPrice __cb);

    public double end_getPrice(Ice.AsyncResult __result);

    public void setPrice(double price);

    public void setPrice(double price, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setPrice(double price);

    public Ice.AsyncResult begin_setPrice(double price, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setPrice(double price, Ice.Callback __cb);

    public Ice.AsyncResult begin_setPrice(double price, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setPrice(double price, Callback_TripOfferTyp_setPrice __cb);

    public Ice.AsyncResult begin_setPrice(double price, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setPrice __cb);

    public void end_setPrice(Ice.AsyncResult __result);

    public String[] getAllowedFeatures();

    public String[] getAllowedFeatures(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getAllowedFeatures();

    public Ice.AsyncResult begin_getAllowedFeatures(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getAllowedFeatures(Ice.Callback __cb);

    public Ice.AsyncResult begin_getAllowedFeatures(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getAllowedFeatures(Callback_TripOfferTyp_getAllowedFeatures __cb);

    public Ice.AsyncResult begin_getAllowedFeatures(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getAllowedFeatures __cb);

    public String[] end_getAllowedFeatures(Ice.AsyncResult __result);

    public void setAllowedFeatures(String[] allowed);

    public void setAllowedFeatures(String[] allowed, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed, Ice.Callback __cb);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed, Callback_TripOfferTyp_setAllowedFeatures __cb);

    public Ice.AsyncResult begin_setAllowedFeatures(String[] allowed, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_setAllowedFeatures __cb);

    public void end_setAllowedFeatures(Ice.AsyncResult __result);

    public boolean addTripWaypoint(PlaceTyp waypoint);

    public boolean addTripWaypoint(PlaceTyp waypoint, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint, Ice.Callback __cb);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint, Callback_TripOfferTyp_addTripWaypoint __cb);

    public Ice.AsyncResult begin_addTripWaypoint(PlaceTyp waypoint, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_addTripWaypoint __cb);

    public boolean end_addTripWaypoint(Ice.AsyncResult __result);

    public boolean removeTripWaypoint(int pos);

    public boolean removeTripWaypoint(int pos, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos, Callback_TripOfferTyp_removeTripWaypoint __cb);

    public Ice.AsyncResult begin_removeTripWaypoint(int pos, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_removeTripWaypoint __cb);

    public boolean end_removeTripWaypoint(Ice.AsyncResult __result);

    public int getNTripWaypoints();

    public int getNTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNTripWaypoints();

    public Ice.AsyncResult begin_getNTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNTripWaypoints(Ice.Callback __cb);

    public Ice.AsyncResult begin_getNTripWaypoints(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getNTripWaypoints(Callback_TripOfferTyp_getNTripWaypoints __cb);

    public Ice.AsyncResult begin_getNTripWaypoints(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getNTripWaypoints __cb);

    public int end_getNTripWaypoints(Ice.AsyncResult __result);

    public void clearTripWaypoints();

    public void clearTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_clearTripWaypoints();

    public Ice.AsyncResult begin_clearTripWaypoints(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_clearTripWaypoints(Ice.Callback __cb);

    public Ice.AsyncResult begin_clearTripWaypoints(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_clearTripWaypoints(Callback_TripOfferTyp_clearTripWaypoints __cb);

    public Ice.AsyncResult begin_clearTripWaypoints(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_clearTripWaypoints __cb);

    public void end_clearTripWaypoints(Ice.AsyncResult __result);

    public boolean addTripPassenger(UserTypPrx passenger, int seats);

    public boolean addTripPassenger(UserTypPrx passenger, int seats, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats, Ice.Callback __cb);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats, Callback_TripOfferTyp_addTripPassenger __cb);

    public Ice.AsyncResult begin_addTripPassenger(UserTypPrx passenger, int seats, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_addTripPassenger __cb);

    public boolean end_addTripPassenger(Ice.AsyncResult __result);

    public boolean removeTripPassenger(UserTypPrx passenger);

    public boolean removeTripPassenger(UserTypPrx passenger, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger, Callback_TripOfferTyp_removeTripPassenger __cb);

    public Ice.AsyncResult begin_removeTripPassenger(UserTypPrx passenger, java.util.Map<String, String> __ctx, Callback_TripOfferTyp_removeTripPassenger __cb);

    public boolean end_removeTripPassenger(Ice.AsyncResult __result);

    public int getNTripPassengers();

    public int getNTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNTripPassengers();

    public Ice.AsyncResult begin_getNTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNTripPassengers(Ice.Callback __cb);

    public Ice.AsyncResult begin_getNTripPassengers(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getNTripPassengers(Callback_TripOfferTyp_getNTripPassengers __cb);

    public Ice.AsyncResult begin_getNTripPassengers(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_getNTripPassengers __cb);

    public int end_getNTripPassengers(Ice.AsyncResult __result);

    public void clearTripPassengers();

    public void clearTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_clearTripPassengers();

    public Ice.AsyncResult begin_clearTripPassengers(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_clearTripPassengers(Ice.Callback __cb);

    public Ice.AsyncResult begin_clearTripPassengers(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_clearTripPassengers(Callback_TripOfferTyp_clearTripPassengers __cb);

    public Ice.AsyncResult begin_clearTripPassengers(java.util.Map<String, String> __ctx, Callback_TripOfferTyp_clearTripPassengers __cb);

    public void end_clearTripPassengers(Ice.AsyncResult __result);
}
