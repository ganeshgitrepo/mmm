/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.value.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import net.sf.mmm.util.date.Iso8601Util;
import net.sf.mmm.util.value.api.ComposedValueConverter;
import net.sf.mmm.util.value.base.AbstractValueConverter;

/**
 * This is the test-case for {@link ComposedValueConverterImpl}.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@SuppressWarnings("all")
public class ComposedValueConverterImplTest {

  @Test
  public void testConverter() throws Exception {

    ComposedValueConverterImpl converter = new ComposedValueConverterImpl();
    converter.addConverter(new ValueConverterToDate());
    converter.addConverter(new ValueConverterToCalendar());
    converter.addConverter(new ValueConverterToNumber());
    converter.addConverter(new ValueConverterToString());
    converter.initialize();
    assertEquals(Object.class, converter.getSourceType());
    assertEquals(Object.class, converter.getTargetType());

    Object value;
    String valueSource = "test-case";
    // convert to integer
    Integer i = Integer.valueOf(1234);
    value = converter.convert(i.toString(), valueSource, Integer.class);
    assertEquals(i, value);
    value = converter.convert(Long.valueOf(i.intValue()), valueSource, Integer.class);
    assertEquals(i, value);
    // convert to date
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    Date date = calendar.getTime();
    value = converter.convert(calendar, valueSource, Date.class);
    assertEquals(date, value);
    String dateString = Iso8601Util.getInstance().formatDateTime(date);
    value = converter.convert(dateString, valueSource, Date.class);
    assertEquals(date, value);
    value = converter.convert(Long.valueOf(date.getTime()), valueSource, Date.class);
    assertEquals(date, value);
    // convert to calendar
    value = converter.convert(date, valueSource, Calendar.class);
    assertEquals(calendar, value);
    String calendarString = Iso8601Util.getInstance().formatDateTime(calendar);
    value = converter.convert(calendarString, valueSource, Calendar.class);
    assertEquals(calendar.getTime(), ((Calendar) value).getTime());
    // convert to string
    value = converter.convert(date, valueSource, String.class);
    assertEquals(dateString, value);
    value = converter.convert(String.class, valueSource, String.class);
    assertEquals(String.class.getName(), value);
  }

  @Test
  public void testSelection() throws Exception {

    ComposedValueConverterImpl converter = new ComposedValueConverterImpl();
    converter.addConverter(new ValueConverterToNumber());
    converter.addConverter(new ValueConverterToInteger());
    converter.addConverter(new ValueConverterFooToObject());
    converter.initialize();
    checkSelection(converter);

    converter = new ComposedValueConverterImpl();
    converter.addConverter(new ValueConverterToInteger());
    converter.addConverter(new ValueConverterToNumber());
    converter.addConverter(new ValueConverterFooToObject());
    converter.initialize();
    checkSelection(converter);

    converter = new ComposedValueConverterImpl();
    converter.addConverter(new ValueConverterFooToObject());
    converter.addConverter(new ValueConverterToInteger());
    converter.addConverter(new ValueConverterToNumber());
    converter.initialize();
    checkSelection(converter);
  }

  protected void checkSelection(ComposedValueConverter converter) {

    Object value;
    String valueSource = "test-case";
    value = converter.convert("foo", valueSource, Integer.class);
    assertSame(ValueConverterToInteger.MAGIC, value);
    Long l = Long.valueOf(1234567890123456789L);
    value = converter.convert(l.toString(), valueSource, Long.class);
    assertEquals(l, value);
    value = converter.convert(new Foo(), valueSource, Integer.class);
    assertSame(ValueConverterFooToObject.MAGIC, value);
  }

  private static class ValueConverterToInteger extends AbstractValueConverter<Object, Integer> {

    public static final Integer MAGIC = Integer.valueOf(4242);

    public Class<Object> getSourceType() {

      return Object.class;
    }

    public Class<Integer> getTargetType() {

      return Integer.class;
    }

    public Integer convert(Object value, Object valueSource, Class<? extends Integer> targetClass,
        Type targetType) {

      if ((value != null) && (value instanceof String)) {
        return MAGIC;
      }
      return null;
    }

  }

  private static class Foo {

  }

  private static class ValueConverterFooToObject extends AbstractValueConverter<Foo, Object> {

    public static final Integer MAGIC = Integer.valueOf(42);

    /**
     * {@inheritDoc}
     */
    public Class<Foo> getSourceType() {

      return Foo.class;
    }

    /**
     * {@inheritDoc}
     */
    public Class<Object> getTargetType() {

      return Object.class;
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(Foo value, Object valueSource, Class<? extends Object> targetClass,
        Type targetType) {

      if ((value != null) && (Integer.class.equals(targetClass))) {
        return MAGIC;
      }
      return null;
    }
  }
}
