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

public final class WaypointCollectionHelper
{
    public static void
    write(IceInternal.BasicStream __os, java.util.List<WaypointTyp> __v)
    {
        if(__v == null)
        {
            __os.writeSize(0);
        }
        else
        {
            __os.writeSize(__v.size());
            for(WaypointTyp __elem : __v)
            {
                __os.writeObject(__elem);
            }
        }
    }

    public static java.util.List<WaypointTyp>
    read(IceInternal.BasicStream __is)
    {
        java.util.List<WaypointTyp> __v;
        __v = new java.util.ArrayList<WaypointTyp>();
        final int __len0 = __is.readAndCheckSeqSize(1);
        final String __type0 = WaypointTyp.ice_staticId();
        for(int __i0 = 0; __i0 < __len0; __i0++)
        {
            __v.add(null);
            __is.readObject(new IceInternal.ListPatcher<WaypointTyp>(__v, WaypointTyp.class, __type0, __i0));
        }
        return __v;
    }
}
