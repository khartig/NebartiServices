/*
 * Nebarti
 * Copyright 2013 All rights reserved.
 */
package com.idot.jackson;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Component;

@Provider
@Component
@Produces({MediaType.APPLICATION_JSON})
public class JacksonMapperProvider implements ContextResolver<ObjectMapper> {
   ObjectMapper mapper;

   public JacksonMapperProvider(){
       mapper = new ObjectMapper();
       mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, true);

   }

   @Override
   public ObjectMapper getContext(Class<?> aClass) {
       return mapper;
   }
}
