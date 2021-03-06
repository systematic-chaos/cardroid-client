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

public final class Glacier2SessionHolder extends Ice.ObjectHolderBase<Glacier2Session>
{
    public
    Glacier2SessionHolder()
    {
    }

    public
    Glacier2SessionHolder(Glacier2Session value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof Glacier2Session)
        {
            value = (Glacier2Session)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _Glacier2SessionDisp.ice_staticId();
    }
}
