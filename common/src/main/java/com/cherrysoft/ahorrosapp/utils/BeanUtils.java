package com.cherrysoft.ahorrosapp.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils extends org.springframework.beans.BeanUtils {
  // Helper method used to ignore null properties on BeanUtils.copyProperties.
  // See: https://stackoverflow.com/a/19739041/12591546
  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }

  /**
   * Copy properties from src to target, ignoring properties in src which are null. Works similar to
   * <a src="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/assign">
   * <code>Object.assign</code>
   * </a> in JavaScript.
   *
   * @param src    source object.
   * @param target target object.
   */
  public static void copyProperties(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }
}
