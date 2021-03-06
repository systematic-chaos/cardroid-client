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

package uclm.esi.cardroid;

/**
 * Interface to get query results.
 * 
 **/
public interface _QueryResultOperationsNC
{
    /**
     * Get more query results.
     * 
     * @param n The maximum number of results to return.
     * 
     * @param destroyed There are no more results, and the query has
     * been destroyed.
     * 
     * @returns A sequence of up to n results.
     * 
     **/
    java.util.List<Ice.ObjectPrx> next(int n, Ice.BooleanHolder destroyed);

    /**
     * Destroy the query result.
     * 
     **/
    void destroy();
}
