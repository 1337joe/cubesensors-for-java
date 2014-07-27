package com.w3asel.cubesensors.api.v1.json;

import java.io.IOException;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Jackson doesn't seem to understand java.time objects for time yet, have to
 * custom-parse them.
 *
 * @author Joe
 */
public class SpanDateDeserializer extends JsonDeserializer<ZonedDateTime> {
	@Override
	public ZonedDateTime deserialize(final JsonParser jp,
			final DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		String text = jp.getText();

		// correct for the time zone not being specified
		if (!text.endsWith("Z")) {
			text += "Z";
		}

		return ZonedDateTime.parse(text);
	}
}
