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

public interface _PlaceTypOperationsNC
{
    String getName();

    void setName(String name);

    LatLngTyp getCoords();

    void setCoords(LatLngTyp coords);

    String getDescription();

    void setDescription(String description);

    boolean hasDescription();

    byte[] getSnapshotBytes();

    void setSnapshotBytes(byte[] snapshotBytes);

    boolean hasSnapshot();

    String _toString();
}
