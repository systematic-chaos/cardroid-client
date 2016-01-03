#pragma once

[["java:package:uclm.esi"]]
module cardroid {
	
	/* Forward declaration. */
	module data {
		module zerocice {
			class UserTyp;
		};
	};
	
	module network {
		
		module zerocice {
		
			exception RegistrationDeniedException {
				string reason;
			};
				
			/**
			 * Interface for the registration of new users in the CarDroid platform
			 **/
			interface Registration {
			/**
				 * Register a new user in the CarDroid platform
				 * @param newUser	The new user data
				 * @param password	The new user's password
				 * @return			Whether the register process completed
				 *						successfully
				 * @throws RegistrationDeniedException The user registration 
				 *										did not complete 
				 *										successfully for some 
				 *										reason
				 **/			
				bool registerNewUser(cardroid::data::zerocice::UserTyp newUser, string password) throws RegistrationDeniedException;
			};
		};
	};
};
