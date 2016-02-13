package com.play.auth.elements.auth.main;

import play.Application;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;
import com.google.inject.Inject;

public class ServiceUserPlugin extends UserServicePlugin {

	@Inject
	public ServiceUserPlugin(final Application app) {
		super(app);
	}

	@Override
	public Object save(final AuthUser authUser) {
		final boolean isLinked = EntryUser.existsByAuthUserIdentity(authUser);
		if (!isLinked) {
			return EntryUser.create(authUser).id;
		} else {
			// we have this user already, so return null
			return null;
		}
	}

	@Override
	public Object getLocalIdentity(final AuthUserIdentity identity) {
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted
		final EntryUser user = EntryUser.findByAuthUserIdentity(identity);
		if(user != null) {
			return user.id;
		} else {
			return null;
		}
	}

	@Override
	public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
		if (!oldUser.equals(newUser)) {
			EntryUser.merge(oldUser, newUser);
		}
		return oldUser;
	}

	@Override
	public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
		EntryUser.addLinkedAccount(oldUser, newUser);
		return newUser;
	}
	
	@Override
	public AuthUser update(final AuthUser knownUser) {
		// User logged in again, bump last login date
		EntryUser.setLastLoginDate(knownUser);
		return knownUser;
	}

}
