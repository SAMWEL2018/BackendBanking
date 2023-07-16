package com.example.transcsystem.security;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * @author Samwel
 *  2023.
 * Time 5:25 PM
 */

public class CachedHttpRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedPayLoad;

    public CachedHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedPayLoad = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedServletInputStream(this.cachedPayLoad);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedPayLoad);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
}
