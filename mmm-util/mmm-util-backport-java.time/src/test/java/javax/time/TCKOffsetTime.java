/*
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time;

import static java.time.calendrical.ChronoField.AMPM_OF_DAY;
import static java.time.calendrical.ChronoField.CLOCK_HOUR_OF_AMPM;
import static java.time.calendrical.ChronoField.CLOCK_HOUR_OF_DAY;
import static java.time.calendrical.ChronoField.HOUR_OF_AMPM;
import static java.time.calendrical.ChronoField.HOUR_OF_DAY;
import static java.time.calendrical.ChronoField.MICRO_OF_DAY;
import static java.time.calendrical.ChronoField.MICRO_OF_SECOND;
import static java.time.calendrical.ChronoField.MILLI_OF_DAY;
import static java.time.calendrical.ChronoField.MILLI_OF_SECOND;
import static java.time.calendrical.ChronoField.MINUTE_OF_DAY;
import static java.time.calendrical.ChronoField.MINUTE_OF_HOUR;
import static java.time.calendrical.ChronoField.NANO_OF_DAY;
import static java.time.calendrical.ChronoField.NANO_OF_SECOND;
import static java.time.calendrical.ChronoField.OFFSET_SECONDS;
import static java.time.calendrical.ChronoField.SECOND_OF_DAY;
import static java.time.calendrical.ChronoField.SECOND_OF_MINUTE;
import static java.time.calendrical.ChronoUnit.NANOS;
import static java.time.calendrical.ChronoUnit.SECONDS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.calendrical.ChronoField;
import java.time.calendrical.ChronoUnit;
import java.time.calendrical.DateTime;
import java.time.calendrical.DateTime.MinusAdjuster;
import java.time.calendrical.DateTime.PlusAdjuster;
import java.time.calendrical.DateTime.WithAdjuster;
import java.time.calendrical.DateTimeAccessor;
import java.time.calendrical.DateTimeAccessor.Query;
import java.time.calendrical.DateTimeField;
import java.time.calendrical.JulianDayField;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatters;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.time.calendrical.MockFieldNoValue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test OffsetTime.
 */
@Test
public class TCKOffsetTime extends AbstractDateTimeTest {

  private static final ZoneOffset OFFSET_PONE = ZoneOffset.ofHours(1);

  private static final ZoneOffset OFFSET_PTWO = ZoneOffset.ofHours(2);

  private static final LocalDate DATE = LocalDate.of(2008, 12, 3);

  private OffsetTime TEST_11_30_59_500_PONE;

  @BeforeMethod(groups = { "tck", "implementation" })
  public void setUp() {

    this.TEST_11_30_59_500_PONE = OffsetTime.of(11, 30, 59, 500, OFFSET_PONE);
  }

  // -----------------------------------------------------------------------
  @Override
  protected List<DateTimeAccessor> samples() {

    DateTimeAccessor[] array = { this.TEST_11_30_59_500_PONE, };
    return Arrays.asList(array);
  }

  @Override
  protected List<DateTimeField> validFields() {

    DateTimeField[] array = { NANO_OF_SECOND, NANO_OF_DAY, MICRO_OF_SECOND, MICRO_OF_DAY, MILLI_OF_SECOND,
        MILLI_OF_DAY, SECOND_OF_MINUTE, SECOND_OF_DAY, MINUTE_OF_HOUR, MINUTE_OF_DAY, CLOCK_HOUR_OF_AMPM, HOUR_OF_AMPM,
        CLOCK_HOUR_OF_DAY, HOUR_OF_DAY, AMPM_OF_DAY, OFFSET_SECONDS, };
    return Arrays.asList(array);
  }

  @Override
  protected List<DateTimeField> invalidFields() {

    List<DateTimeField> list = new ArrayList<>(Arrays.<DateTimeField> asList(ChronoField.values()));
    list.removeAll(validFields());
    list.add(JulianDayField.JULIAN_DAY);
    list.add(JulianDayField.MODIFIED_JULIAN_DAY);
    list.add(JulianDayField.RATA_DIE);
    return list;
  }

