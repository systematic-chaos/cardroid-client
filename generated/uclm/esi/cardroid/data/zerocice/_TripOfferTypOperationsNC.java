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

public interface _TripOfferTypOperationsNC extends _TripTypOperationsNC
{
    DateTimeTyp getTripDateTime();

    void setTripDateTime(DateTimeTyp dt);

    DateTimeTyp getTripReturnDateTime();

    void setTripReturnDateTime(DateTimeTyp rdt);

    UserTypPrx getTripDriver();

    void setTripDriver(UserTypPrx driver);

    java.util.List<WaypointTyp> getTripWaypoints();

    void setTripWaypoints(java.util.List<WaypointTyp> waypoints);

    CarTypPrx getTripCar();

    void setTripCar(CarTypPrx tripCar);

    java.util.List<PassengerTyp> getTripPassengers();

    void setTripPassengers(java.util.List<PassengerTyp> passengers);

    double getPrice();

    void setPrice(double price);

    String[] getAllowedFeatures();

    void setAllowedFeatures(String[] allowed);

    boolean addTripWaypoint(PlaceTyp waypoint);

    boolean removeTripWaypoint(int pos);

    int getNTripWaypoints();

    void clearTripWaypoints();

    boolean addTripPassenger(UserTypPrx passenger, int seats);

    boolean removeTripPassenger(UserTypPrx passenger);

    int getNTripPassengers();

    void clearTripPassengers();
}