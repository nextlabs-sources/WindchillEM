package com.nextlabs;

import wt.access.CustomSecurityLabelValueTranslator;
import wt.util.WTException;

/**
 * This is an example custom translator for use in a custom security label
 * configuration.
 * 
 * 
 */
public class CustomTranslator implements CustomSecurityLabelValueTranslator {
	EntitlementManagerContext emctx = new EntitlementManagerContext();

	/**
	 * Translates an internal custom security label value into an external
	 * representation. In this case, we are changing the plus character(+) into
	 * the comma character(,)
	 * 
	 * @param label_name
	 *            The security label name associated with the value.
	 * @param internal_value
	 *            Internal custom security label value to translate.
	 * @return String External representation of the internal value.
	 * @throws WTException
	 *             Return an exception when the internal value cannot be mapped
	 *             to an external value.
	 */
	public String getExternalValue(String label_name, String internal_value)
			throws WTException {

/*		emctx.log(LogLevel.INFO,
				"getExternalValue Set Security Labels label_name " + label_name);
		emctx.log(LogLevel.INFO,
				"getExternalValue Set Security Labels internal_value "
						+ internal_value);
		internal_value = internal_value.replace("SPACE", " ");
		internal_value = internal_value.replace("#", "HASH");
		internal_value = internal_value.replace(";", "SEMI");
		internal_value = internal_value.replace(".", "DOT");
		internal_value = internal_value.replace("=", "_");
		emctx.log(LogLevel.INFO,
				"getExternalValue after manipulation Set Security Labels internal_value "
						+ internal_value);*/
		return internal_value;
	}

	/**
	 * Translates an external custom security label value into an internal
	 * representation. In this case, we are changing the comma character(,) to
	 * the plus character(+)
	 * 
	 * @param label_name
	 *            The security label name associated with the value.
	 * @param external_value
	 *            External custom security label value to translate.
	 * @return String Internal representation of the external value.
	 * @throws WTException
	 *             Return an exception when the external value cannot be mapped
	 *             to an internal value.
	 */
	public String getInternalValue(String label_name, String external_value)
			throws WTException {
/*
		emctx.log(LogLevel.INFO,
				"getInternalValue Set Security Labels label_name " + label_name);
		emctx.log(LogLevel.INFO,
				"getInternalValue Set Security Labels external_value "
						+ external_value);
		//external_value = external_value.replace("=", "_");
		emctx.log(LogLevel.INFO,
				"getInternalValue Set Security Labels after manipulation external_value "
						+ external_value);*/
		return external_value;
	}

}
