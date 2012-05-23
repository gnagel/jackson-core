package com.fasterxml.jackson.core;

/**
 * Intermediate base class for all problems encountered when
 * processing (parsing, generating) JSON content
 * that are not pure I/O problems.
 * Regular {@link java.io.IOException}s will be passed through as is.
 * Sub-class of {@link java.io.IOException} for convenience.
 */
public class JsonProcessingException
		extends java.io.IOException
{
	final static long		serialVersionUID	= 123;	// Stupid eclipse...


	protected JsonLocation	_location;


	protected JsonProcessingException(final String msg)
	{
		super(msg);
	}


	protected JsonProcessingException(final String msg, final JsonLocation loc)
	{
		this(msg, loc, null);
	}


	protected JsonProcessingException(final String msg, final JsonLocation loc, final Throwable rootCause)
	{
		/*
		 * Argh. IOException(Throwable,String) is only available starting
		 * with JDK 1.6...
		 */
		super(msg);
		if (rootCause != null) {
			initCause(rootCause);
		}
		_location = loc;
	}


	protected JsonProcessingException(final String msg, final Throwable rootCause)
	{
		this(msg, null, rootCause);
	}


	protected JsonProcessingException(final Throwable rootCause)
	{
		this(null, null, rootCause);
	}


	public JsonLocation getLocation() {
		return _location;
	}


	/*
	 * /**********************************************************
	 * /* Methods for sub-classes to use, override
	 * /**********************************************************
	 */

	/**
	 * Default method overridden so that we can add location information
	 */
	@Override
	public String getMessage()
	{
		String msg = super.getMessage();
		if (msg == null) {
			msg = "N/A";
		}
		final JsonLocation loc = getLocation();
		final String suffix = getMessageSuffix();
		// mild optimization, if nothing extra is needed:
		if (loc != null || suffix != null) {
			final StringBuilder sb = new StringBuilder(100);
			sb.append(msg);
			if (suffix != null) {
				sb.append(suffix);
			}
			if (loc != null) {
				sb.append('\n');
				sb.append(" at ");
				sb.append(loc.toString());
			}
			msg = sb.toString();
		}
		return msg;
	}


	/*
	 * /**********************************************************
	 * /* Overrides of standard methods
	 * /**********************************************************
	 */

	/**
	 * Accessor that sub-classes can override to append additional
	 * information right after the main message, but before
	 * source location information.
	 */
	protected String getMessageSuffix() {
		return null;
	}


	@Override
	public String toString() {
		return getClass().getName() + ": " + getMessage();
	}
}
