#pragma once

#include <Glacier2/Session.ice>
#include <IceStorm/IceStorm.ice>

[["java:package:uclm.esi"]]
module cardroid {

	/* Forward declaration. */
	module data {
		module zerocice {
			class UserActivityTyp;
			class MessageTyp;
		};
	};
	
	/* Forward declaration. */
	module zerocice {
		interface CardroidManager;
	};

	module network {
		
		module zerocice {
			
			/**
	 		* The session object. This is used to retrieve a CarDroid session
		 	* on behalf of the client. If the session is not refreshed on a
	 		* periodic basis, it will be automatically destroyed.
	 		*
	 		**/
			interface Glacier2Session extends Glacier2::Session
			{
				/**
		 		*
		 		* Get the CarDroid manager object.
		 		*
		 		* @return A proxy for the new CarDroid manager.
		 		*
		 		**/
		 		cardroid::zerocice::CardroidManager* getCardroidManager();
				
		 		/**
		 		*
		 		* Get the topic manager object used to subscribe to events via 
		 		* the IceStorm service.
		 		*
		 		* @return A proxy to the topic manager used by the server 
		 		*             to publish events.
		 		*
		 		*/
		 		IceStorm::Topic* getTopic();
				 
				/**
	 	 		*
	 	 		* Refresh a session. If a session is not refreshed on a regular
	 	 		* basis by the client, it will be automatically destroyed.
	 	 		*
	 	 		**/
				idempotent void refresh();
			};
			
			/**
	 		* The session object. This is used to retrieve a CarDroid session
	 		* on behalf of the client. If the session is not refreshed on a
	 		* periodic basis, it will be automatically destroyed.
	 		*
	 		**/
			interface Session
			{
				/**
		 		*
		 		* Get the CarDroid manager object.
		 		*
		 		* @return A proxy for the new CarDroid manager.
		 		*
		 		**/
		 		cardroid::zerocice::CardroidManager* getCardroidManager();
				
				/**
		 		*
		 		* Get the topic object used to subscribe to user events via 
		 		* the IceStorm service.
		 		*
		 		* @return A proxy to the topic referring to the session's 
		 		* user, used by the server to publish events.
		 		*
		 		*/
		 		IceStorm::Topic* getTopic();
				 
				/**
	 	 		*
	 	 		* Refresh a session. If a session is not refreshed on a regular
	 	 		* basis by the client, it will be automatically destroyed.
	 	 		*
	 	 		**/
				idempotent void refresh();
				
				/**
		 		*
		 		* Destroy the session.
		 		*
		 		**/
			 	void destroy();
			};
			
			/**
	 		*
	 		* Interface to create new sessions.
	 		*
	 		**/
			interface SessionFactory
			{
				/**
		 		*
		 		* Create a session.
		 		*
		 		* @return A proxy to the session.
		 		*
		 		**/
				Session* create();
				
				/**
		 		*
		 		* Get the value of the session timeout. Sessions are destroyed
		 		* if they see no activity for this period of time.
		 		*	
		 		* @return The timeout (in seconds).
		 		*
		 		**/
			 	["nonmutating"] idempotent long getSessionTimeout();
			};
			
			/**
		 	*
		 	* The CardroidEventStorm interface is the interface that clients implement
		 	* to publish or subscribe to events delivery via the IceStorm service.
		 	*
		 	* IceStorm calls operations of this interface to deliver events
		 	* published by the server to subscribed clients.
		 	*
		 	**/
			interface CardroidEventStorm
			{
				/**
			 	*
			 	* This message arrives due the occurrence of an user-related action
			 	*
			 	**/
			 	void notify(cardroid::data::zerocice::UserActivityTyp* action);
				 
			 	/**
			  	*
			  	* This message arrives due to the income of a message 
			  	* forwarded to the user
			  	*
			  	**/
			  	void message(cardroid::data::zerocice::MessageTyp* msg);
			};
		};
	};
};
