package com.play.auth.elements.auth.main;

import javax.inject.Singleton;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

@Singleton
class SecurityHandlerCache implements HandlerCache {
	
	private final DeadboltHandler defaultHandler = new SecurityDeadboltHandler();

	@Override
	public DeadboltHandler apply(final String key) {
		return this.defaultHandler;
	}

	@Override
	public DeadboltHandler get() {
		return this.defaultHandler;
	}
}