package com.zoctan.api.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 请求装饰器，用于多次读取请求流
 *
 * @author Zoctan
 * @date 2018/07/13
 */
public class RequestWrapper extends HttpServletRequestWrapper {
  private final StringBuilder body;

  public RequestWrapper(final HttpServletRequest request) throws IOException {
    super(request);
    this.body = new StringBuilder();
    final BufferedReader bufferedReader = request.getReader();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      this.body.append(line);
    }
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(this.body.toString().getBytes());
    return new ServletInputStream() {
      @Override
      public int read() {
        return byteArrayInputStream.read();
      }

      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(final ReadListener readListener) {}
    };
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  public String getJson() {
    return this.getReader()
        .lines()
        .sequential()
        .reduce(System.lineSeparator(), (accumulator, actual) -> accumulator + actual);
  }
}
