package com.traceisys.auth.security;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;

import weblogic.security.Security;

public class SubjectHelper {
	public static final SubjectMechanism mechanism = SubjectMechanism.WEBLOGIC;
	
	public static Subject getSubject() {
		switch ( mechanism ) {
		case JAAS:
			return Subject.getSubject( AccessController.getContext() );
		case WEBLOGIC:
			return Security.getCurrentSubject();
		default:
			throw new IllegalArgumentException();
		}
	}

	public static <T> T doAs( Subject subject, PrivilegedAction<T> privilegedAction ) {
		switch ( mechanism ) {
		case JAAS:
			return Subject.doAs( subject, privilegedAction );
		case WEBLOGIC:
			return (T) Security.runAs( subject, privilegedAction );
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public static <T> T doAs( Subject subject, PrivilegedExceptionAction<T> privilegedExceptionAction ) throws PrivilegedActionException {
		switch ( mechanism ) {
		case JAAS:
			return Subject.doAs( subject, privilegedExceptionAction );
		case WEBLOGIC:
			return (T) Security.runAs( subject, privilegedExceptionAction );
		default:
			throw new IllegalArgumentException();
		}
	}
}
