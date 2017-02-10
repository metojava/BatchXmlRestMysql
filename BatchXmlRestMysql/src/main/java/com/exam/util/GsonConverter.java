package com.exam.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

public class GsonConverter extends GsonHttpMessageConverter{

	public GsonConverter()
	{
		
		List<MediaType> supportedTypes = Arrays.asList(
				new MediaType("text", "html", DEFAULT_CHARSET),
				new MediaType("application", "xml", DEFAULT_CHARSET),
				new MediaType("application", "json", DEFAULT_CHARSET)
				
				);
		super.setSupportedMediaTypes(supportedTypes);
	}
}
