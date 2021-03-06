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

public abstract class PlaceTyp extends Ice.ObjectImpl
                               implements _PlaceTypOperations,
                                          _PlaceTypOperationsNC
{
    public PlaceTyp()
    {
    }

    public PlaceTyp(String name, LatLngTyp coords)
    {
        this.name = name;
        this.coords = coords;
    }

    public PlaceTyp(String name, LatLngTyp coords, String placeDescription, byte[] placeSnapshotBytes)
    {
        this.name = name;
        this.coords = coords;
        setPlaceDescription(placeDescription);
        setPlaceSnapshotBytes(placeSnapshotBytes);
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::cardroid::data::zerocice::PlaceTyp"
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

    public final LatLngTyp getCoords()
    {
        return getCoords(null);
    }

    public final String getDescription()
    {
        return getDescription(null);
    }

    public final String getName()
    {
        return getName(null);
    }

    public final byte[] getSnapshotBytes()
    {
        return getSnapshotBytes(null);
    }

    public final boolean hasDescription()
    {
        return hasDescription(null);
    }

    public final boolean hasSnapshot()
    {
        return hasSnapshot(null);
    }

    public final void setCoords(LatLngTyp coords)
    {
        setCoords(coords, null);
    }

    public final void setDescription(String description)
    {
        setDescription(description, null);
    }

    public final void setName(String name)
    {
        setName(name, null);
    }

    public final void setSnapshotBytes(byte[] snapshotBytes)
    {
        setSnapshotBytes(snapshotBytes, null);
    }

    public final String _toString()
    {
        return _toString(null);
    }

    public static Ice.DispatchStatus ___getName(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getName(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setName(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String name;
        name = __is.readString();
        __inS.endReadParams();
        __obj.setName(name, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getCoords(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        LatLngTyp __ret = __obj.getCoords(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __ret.__write(__os);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setCoords(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        LatLngTyp coords;
        coords = new LatLngTyp();
        coords.__read(__is);
        __inS.endReadParams();
        __obj.setCoords(coords, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getDescription(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        String __ret = __obj.getDescription(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeString(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setDescription(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        String description;
        description = __is.readString();
        __inS.endReadParams();
        __obj.setDescription(description, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___hasDescription(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        boolean __ret = __obj.hasDescription(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___getSnapshotBytes(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        byte[] __ret = __obj.getSnapshotBytes(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        BlobHelper.write(__os, __ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___setSnapshotBytes(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        IceInternal.BasicStream __is = __inS.startReadParams();
        byte[] snapshotBytes;
        snapshotBytes = BlobHelper.read(__is);
        __inS.endReadParams();
        __obj.setSnapshotBytes(snapshotBytes, __current);
        __inS.__writeEmptyParams();
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___hasSnapshot(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Idempotent, __current.mode);
        __inS.readEmptyParams();
        boolean __ret = __obj.hasSnapshot(__current);
        IceInternal.BasicStream __os = __inS.__startWriteParams(Ice.FormatType.DefaultFormat);
        __os.writeBool(__ret);
        __inS.__endWriteParams(true);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus ___toString(PlaceTyp __obj, IceInternal.Incoming __inS, Ice.Current __current)
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
        "getCoords",
        "getDescription",
        "getName",
        "getSnapshotBytes",
        "hasDescription",
        "hasSnapshot",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "setCoords",
        "setDescription",
        "setName",
        "setSnapshotBytes",
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
                return ___getCoords(this, in, __current);
            }
            case 1:
            {
                return ___getDescription(this, in, __current);
            }
            case 2:
            {
                return ___getName(this, in, __current);
            }
            case 3:
            {
                return ___getSnapshotBytes(this, in, __current);
            }
            case 4:
            {
                return ___hasDescription(this, in, __current);
            }
            case 5:
            {
                return ___hasSnapshot(this, in, __current);
            }
            case 6:
            {
                return ___ice_id(this, in, __current);
            }
            case 7:
            {
                return ___ice_ids(this, in, __current);
            }
            case 8:
            {
                return ___ice_isA(this, in, __current);
            }
            case 9:
            {
                return ___ice_ping(this, in, __current);
            }
            case 10:
            {
                return ___setCoords(this, in, __current);
            }
            case 11:
            {
                return ___setDescription(this, in, __current);
            }
            case 12:
            {
                return ___setName(this, in, __current);
            }
            case 13:
            {
                return ___setSnapshotBytes(this, in, __current);
            }
            case 14:
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
        __os.writeString(name);
        coords.__write(__os);
        if(__has_placeDescription && __os.writeOpt(3, Ice.OptionalFormat.VSize))
        {
            __os.writeString(placeDescription);
        }
        if(__has_placeSnapshotBytes && __os.writeOpt(4, Ice.OptionalFormat.VSize))
        {
            BlobHelper.write(__os, placeSnapshotBytes);
        }
        __os.endWriteSlice();
    }

    protected void __readImpl(IceInternal.BasicStream __is)
    {
        __is.startReadSlice();
        name = __is.readString();
        coords = new LatLngTyp();
        coords.__read(__is);
        if(__has_placeDescription = __is.readOpt(3, Ice.OptionalFormat.VSize))
        {
            placeDescription = __is.readString();
        }
        if(__has_placeSnapshotBytes = __is.readOpt(4, Ice.OptionalFormat.VSize))
        {
            placeSnapshotBytes = BlobHelper.read(__is);
        }
        __is.endReadSlice();
    }

    public String name;

    public LatLngTyp coords;

    private String placeDescription;
    private boolean __has_placeDescription;

    public String
    getPlaceDescription()
    {
        if(!__has_placeDescription)
        {
            throw new java.lang.IllegalStateException("placeDescription is not set");
        }
        return placeDescription;
    }

    public void
    setPlaceDescription(String _placeDescription)
    {
        __has_placeDescription = true;
        placeDescription = _placeDescription;
    }

    public boolean
    hasPlaceDescription()
    {
        return __has_placeDescription;
    }

    public void
    clearPlaceDescription()
    {
        __has_placeDescription = false;
    }

    public void
    optionalPlaceDescription(Ice.Optional<String> __v)
    {
        if(__v == null || !__v.isSet())
        {
            __has_placeDescription = false;
        }
        else
        {
            __has_placeDescription = true;
            placeDescription = __v.get();
        }
    }

    public Ice.Optional<String>
    optionalPlaceDescription()
    {
        if(__has_placeDescription)
        {
            return new Ice.Optional<String>(placeDescription);
        }
        else
        {
            return new Ice.Optional<String>();
        }
    }

    private byte[] placeSnapshotBytes;
    private boolean __has_placeSnapshotBytes;

    public byte[]
    getPlaceSnapshotBytes()
    {
        if(!__has_placeSnapshotBytes)
        {
            throw new java.lang.IllegalStateException("placeSnapshotBytes is not set");
        }
        return placeSnapshotBytes;
    }

    public void
    setPlaceSnapshotBytes(byte[] _placeSnapshotBytes)
    {
        __has_placeSnapshotBytes = true;
        placeSnapshotBytes = _placeSnapshotBytes;
    }

    public boolean
    hasPlaceSnapshotBytes()
    {
        return __has_placeSnapshotBytes;
    }

    public void
    clearPlaceSnapshotBytes()
    {
        __has_placeSnapshotBytes = false;
    }

    public void
    optionalPlaceSnapshotBytes(Ice.Optional<byte[]> __v)
    {
        if(__v == null || !__v.isSet())
        {
            __has_placeSnapshotBytes = false;
        }
        else
        {
            __has_placeSnapshotBytes = true;
            placeSnapshotBytes = __v.get();
        }
    }

    public Ice.Optional<byte[]>
    optionalPlaceSnapshotBytes()
    {
        if(__has_placeSnapshotBytes)
        {
            return new Ice.Optional<byte[]>(placeSnapshotBytes);
        }
        else
        {
            return new Ice.Optional<byte[]>();
        }
    }

    public byte
    getPlaceSnapshotBytes(int _index)
    {
        if(!__has_placeSnapshotBytes)
        {
            throw new java.lang.IllegalStateException("placeSnapshotBytes is not set");
        }
        return placeSnapshotBytes[_index];
    }

    public void
    setPlaceSnapshotBytes(int _index, byte _val)
    {
        if(!__has_placeSnapshotBytes)
        {
            throw new java.lang.IllegalStateException("placeSnapshotBytes is not set");
        }
        placeSnapshotBytes[_index] = _val;
    }

    public static final long serialVersionUID = 2100489119L;
}
