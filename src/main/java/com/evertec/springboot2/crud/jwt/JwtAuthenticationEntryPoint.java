package com.evertec.springboot2.crud.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import com.evertec.springboot2.crud.model.ErrorResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // authException.getMessage()
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", "Request no autorizado.", HttpServletResponse.SC_UNAUTHORIZED);


        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(errorResponse);

        OutputStream out = response.getOutputStream();
        out.write(jsonBody.getBytes());
        out.flush();
    }
}
