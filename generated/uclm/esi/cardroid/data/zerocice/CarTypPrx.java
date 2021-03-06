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

public interface CarTypPrx extends Ice.ObjectPrx
{
    public String getBrand();

    public String getBrand(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getBrand();

    public Ice.AsyncResult begin_getBrand(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getBrand(Ice.Callback __cb);

    public Ice.AsyncResult begin_getBrand(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getBrand(Callback_CarTyp_getBrand __cb);

    public Ice.AsyncResult begin_getBrand(java.util.Map<String, String> __ctx, Callback_CarTyp_getBrand __cb);

    public String end_getBrand(Ice.AsyncResult __result);

    public void setBrand(String brand);

    public void setBrand(String brand, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setBrand(String brand);

    public Ice.AsyncResult begin_setBrand(String brand, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setBrand(String brand, Ice.Callback __cb);

    public Ice.AsyncResult begin_setBrand(String brand, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setBrand(String brand, Callback_CarTyp_setBrand __cb);

    public Ice.AsyncResult begin_setBrand(String brand, java.util.Map<String, String> __ctx, Callback_CarTyp_setBrand __cb);

    public void end_setBrand(Ice.AsyncResult __result);

    public String getModel();

    public String getModel(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getModel();

    public Ice.AsyncResult begin_getModel(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getModel(Ice.Callback __cb);

    public Ice.AsyncResult begin_getModel(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getModel(Callback_CarTyp_getModel __cb);

    public Ice.AsyncResult begin_getModel(java.util.Map<String, String> __ctx, Callback_CarTyp_getModel __cb);

    public String end_getModel(Ice.AsyncResult __result);

    public void setModel(String model);

    public void setModel(String model, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setModel(String model);

    public Ice.AsyncResult begin_setModel(String model, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setModel(String model, Ice.Callback __cb);

    public Ice.AsyncResult begin_setModel(String model, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setModel(String model, Callback_CarTyp_setModel __cb);

    public Ice.AsyncResult begin_setModel(String model, java.util.Map<String, String> __ctx, Callback_CarTyp_setModel __cb);

    public void end_setModel(Ice.AsyncResult __result);

    public Fuel getCarFuel();

    public Fuel getCarFuel(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getCarFuel();

    public Ice.AsyncResult begin_getCarFuel(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getCarFuel(Ice.Callback __cb);

    public Ice.AsyncResult begin_getCarFuel(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getCarFuel(Callback_CarTyp_getCarFuel __cb);

    public Ice.AsyncResult begin_getCarFuel(java.util.Map<String, String> __ctx, Callback_CarTyp_getCarFuel __cb);

    public Fuel end_getCarFuel(Ice.AsyncResult __result);

    public void setCarFuel(Fuel carFuel);

    public void setCarFuel(Fuel carFuel, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel, Ice.Callback __cb);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel, Callback_CarTyp_setCarFuel __cb);

    public Ice.AsyncResult begin_setCarFuel(Fuel carFuel, java.util.Map<String, String> __ctx, Callback_CarTyp_setCarFuel __cb);

    public void end_setCarFuel(Ice.AsyncResult __result);

    public double getConsumptionPerKm();

    public double getConsumptionPerKm(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getConsumptionPerKm();

    public Ice.AsyncResult begin_getConsumptionPerKm(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getConsumptionPerKm(Ice.Callback __cb);

    public Ice.AsyncResult begin_getConsumptionPerKm(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getConsumptionPerKm(Callback_CarTyp_getConsumptionPerKm __cb);

    public Ice.AsyncResult begin_getConsumptionPerKm(java.util.Map<String, String> __ctx, Callback_CarTyp_getConsumptionPerKm __cb);

    public double end_getConsumptionPerKm(Ice.AsyncResult __result);

    public void setConsumptionPerKm(double consumptionPerKm);

    public void setConsumptionPerKm(double consumptionPerKm, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm, Ice.Callback __cb);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm, Callback_CarTyp_setConsumptionPerKm __cb);

    public Ice.AsyncResult begin_setConsumptionPerKm(double consumptionPerKm, java.util.Map<String, String> __ctx, Callback_CarTyp_setConsumptionPerKm __cb);

    public void end_setConsumptionPerKm(Ice.AsyncResult __result);

    public int getNSeats();

    public int getNSeats(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNSeats();

    public Ice.AsyncResult begin_getNSeats(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getNSeats(Ice.Callback __cb);

    public Ice.AsyncResult begin_getNSeats(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getNSeats(Callback_CarTyp_getNSeats __cb);

    public Ice.AsyncResult begin_getNSeats(java.util.Map<String, String> __ctx, Callback_CarTyp_getNSeats __cb);

    public int end_getNSeats(Ice.AsyncResult __result);

    public void setNSeats(int nSeats);

    public void setNSeats(int nSeats, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setNSeats(int nSeats);

    public Ice.AsyncResult begin_setNSeats(int nSeats, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setNSeats(int nSeats, Ice.Callback __cb);

    public Ice.AsyncResult begin_setNSeats(int nSeats, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setNSeats(int nSeats, Callback_CarTyp_setNSeats __cb);

    public Ice.AsyncResult begin_setNSeats(int nSeats, java.util.Map<String, String> __ctx, Callback_CarTyp_setNSeats __cb);

    public void end_setNSeats(Ice.AsyncResult __result);

    public String getColor();

    public String getColor(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getColor();

    public Ice.AsyncResult begin_getColor(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getColor(Ice.Callback __cb);

    public Ice.AsyncResult begin_getColor(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getColor(Callback_CarTyp_getColor __cb);

    public Ice.AsyncResult begin_getColor(java.util.Map<String, String> __ctx, Callback_CarTyp_getColor __cb);

    public String end_getColor(Ice.AsyncResult __result);

    public void setColor(String color);

    public void setColor(String color, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setColor(String color);

    public Ice.AsyncResult begin_setColor(String color, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setColor(String color, Ice.Callback __cb);

    public Ice.AsyncResult begin_setColor(String color, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setColor(String color, Callback_CarTyp_setColor __cb);

    public Ice.AsyncResult begin_setColor(String color, java.util.Map<String, String> __ctx, Callback_CarTyp_setColor __cb);

    public void end_setColor(Ice.AsyncResult __result);

    public String getPlate();

    public String getPlate(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getPlate();

    public Ice.AsyncResult begin_getPlate(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getPlate(Ice.Callback __cb);

    public Ice.AsyncResult begin_getPlate(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getPlate(Callback_CarTyp_getPlate __cb);

    public Ice.AsyncResult begin_getPlate(java.util.Map<String, String> __ctx, Callback_CarTyp_getPlate __cb);

    public String end_getPlate(Ice.AsyncResult __result);

    public void setPlate(String plate);

    public void setPlate(String plate, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setPlate(String plate);

    public Ice.AsyncResult begin_setPlate(String plate, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_setPlate(String plate, Ice.Callback __cb);

    public Ice.AsyncResult begin_setPlate(String plate, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_setPlate(String plate, Callback_CarTyp_setPlate __cb);

    public Ice.AsyncResult begin_setPlate(String plate, java.util.Map<String, String> __ctx, Callback_CarTyp_setPlate __cb);

    public void end_setPlate(Ice.AsyncResult __result);

    public String _toString();

    public String _toString(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_toString();

    public Ice.AsyncResult begin_toString(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_toString(Ice.Callback __cb);

    public Ice.AsyncResult begin_toString(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_toString(Callback_CarTyp_toString __cb);

    public Ice.AsyncResult begin_toString(java.util.Map<String, String> __ctx, Callback_CarTyp_toString __cb);

    public String end_toString(Ice.AsyncResult __result);
}
