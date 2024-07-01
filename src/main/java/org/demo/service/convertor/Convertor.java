package org.demo.service.convertor;

public interface Convertor<F, T> {

  T convert(F from);
}
