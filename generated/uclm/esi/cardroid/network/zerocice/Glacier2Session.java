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

/**
 * The session object. This is used to retrieve a CarDroid session
 * on behalf of the client. If the session is not refreshed on a
 * periodic basis, it will be automatically destroyed.
 * 
 **/
public interface Glacier2Session extends Ice.Object,
                                         _Glacier2SessionOperations, _Glacier2SessionOperationsNC,
                                         Glacier2.Session
{
    public static final long serialVersionUID = 199603753L;
}