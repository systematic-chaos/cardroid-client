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
public interface _SessionOperationsNC
{
    /**
     * Get the CarDroid manager object.
     * 
     * @return A proxy for the new CarDroid manager.
     * 
     **/
    uclm.esi.cardroid.zerocice.CardroidManagerPrx getCardroidManager();

    /**
     * Get the topic object used to subscribe to user events via 
     * the IceStorm service.
     * 
     * @return A proxy to the topic referring to the session's 
     * user, used by the server to publish events.
     * 
     **/
    IceStorm.TopicPrx getTopic();

    /**
     * Refresh a session. If a session is not refreshed on a regular
     * basis by the client, it will be automatically destroyed.
     * 
     **/
    void refresh();

    /**
     * Destroy the session.
     * 
     **/
    void destroy();
}