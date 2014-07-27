package com.w3asel.cubesensors.api.v1.json;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class JsonSpanResponse extends JsonCurrentResponse {
	public static class SpanQuery {
		@JsonDeserialize(using = SpanDateDeserializer.class)
		public ZonedDateTime start;
		@JsonDeserialize(using = SpanDateDeserializer.class)
		public ZonedDateTime end;
		public int resolution;
	}

	public SpanQuery query;
}