  @Test(groups = { "tck" })
  public void test_serialization() throws IOException, ClassNotFoundException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(this.TEST_11_30_59_500_PONE);
    oos.close();

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
    assertEquals(ois.readObject(), this.TEST_11_30_59_500_PONE);
  }

  @Test(groups = { "tck" })
  public void test_immutable() {

    Class<OffsetTime> cls = OffsetTime.class;
    assertTrue(Modifier.isPublic(cls.getModifiers()));
    assertTrue(Modifier.isFinal(cls.getModifiers()));
    Field[] fields = cls.getDeclaredFields();
    for (Field field : fields) {
      if (field.getName().contains("$") == false) {
        assertTrue(Modifier.isPrivate(field.getModifiers()));
        assertTrue(Modifier.isFinal(field.getModifiers()));
      }
    }
  }

  // -----------------------------------------------------------------------
  // now()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void now() {

    ZonedDateTime nowDT = ZonedDateTime.now();

    OffsetTime expected = OffsetTime.now(Clock.systemDefaultZone());
    OffsetTime test = OffsetTime.now();
    long diff = Math.abs(test.getTime().toNanoOfDay() - expected.getTime().toNanoOfDay());
    assertTrue(diff < 100000000); // less than 0.1 secs
    assertEquals(test.getOffset(), nowDT.getOffset());
  }

  // -----------------------------------------------------------------------
  // now(Clock)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void now_Clock_allSecsInDay() {

    for (int i = 0; i < (2 * 24 * 60 * 60); i++) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
      OffsetTime test = OffsetTime.now(clock);
      assertEquals(test.getHour(), (i / (60 * 60)) % 24);
      assertEquals(test.getMinute(), (i / 60) % 60);
      assertEquals(test.getSecond(), i % 60);
      assertEquals(test.getNano(), 8);
      assertEquals(test.getOffset(), ZoneOffset.UTC);
    }
  }

  @Test(groups = { "tck" })
  public void now_Clock_beforeEpoch() {

    for (int i = -1; i >= -(24 * 60 * 60); i--) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
      OffsetTime test = OffsetTime.now(clock);
      assertEquals(test.getHour(), ((i + 24 * 60 * 60) / (60 * 60)) % 24);
      assertEquals(test.getMinute(), ((i + 24 * 60 * 60) / 60) % 60);
      assertEquals(test.getSecond(), (i + 24 * 60 * 60) % 60);
      assertEquals(test.getNano(), 8);
      assertEquals(test.getOffset(), ZoneOffset.UTC);
    }
  }

  @Test(groups = { "tck" })
  public void now_Clock_offsets() {

    Instant base = LocalDateTime.of(1970, 1, 1, 12, 0).toInstant(ZoneOffset.UTC);
    for (int i = -9; i < 15; i++) {
      ZoneOffset offset = ZoneOffset.ofHours(i);
      Clock clock = Clock.fixed(base, offset);
      OffsetTime test = OffsetTime.now(clock);
      assertEquals(test.getHour(), (12 + i) % 24);
      assertEquals(test.getMinute(), 0);
      assertEquals(test.getSecond(), 0);
      assertEquals(test.getNano(), 0);
      assertEquals(test.getOffset(), offset);
    }
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void now_Clock_nullZoneId() {

    OffsetTime.now((ZoneId) null);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void now_Clock_nullClock() {

    OffsetTime.now((Clock) null);
  }

  // -----------------------------------------------------------------------
  // factories
  // -----------------------------------------------------------------------
  void check(OffsetTime test, int h, int m, int s, int n, ZoneOffset offset) {

    assertEquals(test.getTime(), LocalTime.of(h, m, s, n));
    assertEquals(test.getOffset(), offset);

    assertEquals(test.getHour(), h);
    assertEquals(test.getMinute(), m);
    assertEquals(test.getSecond(), s);
    assertEquals(test.getNano(), n);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_intsHM() {

    OffsetTime test = OffsetTime.of(11, 30, OFFSET_PONE);
    check(test, 11, 30, 0, 0, OFFSET_PONE);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_intsHMS() {

    OffsetTime test = OffsetTime.of(11, 30, 10, OFFSET_PONE);
    check(test, 11, 30, 10, 0, OFFSET_PONE);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_intsHMSN() {

    OffsetTime test = OffsetTime.of(11, 30, 10, 500, OFFSET_PONE);
    check(test, 11, 30, 10, 500, OFFSET_PONE);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_LocalTimeZoneOffset() {

    LocalTime localTime = LocalTime.of(11, 30, 10, 500);
    OffsetTime test = OffsetTime.of(localTime, OFFSET_PONE);
    check(test, 11, 30, 10, 500, OFFSET_PONE);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_LocalTimeZoneOffset_nullTime() {

    OffsetTime.of((LocalTime) null, OFFSET_PONE);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_LocalTimeZoneOffset_nullOffset() {

    LocalTime localTime = LocalTime.of(11, 30, 10, 500);
    OffsetTime.of(localTime, (ZoneOffset) null);
  }

  // -----------------------------------------------------------------------
  // ofInstant()
  // -----------------------------------------------------------------------
  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_ofInstant_nullInstant() {

    OffsetTime.ofInstant((Instant) null, ZoneOffset.UTC);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_ofInstant_nullOffset() {

    Instant instant = Instant.ofEpochSecond(0L);
    OffsetTime.ofInstant(instant, (ZoneOffset) null);
  }

  @Test(groups = { "tck" })
  public void factory_ofInstant_allSecsInDay() {

    for (int i = 0; i < (2 * 24 * 60 * 60); i++) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      OffsetTime test = OffsetTime.ofInstant(instant, ZoneOffset.UTC);
      assertEquals(test.getHour(), (i / (60 * 60)) % 24);
      assertEquals(test.getMinute(), (i / 60) % 60);
      assertEquals(test.getSecond(), i % 60);
      assertEquals(test.getNano(), 8);
    }
  }

  @Test(groups = { "tck" })
  public void factory_ofInstant_beforeEpoch() {

    for (int i = -1; i >= -(24 * 60 * 60); i--) {
      Instant instant = Instant.ofEpochSecond(i, 8);
      OffsetTime test = OffsetTime.ofInstant(instant, ZoneOffset.UTC);
      assertEquals(test.getHour(), ((i + 24 * 60 * 60) / (60 * 60)) % 24);
      assertEquals(test.getMinute(), ((i + 24 * 60 * 60) / 60) % 60);
      assertEquals(test.getSecond(), (i + 24 * 60 * 60) % 60);
      assertEquals(test.getNano(), 8);
    }
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_ofInstant_maxYear() {

    OffsetTime test = OffsetTime.ofInstant(Instant.ofEpochSecond(Long.MAX_VALUE), ZoneOffset.UTC);
    int hour = (int) ((Long.MAX_VALUE / (60 * 60)) % 24);
    int min = (int) ((Long.MAX_VALUE / 60) % 60);
    int sec = (int) (Long.MAX_VALUE % 60);
    assertEquals(test.getHour(), hour);
    assertEquals(test.getMinute(), min);
    assertEquals(test.getSecond(), sec);
    assertEquals(test.getNano(), 0);
  }

  @Test(groups = { "tck" })
  public void factory_ofInstant_minYear() {

    long oneDay = 24 * 60 * 60;
    long addition = ((Long.MAX_VALUE / oneDay) + 2) * oneDay;

    OffsetTime test = OffsetTime.ofInstant(Instant.ofEpochSecond(Long.MIN_VALUE), ZoneOffset.UTC);
    long added = Long.MIN_VALUE + addition;
    int hour = (int) ((added / (60 * 60)) % 24);
    int min = (int) ((added / 60) % 60);
    int sec = (int) (added % 60);
    assertEquals(test.getHour(), hour);
    assertEquals(test.getMinute(), min);
    assertEquals(test.getSecond(), sec);
    assertEquals(test.getNano(), 0);
  }

  // -----------------------------------------------------------------------
  // from(DateTimeAccessor)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_from_DateTimeAccessor_OT() {

    assertEquals(OffsetTime.from(OffsetTime.of(17, 30, OFFSET_PONE)), OffsetTime.of(17, 30, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_from_DateTimeAccessor_ZDT() {

    ZonedDateTime base = LocalDateTime.of(2007, 7, 15, 11, 30, 59, 500).atZone(OFFSET_PONE);
    assertEquals(OffsetTime.from(base), this.TEST_11_30_59_500_PONE);
  }

  @Test(expectedExceptions = DateTimeException.class, groups = { "tck" })
  public void factory_from_DateTimeAccessor_invalid_noDerive() {

    OffsetTime.from(LocalDate.of(2007, 7, 15));
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_from_DateTimeAccessor_null() {

    OffsetTime.from((DateTimeAccessor) null);
  }

  // -----------------------------------------------------------------------
  // parse()
  // -----------------------------------------------------------------------
  @Test(dataProvider = "sampleToString", groups = { "tck" })
  public void factory_parse_validText(int h, int m, int s, int n, String offsetId, String parsable) {

    OffsetTime t = OffsetTime.parse(parsable);
    assertNotNull(t, parsable);
    check(t, h, m, s, n, ZoneOffset.of(offsetId));
  }

  @DataProvider(name = "sampleBadParse")
  Object[][] provider_sampleBadParse() {

    return new Object[][] { { "00;00" }, { "12-00" }, { "-01:00" }, { "00:00:00-09" }, { "00:00:00,09" },
        { "00:00:abs" }, { "11" }, { "11:30" }, { "11:30+01:00[Europe/Paris]" }, };
  }

  @Test(dataProvider = "sampleBadParse", expectedExceptions = { DateTimeParseException.class }, groups = { "tck" })
  public void factory_parse_invalidText(String unparsable) {

    OffsetTime.parse(unparsable);
  }

  // -----------------------------------------------------------------------s
  @Test(expectedExceptions = { DateTimeParseException.class }, groups = { "tck" })
  public void factory_parse_illegalHour() {

    OffsetTime.parse("25:00+01:00");
  }

  @Test(expectedExceptions = { DateTimeParseException.class }, groups = { "tck" })
  public void factory_parse_illegalMinute() {

    OffsetTime.parse("12:60+01:00");
  }

  @Test(expectedExceptions = { DateTimeParseException.class }, groups = { "tck" })
  public void factory_parse_illegalSecond() {

    OffsetTime.parse("12:12:60+01:00");
  }

  // -----------------------------------------------------------------------
  // parse(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void factory_parse_formatter() {

    DateTimeFormatter f = DateTimeFormatters.pattern("H m s XXX");
    OffsetTime test = OffsetTime.parse("11 30 0 +01:00", f);
    assertEquals(test, OffsetTime.of(11, 30, ZoneOffset.ofHours(1)));
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_parse_formatter_nullText() {

    DateTimeFormatter f = DateTimeFormatters.pattern("y M d H m s");
    OffsetTime.parse((String) null, f);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void factory_parse_formatter_nullFormatter() {

    OffsetTime.parse("ANY", null);
  }

  // -----------------------------------------------------------------------
  // constructor
  // -----------------------------------------------------------------------
  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void constructor_nullTime() throws Throwable {

    Constructor<OffsetTime> con = OffsetTime.class.getDeclaredConstructor(LocalTime.class, ZoneOffset.class);
    con.setAccessible(true);
    try {
      con.newInstance(null, OFFSET_PONE);
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void constructor_nullOffset() throws Throwable {

    Constructor<OffsetTime> con = OffsetTime.class.getDeclaredConstructor(LocalTime.class, ZoneOffset.class);
    con.setAccessible(true);
    try {
      con.newInstance(LocalTime.of(11, 30), null);
    } catch (InvocationTargetException ex) {
      throw ex.getCause();
    }
  }

  // -----------------------------------------------------------------------
  // basics
  // -----------------------------------------------------------------------
  @DataProvider(name = "sampleTimes")
  Object[][] provider_sampleTimes() {

    return new Object[][] { { 11, 30, 20, 500, OFFSET_PONE }, { 11, 0, 0, 0, OFFSET_PONE },
        { 23, 59, 59, 999999999, OFFSET_PONE }, };
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_get(int h, int m, int s, int n, ZoneOffset offset) {

    LocalTime localTime = LocalTime.of(h, m, s, n);
    OffsetTime a = OffsetTime.of(localTime, offset);

    assertEquals(a.getTime(), localTime);
    assertEquals(a.getOffset(), offset);
    assertEquals(a.toString(), localTime.toString() + offset.toString());
    assertEquals(a.getHour(), localTime.getHour());
    assertEquals(a.getMinute(), localTime.getMinute());
    assertEquals(a.getSecond(), localTime.getSecond());
    assertEquals(a.getNano(), localTime.getNano());
  }

  // -----------------------------------------------------------------------
  // get(DateTimeField)
  // -----------------------------------------------------------------------
  @DataProvider(name = "invalidFields")
  Object[][] data_invalidFields() {

    return new Object[][] { { ChronoField.DAY_OF_MONTH }, { ChronoField.YEAR }, { ChronoField.INSTANT_SECONDS },
        { MockFieldNoValue.INSTANCE }, };
  }

  @Test(groups = { "tck" })
  public void test_get_DateTimeField() {

    OffsetTime test = OffsetTime.of(12, 30, 40, 987654321, OFFSET_PONE);
    assertEquals(test.getLong(ChronoField.HOUR_OF_DAY), 12);
    assertEquals(test.getLong(ChronoField.MINUTE_OF_HOUR), 30);
    assertEquals(test.getLong(ChronoField.SECOND_OF_MINUTE), 40);
    assertEquals(test.getLong(ChronoField.NANO_OF_SECOND), 987654321);
    assertEquals(test.getLong(ChronoField.HOUR_OF_AMPM), 0);
    assertEquals(test.getLong(ChronoField.AMPM_OF_DAY), 1);

    assertEquals(test.getLong(ChronoField.OFFSET_SECONDS), 3600);
  }

  @Test(dataProvider = "invalidFields", expectedExceptions = DateTimeException.class, groups = { "tck" })
  public void test_get_DateTimeField_invalidField(DateTimeField field) {

    this.TEST_11_30_59_500_PONE.getLong(field);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_get_DateTimeField_null() {

    this.TEST_11_30_59_500_PONE.getLong((DateTimeField) null);
  }

  // -----------------------------------------------------------------------
  // query(Query)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_query_chrono() {

    assertEquals(this.TEST_11_30_59_500_PONE.query(Query.CHRONO), null);
  }

  @Test(groups = { "tck" })
  public void test_query_zone() {

    assertEquals(this.TEST_11_30_59_500_PONE.query(Query.ZONE_ID), null);
  }

  @Test(groups = { "tck" })
  public void test_query_timePrecision() {

    assertEquals(this.TEST_11_30_59_500_PONE.query(Query.TIME_PRECISION), NANOS);
  }

  @Test(groups = { "tck" })
  public void test_query_offset() {

    assertEquals(this.TEST_11_30_59_500_PONE.query(Query.OFFSET), OFFSET_PONE);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_query_null() {

    this.TEST_11_30_59_500_PONE.query(null);
  }

  // -----------------------------------------------------------------------
  // withOffsetSameLocal()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withOffsetSameLocal() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withOffsetSameLocal(OFFSET_PTWO);
    assertEquals(test.getTime(), base.getTime());
    assertEquals(test.getOffset(), OFFSET_PTWO);
  }

  @Test(groups = { "tck" })
  public void test_withOffsetSameLocal_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withOffsetSameLocal(OFFSET_PONE);
    assertEquals(test, base);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_withOffsetSameLocal_null() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    base.withOffsetSameLocal(null);
  }

  // -----------------------------------------------------------------------
  // withOffsetSameInstant()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withOffsetSameInstant() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withOffsetSameInstant(OFFSET_PTWO);
    OffsetTime expected = OffsetTime.of(12, 30, 59, OFFSET_PTWO);
    assertEquals(test, expected);
  }

  @Test(groups = { "tck" })
  public void test_withOffsetSameInstant_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withOffsetSameInstant(OFFSET_PONE);
    assertEquals(test, base);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_withOffsetSameInstant_null() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    base.withOffsetSameInstant(null);
  }

  // -----------------------------------------------------------------------
  // with(WithAdjuster)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_with_adjustment() {

    final OffsetTime sample = OffsetTime.of(23, 5, OFFSET_PONE);
    WithAdjuster adjuster = new WithAdjuster() {

      @Override
      public DateTime doWithAdjustment(DateTime dateTime) {

        return sample;
      }
    };
    assertEquals(this.TEST_11_30_59_500_PONE.with(adjuster), sample);
  }

  @Test(groups = { "tck" })
  public void test_with_adjustment_LocalTime() {

    OffsetTime test = this.TEST_11_30_59_500_PONE.with(LocalTime.of(13, 30));
    assertEquals(test, OffsetTime.of(13, 30, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_with_adjustment_OffsetTime() {

    OffsetTime test = this.TEST_11_30_59_500_PONE.with(OffsetTime.of(13, 35, OFFSET_PTWO));
    assertEquals(test, OffsetTime.of(13, 35, OFFSET_PTWO));
  }

  @Test(groups = { "tck" })
  public void test_with_adjustment_ZoneOffset() {

    OffsetTime test = this.TEST_11_30_59_500_PONE.with(OFFSET_PTWO);
    assertEquals(test, OffsetTime.of(11, 30, 59, 500, OFFSET_PTWO));
  }

  @Test(groups = { "tck" })
  public void test_with_adjustment_AmPm() {

    OffsetTime test = this.TEST_11_30_59_500_PONE.with(new WithAdjuster() {

      @Override
      public DateTime doWithAdjustment(DateTime dateTime) {

        return dateTime.with(HOUR_OF_DAY, 23);
      }
    });
    assertEquals(test, OffsetTime.of(23, 30, 59, 500, OFFSET_PONE));
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_with_adjustment_null() {

    this.TEST_11_30_59_500_PONE.with((WithAdjuster) null);
  }

  // -----------------------------------------------------------------------
  // with(DateTimeField, long)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_with_DateTimeField() {

    OffsetTime test = OffsetTime.of(12, 30, 40, 987654321, OFFSET_PONE);
    assertEquals(test.with(ChronoField.HOUR_OF_DAY, 15), OffsetTime.of(15, 30, 40, 987654321, OFFSET_PONE));
    assertEquals(test.with(ChronoField.MINUTE_OF_HOUR, 50), OffsetTime.of(12, 50, 40, 987654321, OFFSET_PONE));
    assertEquals(test.with(ChronoField.SECOND_OF_MINUTE, 50), OffsetTime.of(12, 30, 50, 987654321, OFFSET_PONE));
    assertEquals(test.with(ChronoField.NANO_OF_SECOND, 12345), OffsetTime.of(12, 30, 40, 12345, OFFSET_PONE));
    assertEquals(test.with(ChronoField.HOUR_OF_AMPM, 6), OffsetTime.of(18, 30, 40, 987654321, OFFSET_PONE));
    assertEquals(test.with(ChronoField.AMPM_OF_DAY, 0), OffsetTime.of(0, 30, 40, 987654321, OFFSET_PONE));

    assertEquals(test.with(ChronoField.OFFSET_SECONDS, 7205),
        OffsetTime.of(12, 30, 40, 987654321, ZoneOffset.ofHoursMinutesSeconds(2, 0, 5)));
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_with_DateTimeField_null() {

    this.TEST_11_30_59_500_PONE.with((DateTimeField) null, 0);
  }

  @Test(dataProvider = "invalidFields", expectedExceptions = DateTimeException.class, groups = { "tck" })
  public void test_with_DateTimeField_invalidField(DateTimeField field) {

    this.TEST_11_30_59_500_PONE.with(field, 0);
  }

  // -----------------------------------------------------------------------
  // withHour()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withHour_normal() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withHour(15);
    assertEquals(test, OffsetTime.of(15, 30, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_withHour_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withHour(11);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // withMinute()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withMinute_normal() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withMinute(15);
    assertEquals(test, OffsetTime.of(11, 15, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_withMinute_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withMinute(30);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // withSecond()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withSecond_normal() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withSecond(15);
    assertEquals(test, OffsetTime.of(11, 30, 15, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_withSecond_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.withSecond(59);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // withNano()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_withNanoOfSecond_normal() {

    OffsetTime base = OffsetTime.of(11, 30, 59, 1, OFFSET_PONE);
    OffsetTime test = base.withNano(15);
    assertEquals(test, OffsetTime.of(11, 30, 59, 15, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_withNanoOfSecond_noChange() {

    OffsetTime base = OffsetTime.of(11, 30, 59, 1, OFFSET_PONE);
    OffsetTime test = base.withNano(1);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // plus(PlusAdjuster)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_plus_PlusAdjuster() {

    MockSimplePeriod period = MockSimplePeriod.of(7, ChronoUnit.MINUTES);
    OffsetTime t = this.TEST_11_30_59_500_PONE.plus(period);
    assertEquals(t, OffsetTime.of(11, 37, 59, 500, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_plus_PlusAdjuster_noChange() {

    OffsetTime t = this.TEST_11_30_59_500_PONE.plus(MockSimplePeriod.of(0, SECONDS));
    assertEquals(t, this.TEST_11_30_59_500_PONE);
  }

  @Test(groups = { "tck" })
  public void test_plus_PlusAdjuster_zero() {

    OffsetTime t = this.TEST_11_30_59_500_PONE.plus(Period.ZERO);
    assertEquals(t, this.TEST_11_30_59_500_PONE);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_plus_PlusAdjuster_null() {

    this.TEST_11_30_59_500_PONE.plus((PlusAdjuster) null);
  }

  // -----------------------------------------------------------------------
  // plusHours()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_plusHours() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusHours(13);
    assertEquals(test, OffsetTime.of(0, 30, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_plusHours_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusHours(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // plusMinutes()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_plusMinutes() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusMinutes(30);
    assertEquals(test, OffsetTime.of(12, 0, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_plusMinutes_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusMinutes(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // plusSeconds()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_plusSeconds() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusSeconds(1);
    assertEquals(test, OffsetTime.of(11, 31, 0, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_plusSeconds_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusSeconds(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // plusNanos()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_plusNanos() {

    OffsetTime base = OffsetTime.of(11, 30, 59, 0, OFFSET_PONE);
    OffsetTime test = base.plusNanos(1);
    assertEquals(test, OffsetTime.of(11, 30, 59, 1, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_plusNanos_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.plusNanos(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // minus(MinusAdjuster)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_minus_MinusAdjuster() {

    MockSimplePeriod period = MockSimplePeriod.of(7, ChronoUnit.MINUTES);
    OffsetTime t = this.TEST_11_30_59_500_PONE.minus(period);
    assertEquals(t, OffsetTime.of(11, 23, 59, 500, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_minus_MinusAdjuster_noChange() {

    OffsetTime t = this.TEST_11_30_59_500_PONE.minus(MockSimplePeriod.of(0, SECONDS));
    assertEquals(t, this.TEST_11_30_59_500_PONE);
  }

  @Test(groups = { "tck" })
  public void test_minus_MinusAdjuster_zero() {

    OffsetTime t = this.TEST_11_30_59_500_PONE.minus(Period.ZERO);
    assertEquals(t, this.TEST_11_30_59_500_PONE);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_minus_MinusAdjuster_null() {

    this.TEST_11_30_59_500_PONE.minus((MinusAdjuster) null);
  }

  // -----------------------------------------------------------------------
  // minusHours()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_minusHours() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusHours(-13);
    assertEquals(test, OffsetTime.of(0, 30, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_minusHours_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusHours(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // minusMinutes()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_minusMinutes() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusMinutes(50);
    assertEquals(test, OffsetTime.of(10, 40, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_minusMinutes_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusMinutes(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // minusSeconds()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_minusSeconds() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusSeconds(60);
    assertEquals(test, OffsetTime.of(11, 29, 59, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_minusSeconds_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusSeconds(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // minusNanos()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_minusNanos() {

    OffsetTime base = OffsetTime.of(11, 30, 59, 0, OFFSET_PONE);
    OffsetTime test = base.minusNanos(1);
    assertEquals(test, OffsetTime.of(11, 30, 58, 999999999, OFFSET_PONE));
  }

  @Test(groups = { "tck" })
  public void test_minusNanos_zero() {

    OffsetTime base = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    OffsetTime test = base.minusNanos(0);
    assertEquals(test, base);
  }

  // -----------------------------------------------------------------------
  // compareTo()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_compareTo_time() {

    OffsetTime a = OffsetTime.of(11, 29, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(11, 30, OFFSET_PONE); // a is before b due to time
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(convertInstant(a).compareTo(convertInstant(b)) < 0, true);
  }

  @Test(groups = { "tck" })
  public void test_compareTo_offset() {

    OffsetTime a = OffsetTime.of(11, 30, OFFSET_PTWO);
    OffsetTime b = OffsetTime.of(11, 30, OFFSET_PONE); // a is before b due to offset
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(convertInstant(a).compareTo(convertInstant(b)) < 0, true);
  }

  @Test(groups = { "tck" })
  public void test_compareTo_both() {

    OffsetTime a = OffsetTime.of(11, 50, OFFSET_PTWO);
    OffsetTime b = OffsetTime.of(11, 20, OFFSET_PONE); // a is before b on instant scale
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(convertInstant(a).compareTo(convertInstant(b)) < 0, true);
  }

  @Test(groups = { "tck" })
  public void test_compareTo_bothNearStartOfDay() {

    OffsetTime a = OffsetTime.of(0, 10, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(2, 30, OFFSET_PTWO); // a is before b on instant scale
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(convertInstant(a).compareTo(convertInstant(b)) < 0, true);
  }

  @Test(groups = { "tck" })
  public void test_compareTo_hourDifference() {

    OffsetTime a = OffsetTime.of(10, 0, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(11, 0, OFFSET_PTWO); // a is before b despite being same time-line time
    assertEquals(a.compareTo(b) < 0, true);
    assertEquals(b.compareTo(a) > 0, true);
    assertEquals(a.compareTo(a) == 0, true);
    assertEquals(b.compareTo(b) == 0, true);
    assertEquals(convertInstant(a).compareTo(convertInstant(b)) == 0, true);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_compareTo_null() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    a.compareTo(null);
  }

  @Test(expectedExceptions = ClassCastException.class, groups = { "tck" })
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void compareToNonOffsetTime() {

    Comparable c = this.TEST_11_30_59_500_PONE;
    c.compareTo(new Object());
  }

  private Instant convertInstant(OffsetTime ot) {

    return DATE.atTime(ot.getTime()).toInstant(ot.getOffset());
  }

  // -----------------------------------------------------------------------
  // isAfter() / isBefore() / isEqual()
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_isBeforeIsAfterIsEqual1() {

    OffsetTime a = OffsetTime.of(11, 30, 58, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(11, 30, 59, OFFSET_PONE); // a is before b due to time
    assertEquals(a.isBefore(b), true);
    assertEquals(a.isEqual(b), false);
    assertEquals(a.isAfter(b), false);

    assertEquals(b.isBefore(a), false);
    assertEquals(b.isEqual(a), false);
    assertEquals(b.isAfter(a), true);

    assertEquals(a.isBefore(a), false);
    assertEquals(b.isBefore(b), false);

    assertEquals(a.isEqual(a), true);
    assertEquals(b.isEqual(b), true);

    assertEquals(a.isAfter(a), false);
    assertEquals(b.isAfter(b), false);
  }

  @Test(groups = { "tck" })
  public void test_isBeforeIsAfterIsEqual1nanos() {

    OffsetTime a = OffsetTime.of(11, 30, 59, 3, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(11, 30, 59, 4, OFFSET_PONE); // a is before b due to time
    assertEquals(a.isBefore(b), true);
    assertEquals(a.isEqual(b), false);
    assertEquals(a.isAfter(b), false);

    assertEquals(b.isBefore(a), false);
    assertEquals(b.isEqual(a), false);
    assertEquals(b.isAfter(a), true);

    assertEquals(a.isBefore(a), false);
    assertEquals(b.isBefore(b), false);

    assertEquals(a.isEqual(a), true);
    assertEquals(b.isEqual(b), true);

    assertEquals(a.isAfter(a), false);
    assertEquals(b.isAfter(b), false);
  }

  @Test(groups = { "tck" })
  public void test_isBeforeIsAfterIsEqual2() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PTWO);
    OffsetTime b = OffsetTime.of(11, 30, 58, OFFSET_PONE); // a is before b due to offset
    assertEquals(a.isBefore(b), true);
    assertEquals(a.isEqual(b), false);
    assertEquals(a.isAfter(b), false);

    assertEquals(b.isBefore(a), false);
    assertEquals(b.isEqual(a), false);
    assertEquals(b.isAfter(a), true);

    assertEquals(a.isBefore(a), false);
    assertEquals(b.isBefore(b), false);

    assertEquals(a.isEqual(a), true);
    assertEquals(b.isEqual(b), true);

    assertEquals(a.isAfter(a), false);
    assertEquals(b.isAfter(b), false);
  }

  @Test(groups = { "tck" })
  public void test_isBeforeIsAfterIsEqual2nanos() {

    OffsetTime a = OffsetTime.of(11, 30, 59, 4, ZoneOffset.ofTotalSeconds(OFFSET_PONE.getTotalSeconds() + 1));
    OffsetTime b = OffsetTime.of(11, 30, 59, 3, OFFSET_PONE); // a is before b due to offset
    assertEquals(a.isBefore(b), true);
    assertEquals(a.isEqual(b), false);
    assertEquals(a.isAfter(b), false);

    assertEquals(b.isBefore(a), false);
    assertEquals(b.isEqual(a), false);
    assertEquals(b.isAfter(a), true);

    assertEquals(a.isBefore(a), false);
    assertEquals(b.isBefore(b), false);

    assertEquals(a.isEqual(a), true);
    assertEquals(b.isEqual(b), true);

    assertEquals(a.isAfter(a), false);
    assertEquals(b.isAfter(b), false);
  }

  @Test(groups = { "tck" })
  public void test_isBeforeIsAfterIsEqual_instantComparison() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PTWO);
    OffsetTime b = OffsetTime.of(10, 30, 59, OFFSET_PONE); // a is same instant as b
    assertEquals(a.isBefore(b), false);
    assertEquals(a.isEqual(b), true);
    assertEquals(a.isAfter(b), false);

    assertEquals(b.isBefore(a), false);
    assertEquals(b.isEqual(a), true);
    assertEquals(b.isAfter(a), false);

    assertEquals(a.isBefore(a), false);
    assertEquals(b.isBefore(b), false);

    assertEquals(a.isEqual(a), true);
    assertEquals(b.isEqual(b), true);

    assertEquals(a.isAfter(a), false);
    assertEquals(b.isAfter(b), false);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_isBefore_null() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    a.isBefore(null);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_isAfter_null() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    a.isAfter(null);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_isEqual_null() {

    OffsetTime a = OffsetTime.of(11, 30, 59, OFFSET_PONE);
    a.isEqual(null);
  }

  // -----------------------------------------------------------------------
  // equals() / hashCode()
  // -----------------------------------------------------------------------
  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_true(int h, int m, int s, int n, ZoneOffset ignored) {

    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    assertEquals(a.equals(b), true);
    assertEquals(a.hashCode() == b.hashCode(), true);
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_false_hour_differs(int h, int m, int s, int n, ZoneOffset ignored) {

    h = (h == 23 ? 22 : h);
    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h + 1, m, s, n, OFFSET_PONE);
    assertEquals(a.equals(b), false);
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_false_minute_differs(int h, int m, int s, int n, ZoneOffset ignored) {

    m = (m == 59 ? 58 : m);
    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h, m + 1, s, n, OFFSET_PONE);
    assertEquals(a.equals(b), false);
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_false_second_differs(int h, int m, int s, int n, ZoneOffset ignored) {

    s = (s == 59 ? 58 : s);
    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h, m, s + 1, n, OFFSET_PONE);
    assertEquals(a.equals(b), false);
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_false_nano_differs(int h, int m, int s, int n, ZoneOffset ignored) {

    n = (n == 999999999 ? 999999998 : n);
    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h, m, s, n + 1, OFFSET_PONE);
    assertEquals(a.equals(b), false);
  }

  @Test(dataProvider = "sampleTimes", groups = { "tck" })
  public void test_equals_false_offset_differs(int h, int m, int s, int n, ZoneOffset ignored) {

    OffsetTime a = OffsetTime.of(h, m, s, n, OFFSET_PONE);
    OffsetTime b = OffsetTime.of(h, m, s, n, OFFSET_PTWO);
    assertEquals(a.equals(b), false);
  }

  @Test(groups = { "tck" })
  public void test_equals_itself_true() {

    assertEquals(this.TEST_11_30_59_500_PONE.equals(this.TEST_11_30_59_500_PONE), true);
  }

  @Test(groups = { "tck" })
  public void test_equals_string_false() {

    assertEquals(this.TEST_11_30_59_500_PONE.equals("2007-07-15"), false);
  }

  @Test(groups = { "tck" })
  public void test_equals_null_false() {

    assertEquals(this.TEST_11_30_59_500_PONE.equals(null), false);
  }

  // -----------------------------------------------------------------------
  // toString()
  // -----------------------------------------------------------------------
  @DataProvider(name = "sampleToString")
  Object[][] provider_sampleToString() {

    return new Object[][] { { 11, 30, 59, 0, "Z", "11:30:59Z" }, { 11, 30, 59, 0, "+01:00", "11:30:59+01:00" },
        { 11, 30, 59, 999000000, "Z", "11:30:59.999Z" }, { 11, 30, 59, 999000000, "+01:00", "11:30:59.999+01:00" },
        { 11, 30, 59, 999000, "Z", "11:30:59.000999Z" }, { 11, 30, 59, 999000, "+01:00", "11:30:59.000999+01:00" },
        { 11, 30, 59, 999, "Z", "11:30:59.000000999Z" }, { 11, 30, 59, 999, "+01:00", "11:30:59.000000999+01:00" }, };
  }

  @Test(dataProvider = "sampleToString", groups = { "tck" })
  public void test_toString(int h, int m, int s, int n, String offsetId, String expected) {

    OffsetTime t = OffsetTime.of(h, m, s, n, ZoneOffset.of(offsetId));
    String str = t.toString();
    assertEquals(str, expected);
  }

  // -----------------------------------------------------------------------
  // toString(DateTimeFormatter)
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_toString_formatter() {

    DateTimeFormatter f = DateTimeFormatters.pattern("H m s");
    String t = OffsetTime.of(11, 30, OFFSET_PONE).toString(f);
    assertEquals(t, "11 30 0");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_toString_formatter_null() {

    OffsetTime.of(11, 30, OFFSET_PONE).toString(null);
  }

}
