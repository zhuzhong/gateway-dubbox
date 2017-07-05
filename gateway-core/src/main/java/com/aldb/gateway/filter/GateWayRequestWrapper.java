/**
 * 
 */
package com.aldb.gateway.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Administrator
 *
 */
public class GateWayRequestWrapper extends HttpServletRequestWrapper {

    /**
     * @param request
     */
    public GateWayRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = parseInputStream2Byte(request.getInputStream());
    }

    private final byte[] body; // 报文
    final static int BUFFER_SIZE = 4096;

    /**
     * 将InputStream转换成byte数组
     * 
     * @param in
     *            InputStream
     * @return byte[]
     * @throws IOException
     */
    private byte[] parseInputStream2Byte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = outStream.toByteArray();
        outStream.close();
        in.close();
        return data;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}
