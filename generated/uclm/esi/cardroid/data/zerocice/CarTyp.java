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

public abstract class CarTyp extends Ice.ObjectImpl
                             implements _CarTypOperations,
                                        _CarTypOperationsNC
{
    public CarTyp()
    {
    }

    public CarTyp(String brand, String model, Fuel carFuel, double consumptionPerKm, int nSeats, String color, String plate)
    {
        this.brand = brand;
        this.model = model;
        this.carFuel = carFuel;
        this.consumptionPerKm = consumptionPerKm;
        this.nSeats = nSeats;
        this.color = color;
        this.plate = plate;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::cardroid::data::zerocice::CarTyp"
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

    public final String getBrand()
    {
        return getBrand(null);
    }

    public final Fuel getCarFuel()
    {
        return getCarFuel(null);
    }

    public final String getColor()
    {
        return getColor(null);
    }

    public final double getConsumptionPerKm()
    {
        return getConsumptionPerKm(null);
    }

    public final String getModel()
    {
        return getModel(null);
    }

    public final int getNSeats()
    {
        return getNSeats(null);
    }

    public final String getPlate()
    {
        return getPlate(null);
    }

    public final void setBrand(String brand)
    {
        setBrand(brand, null);
    }

    public final void setCarFuel(Fuel carFuel)
    {
        setCarFuel(carFuel, null);
    }

    public final void setColor(String color)
    {
        setColor(color, null);
    }

    public final void setConsumptionPerKm(double consumptionPerKm)
    {
        setConsumptionPerKm(consumptionPerKm, null);
    }

    public final void setModel(String model)
    {
        setModel(model, null);
    }

    public final void setNSeats(int nSeats)
    {
        setNSeats(nSeats, null);
    }

    public final void setPlate(String plate)
    {
        setPlate(plate, null);
    }

    public final String _toString()
    {
        return _toString(null);
    }

    public static Ice.DispatchStatus ___getBrand(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getBrand(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setBrand(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String brand;
        brand = __is.readString();
        __inS.endReadParams();
        __obj.setBrand(brand, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getModel(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getModel(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setModel(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String model;
        model = __is.readString();
        __inS.endReadParams();
        __obj.setModel(model, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getCarFuel(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        Fuel __ret = __obj.getCarFuel(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setCarFuel(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        Fuel carFuel;
        carFuel = Fuel.__read(__is);
        __inS.endReadParams();
        __obj.setCarFuel(carFuel, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getConsumptionPerKm(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        double __ret = __obj.getConsumptionPerKm(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeDouble(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setConsumptionPerKm(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        double consumptionPerKm;
        consumptionPerKm = __is.readDouble();
        __inS.endReadParams();
        __obj.setConsumptionPerKm(consumptionPerKm, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getNSeats(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        int __ret = __obj.getNSeats(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeInt(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setNSeats(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        int nSeats;
        nSeats = __is.readInt();
        __inS.endReadParams();
        __obj.setNSeats(nSeats, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getColor(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getColor(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setColor(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String color;
        color = __is.readString();
        __inS.endReadParams();
        __obj.setColor(color, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getPlate(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getPlate(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setPlate(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String plate;
        plate = __is.readString();
        __inS.endReadParams();
        __obj.setPlate(plate, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___toString(CarTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj._toString(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "getBrand",
        "getCarFuel",
        "getColor",
        "getConsumptionPerKm",
        "getModel",
        "getNSeats",
        "getPlate",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "setBrand",
        "setCarFuel",
        "setColor",
        "setConsumptionPerKm",
        "setModel",
        "setNSeats",
        "setPlate",
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
                return ___getBrand(this, in, __current);
            }
            case 1:
            {
                return ___getCarFuel(this, in, __current);
            }
            case 2:
            {
                return ___getColor(this, in, __current);
            }
            case 3:
            {
                return ___getConsumptionPerKm(this, in, __current);
            }
            case 4:
            {
                return ___getModel(this, in, __current);
            }
            case 5:
            {
                return ___getNSeats(this, in, __current);
            }
            case 6:
            {
                return ___getPlate(this, in, __current);
            }
            case 7:
            {
                return ___ice_id(this, in, __current);
            }
            case 8:
            {
                return ___ice_ids(this, in, __current);
            }
            case 9:
            {
                return ___ice_isA(this, in, __current);
            }
            case 10:
            {
                return ___ice_ping(this, in, __current);
            }
            case 11:
            {
                return ___setBrand(this, in, __current);
            }
            case 12:
            {
                return ___setCarFuel(this, in, __current);
            }
            case 13:
            {
                return ___setColor(this, in, __current);
            }
            case 14:
            {
                return ___setConsumptionPerKm(this, in, __current);
            }
            case 15:
            {
                return ___setModel(this, in, __current);
            }
            case 16:
            {
                return ___setNSeats(this, in, __current);
            }
            case 17:
            {
                return ___setPlate(this, in, __current);
            }
            case 18:
            {
                return ___toString(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    protected void __writeImpl(IceInternal.BasicStream __os)
    {
        __os.startWriteSlice(ice_staticId(), -1, true);
        __os.writeString(brand);
        __os.writeString(model);
        carFuel.__write(__os);
        __os.writeDouble(consumptionPerKm);
        __os.writeInt(nSeats);
        __os.writeString(color);
        __os.writeString(plate);
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        brand = __is.readString();
        model = __is.readString();
        carFuel = Fuel.__read(__is);
        consumptionPerKm = __is.readDouble();
        nSeats = __is.readInt();
        color = __is.readString();
        plate = __is.readString();
        __is.endReadSlice();
    }

    public String brand;

    public String model;

    public Fuel carFuel;

    public double consumptionPerKm;

    public int nSeats;

    public String color;

    public String plate;

    public static final long serialVersionUID = 796104884L;
}
