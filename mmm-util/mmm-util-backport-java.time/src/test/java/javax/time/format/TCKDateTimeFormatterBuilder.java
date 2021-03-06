/*
 * Copyright (c) 2009-2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.format;

import static java.time.calendrical.ChronoField.DAY_OF_MONTH;
import static java.time.calendrical.ChronoField.DAY_OF_WEEK;
import static java.time.calendrical.ChronoField.MINUTE_OF_HOUR;
import static java.time.calendrical.ChronoField.MONTH_OF_YEAR;
import static java.time.calendrical.ChronoField.YEAR;
import static org.testng.Assert.assertEquals;

import java.text.ParsePosition;
import java.time.calendrical.DateTimeBuilder;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test DateTimeFormatterBuilder.
 */
@Test
public class TCKDateTimeFormatterBuilder {

  private DateTimeFormatterBuilder builder;

  @BeforeMethod(groups = { "tck" })
  public void setUp() {

    this.builder = new DateTimeFormatterBuilder();
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_toFormatter_empty() throws Exception {

    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "");
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_parseCaseSensitive() throws Exception {

    this.builder.parseCaseSensitive();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ParseCaseSensitive(true)");
  }

  @Test(groups = { "tck" })
  public void test_parseCaseInsensitive() throws Exception {

    this.builder.parseCaseInsensitive();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ParseCaseSensitive(false)");
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_parseStrict() throws Exception {

    this.builder.parseStrict();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ParseStrict(true)");
  }

  @Test(groups = { "tck" })
  public void test_parseLenient() throws Exception {

    this.builder.parseLenient();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ParseStrict(false)");
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendValue_1arg() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendValue_1arg_null() throws Exception {

    this.builder.appendValue(null);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendValue_2arg() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 3);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth,3)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendValue_2arg_null() throws Exception {

    this.builder.appendValue(null, 3);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_2arg_widthTooSmall() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_2arg_widthTooBig() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 20);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendValue_3arg() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 2, 3, SignStyle.NORMAL);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(DayOfMonth,2,3,NORMAL)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendValue_3arg_nullField() throws Exception {

    this.builder.appendValue(null, 2, 3, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_3arg_minWidthTooSmall() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 0, 2, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_3arg_minWidthTooBig() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 20, 2, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_3arg_maxWidthTooSmall() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 2, 0, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_3arg_maxWidthTooBig() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 2, 20, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendValue_3arg_maxWidthMinWidth() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 4, 2, SignStyle.NORMAL);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendValue_3arg_nullSignStyle() throws Exception {

    this.builder.appendValue(DAY_OF_MONTH, 2, 3, null);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendValue_subsequent2_parse3() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)");
    DateTimeBuilder cal = f.parseToBuilder("123", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(MONTH_OF_YEAR), Long.valueOf(1));
    assertEquals(cal.getFieldValueMap().get(DAY_OF_MONTH), Long.valueOf(23));
  }

  @Test(groups = { "tck" })
  public void test_appendValue_subsequent2_parse4() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)");
    DateTimeBuilder cal = f.parseToBuilder("0123", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(MONTH_OF_YEAR), Long.valueOf(1));
    assertEquals(cal.getFieldValueMap().get(DAY_OF_MONTH), Long.valueOf(23));
  }

  @Test(groups = { "tck" })
  public void test_appendValue_subsequent2_parse5() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValue(DAY_OF_MONTH, 2).appendLiteral('4');
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)Value(DayOfMonth,2)'4'");
    DateTimeBuilder cal = f.parseToBuilder("01234", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(MONTH_OF_YEAR), Long.valueOf(1));
    assertEquals(cal.getFieldValueMap().get(DAY_OF_MONTH), Long.valueOf(23));
  }

  @Test(groups = { "tck" })
  public void test_appendValue_subsequent3_parse6() throws Exception {

    this.builder.appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendValue(MONTH_OF_YEAR, 2)
        .appendValue(DAY_OF_MONTH, 2);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(Year,4,10,EXCEEDS_PAD)Value(MonthOfYear,2)Value(DayOfMonth,2)");
    DateTimeBuilder cal = f.parseToBuilder("20090630", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(YEAR), Long.valueOf(2009));
    assertEquals(cal.getFieldValueMap().get(MONTH_OF_YEAR), Long.valueOf(6));
    assertEquals(cal.getFieldValueMap().get(DAY_OF_MONTH), Long.valueOf(30));
  }

  // -----------------------------------------------------------------------
  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendValueReduced_null() throws Exception {

    this.builder.appendValueReduced(null, 2, 2000);
  }

  @Test(groups = { "tck" })
  public void test_appendValueReduced() throws Exception {

    this.builder.appendValueReduced(YEAR, 2, 2000);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ReducedValue(Year,2,2000)");
    DateTimeBuilder cal = f.parseToBuilder("12", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(YEAR), Long.valueOf(2012));
  }

  @Test(groups = { "tck" })
  public void test_appendValueReduced_subsequent_parse() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL).appendValueReduced(YEAR, 2, 2000);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear,1,2,NORMAL)ReducedValue(Year,2,2000)");
    DateTimeBuilder cal = f.parseToBuilder("123", new ParsePosition(0));
    assertEquals(cal.getFieldValueMap().get(MONTH_OF_YEAR), Long.valueOf(1));
    assertEquals(cal.getFieldValueMap().get(YEAR), Long.valueOf(2023));
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendFraction_4arg() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, 1, 9, false);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Fraction(MinuteOfHour,1,9)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendFraction_4arg_nullRule() throws Exception {

    this.builder.appendFraction(null, 1, 9, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_invalidRuleNotFixedSet() throws Exception {

    this.builder.appendFraction(DAY_OF_MONTH, 1, 9, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_minTooSmall() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, -1, 9, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_minTooBig() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, 10, 9, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_maxTooSmall() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, 0, -1, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_maxTooBig() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, 1, 10, false);
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendFraction_4arg_maxWidthMinWidth() throws Exception {

    this.builder.appendFraction(MINUTE_OF_HOUR, 9, 3, false);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendText_1arg() throws Exception {

    this.builder.appendText(MONTH_OF_YEAR);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendText_1arg_null() throws Exception {

    this.builder.appendText(null);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendText_2arg() throws Exception {

    this.builder.appendText(MONTH_OF_YEAR, TextStyle.SHORT);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear,SHORT)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendText_2arg_nullRule() throws Exception {

    this.builder.appendText(null, TextStyle.SHORT);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendText_2arg_nullStyle() throws Exception {

    this.builder.appendText(MONTH_OF_YEAR, (TextStyle) null);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendTextMap() throws Exception {

    Map<Long, String> map = new HashMap<Long, String>();
    map.put(1L, "JNY");
    map.put(2L, "FBY");
    map.put(3L, "MCH");
    map.put(4L, "APL");
    map.put(5L, "MAY");
    map.put(6L, "JUN");
    map.put(7L, "JLY");
    map.put(8L, "AGT");
    map.put(9L, "SPT");
    map.put(10L, "OBR");
    map.put(11L, "NVR");
    map.put(12L, "DBR");
    this.builder.appendText(MONTH_OF_YEAR, map);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Text(MonthOfYear)"); // TODO: toString should be different?
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendTextMap_nullRule() throws Exception {

    this.builder.appendText(null, new HashMap<Long, String>());
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendTextMap_nullStyle() throws Exception {

    this.builder.appendText(MONTH_OF_YEAR, (Map<Long, String>) null);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendOffsetId() throws Exception {

    this.builder.appendOffsetId();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Offset('Z',+HH:MM:ss)");
  }

  @DataProvider(name = "offsetPatterns")
  Object[][] data_offsetPatterns() {

    return new Object[][] { { "+HH" }, { "+HHMM" }, { "+HH:MM" }, { "+HHMMss" }, { "+HH:MM:ss" }, { "+HHMMSS" },
        { "+HH:MM:SS" }, };
  }

  @Test(dataProvider = "offsetPatterns", groups = { "tck" })
  public void test_appendOffset(String pattern) throws Exception {

    this.builder.appendOffset(pattern, "Z");
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Offset('Z'," + pattern + ")");
  }

  @DataProvider(name = "badOffsetPatterns")
  Object[][] data_badOffsetPatterns() {

    return new Object[][] { { "HH" }, { "HHMM" }, { "HH:MM" }, { "HHMMss" }, { "HH:MM:ss" }, { "HHMMSS" },
        { "HH:MM:SS" }, { "+H" }, { "+HMM" }, { "+HHM" }, { "+A" }, };
  }

  @Test(dataProvider = "badOffsetPatterns", expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendOffset_badPattern(String pattern) throws Exception {

    this.builder.appendOffset(pattern, "Z");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendOffset_3arg_nullText() throws Exception {

    this.builder.appendOffset("+HH:MM", null);
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendOffset_3arg_nullPattern() throws Exception {

    this.builder.appendOffset(null, "Z");
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_appendZoneId() throws Exception {

    this.builder.appendZoneId();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ZoneId()");
  }

  @Test(groups = { "tck" })
  public void test_appendZoneText_1arg() throws Exception {

    this.builder.appendZoneText(TextStyle.FULL);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "ZoneText(FULL)");
  }

  @Test(expectedExceptions = NullPointerException.class, groups = { "tck" })
  public void test_appendZoneText_1arg_nullText() throws Exception {

    this.builder.appendZoneText(null);
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_padNext_1arg() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).padNext(2).appendValue(DAY_OF_MONTH).appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad(Value(DayOfMonth),2)Value(DayOfWeek)");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_padNext_1arg_invalidWidth() throws Exception {

    this.builder.padNext(0);
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_padNext_2arg_dash() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).padNext(2, '-').appendValue(DAY_OF_MONTH).appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad(Value(DayOfMonth),2,'-')Value(DayOfWeek)");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_padNext_2arg_invalidWidth() throws Exception {

    this.builder.padNext(0, '-');
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_padOptional() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).padNext(5).optionalStart().appendValue(DAY_OF_MONTH).optionalEnd()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Pad([Value(DayOfMonth)],5)Value(DayOfWeek)");
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_optionalStart_noEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().appendValue(DAY_OF_MONTH).appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)Value(DayOfWeek)]");
  }

  @Test(groups = { "tck" })
  public void test_optionalStart2_noEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().appendValue(DAY_OF_MONTH).optionalStart()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)[Value(DayOfWeek)]]");
  }

  @Test(groups = { "tck" })
  public void test_optionalStart_doubleStart() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalStart().appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  // -----------------------------------------------------------------------
  @Test(groups = { "tck" })
  public void test_optionalEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().appendValue(DAY_OF_MONTH).optionalEnd()
        .appendValue(DAY_OF_WEEK);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)]Value(DayOfWeek)");
  }

  @Test(groups = { "tck" })
  public void test_optionalEnd2() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().appendValue(DAY_OF_MONTH).optionalStart()
        .appendValue(DAY_OF_WEEK).optionalEnd().appendValue(DAY_OF_MONTH).optionalEnd();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[Value(DayOfMonth)[Value(DayOfWeek)]Value(DayOfMonth)]");
  }

  @Test(groups = { "tck" })
  public void test_optionalEnd_doubleStartSingleEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalStart().appendValue(DAY_OF_MONTH).optionalEnd();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  @Test(groups = { "tck" })
  public void test_optionalEnd_doubleStartDoubleEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalStart().appendValue(DAY_OF_MONTH).optionalEnd()
        .optionalEnd();
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)[[Value(DayOfMonth)]]");
  }

  @Test(groups = { "tck" })
  public void test_optionalStartEnd_immediateStartEnd() throws Exception {

    this.builder.appendValue(MONTH_OF_YEAR).optionalStart().optionalEnd().appendValue(DAY_OF_MONTH);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), "Value(MonthOfYear)Value(DayOfMonth)");
  }

  @Test(expectedExceptions = IllegalStateException.class, groups = { "tck" })
  public void test_optionalEnd_noStart() throws Exception {

    this.builder.optionalEnd();
  }

  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  // -----------------------------------------------------------------------
  @DataProvider(name = "validPatterns")
  Object[][] dataValid() {

    return new Object[][] {
        { "'a'", "'a'" },
        { "''", "''" },
        { "'!'", "'!'" },
        { "!", "'!'" },

        { "'hello_people,][)('", "'hello_people,][)('" },
        { "'hi'", "'hi'" },
        { "'yyyy'", "'yyyy'" },
        { "''''", "''" },
        { "'o''clock'", "'o''clock'" },

        { "G", "Value(Era)" },
        { "GG", "Value(Era,2)" },
        { "GGG", "Text(Era,SHORT)" },
        { "GGGG", "Text(Era)" },
        { "GGGGG", "Text(Era,NARROW)" },

        { "y", "Value(Year)" },
        { "yy", "ReducedValue(Year,2,2000)" },
        { "yyy", "Value(Year,3,19,NORMAL)" },
        { "yyyy", "Value(Year,4,19,EXCEEDS_PAD)" },
        { "yyyyy", "Value(Year,5,19,EXCEEDS_PAD)" },

        // {"Y", "Value(WeekBasedYear)"},
        // {"YY", "ReducedValue(WeekBasedYear,2,2000)"},
        // {"YYY", "Value(WeekBasedYear,3,19,NORMAL)"},
        // {"YYYY", "Value(WeekBasedYear,4,19,EXCEEDS_PAD)"},
        // {"YYYYY", "Value(WeekBasedYear,5,19,EXCEEDS_PAD)"},

        { "M", "Value(MonthOfYear)" },
        { "MM", "Value(MonthOfYear,2)" },
        { "MMM", "Text(MonthOfYear,SHORT)" },
        { "MMMM", "Text(MonthOfYear)" },
        { "MMMMM", "Text(MonthOfYear,NARROW)" },

        // {"w", "Value(WeekOfWeekBasedYear)"},
        // {"ww", "Value(WeekOfWeekBasedYear,2)"},
        // {"www", "Value(WeekOfWeekBasedYear,3)"},

        { "D", "Value(DayOfYear)" }, { "DD", "Value(DayOfYear,2)" },
        { "DDD", "Value(DayOfYear,3)" },

        { "d", "Value(DayOfMonth)" },
        { "dd", "Value(DayOfMonth,2)" },
        { "ddd", "Value(DayOfMonth,3)" },

        { "F", "Value(AlignedWeekOfMonth)" },
        { "FF", "Value(AlignedWeekOfMonth,2)" },
        { "FFF", "Value(AlignedWeekOfMonth,3)" },

        { "E", "Value(DayOfWeek)" },
        { "EE", "Value(DayOfWeek,2)" },
        { "EEE", "Text(DayOfWeek,SHORT)" },
        { "EEEE", "Text(DayOfWeek)" },
        { "EEEEE", "Text(DayOfWeek,NARROW)" },

        { "a", "Text(AmPmOfDay,SHORT)" },
        { "aa", "Text(AmPmOfDay,SHORT)" },
        { "aaa", "Text(AmPmOfDay,SHORT)" },
        { "aaaa", "Text(AmPmOfDay)" },
        { "aaaaa", "Text(AmPmOfDay,NARROW)" },

        { "H", "Value(HourOfDay)" },
        { "HH", "Value(HourOfDay,2)" },
        { "HHH", "Value(HourOfDay,3)" },

        { "K", "Value(HourOfAmPm)" },
        { "KK", "Value(HourOfAmPm,2)" },
        { "KKK", "Value(HourOfAmPm,3)" },

        { "k", "Value(ClockHourOfDay)" },
        { "kk", "Value(ClockHourOfDay,2)" },
        { "kkk", "Value(ClockHourOfDay,3)" },

        { "h", "Value(ClockHourOfAmPm)" },
        { "hh", "Value(ClockHourOfAmPm,2)" },
        { "hhh", "Value(ClockHourOfAmPm,3)" },

        { "m", "Value(MinuteOfHour)" },
        { "mm", "Value(MinuteOfHour,2)" },
        { "mmm", "Value(MinuteOfHour,3)" },

        { "s", "Value(SecondOfMinute)" },
        { "ss", "Value(SecondOfMinute,2)" },
        { "sss", "Value(SecondOfMinute,3)" },

        { "S", "Fraction(NanoOfSecond,1,1)" },
        { "SS", "Fraction(NanoOfSecond,2,2)" },
        { "SSS", "Fraction(NanoOfSecond,3,3)" },
        { "SSSSSSSSS", "Fraction(NanoOfSecond,9,9)" },

        { "A", "Value(MilliOfDay)" },
        { "AA", "Value(MilliOfDay,2)" },
        { "AAA", "Value(MilliOfDay,3)" },

        { "n", "Value(NanoOfSecond)" },
        { "nn", "Value(NanoOfSecond,2)" },
        { "nnn", "Value(NanoOfSecond,3)" },

        { "N", "Value(NanoOfDay)" },
        { "NN", "Value(NanoOfDay,2)" },
        { "NNN", "Value(NanoOfDay,3)" },

        { "z", "ZoneText(SHORT)" },
        { "zz", "ZoneText(SHORT)" },
        { "zzz", "ZoneText(SHORT)" },
        { "zzzz", "ZoneText(FULL)" },
        { "zzzzz", "ZoneText(FULL)" },

        { "I", "ZoneId()" },
        { "II", "ZoneId()" },
        { "III", "ZoneId()" },
        { "IIII", "ZoneId()" },
        { "IIIII", "ZoneId()" },

        { "Z", "Offset('+0000',+HHMM)" }, // SimpleDateFormat compatible
        { "ZZ", "Offset('+0000',+HHMM)" },
        { "ZZZ", "Offset('+00:00',+HH:MM)" },

        { "X", "Offset('Z',+HH)" },
        { "XX", "Offset('Z',+HHMM)" },
        { "XXX", "Offset('Z',+HH:MM)" },
        { "XXXX", "Offset('Z',+HHMMss)" },
        { "XXXXX", "Offset('Z',+HH:MM:ss)" },

        { "ppH", "Pad(Value(HourOfDay),2)" },
        { "pppDD", "Pad(Value(DayOfYear,2),3)" },

        { "yyyy[-MM[-dd", "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)['-'Value(DayOfMonth,2)]]" },
        { "yyyy[-MM[-dd]]", "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)['-'Value(DayOfMonth,2)]]" },
        { "yyyy[-MM[]-dd]", "Value(Year,4,19,EXCEEDS_PAD)['-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)]" },

        {
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "Value(Year,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)"
                + "'T'Value(HourOfDay,2)':'Value(MinuteOfHour,2)':'Value(SecondOfMinute,2)'.'Fraction(NanoOfSecond,3,3)" }, };
  }

  @Test(dataProvider = "validPatterns", groups = { "tck" })
  public void test_appendPattern_valid(String input, String expected) throws Exception {

    this.builder.appendPattern(input);
    DateTimeFormatter f = this.builder.toFormatter();
    assertEquals(f.toString(), expected);
  }

  // -----------------------------------------------------------------------
  @DataProvider(name = "invalidPatterns")
  Object[][] dataInvalid() {

    return new Object[][] { { "'" }, { "'hello" }, { "'hel''lo" }, { "'hello''" }, { "]" }, { "yyyy]" }, { "yyyy]MM" },
        { "yyyy[MM]]" },

        { "MMMMMM" }, { "QQQQQQ" }, { "EEEEEE" }, { "aaaaaa" }, { "ZZZZ" }, { "XXXXXX" },

        { "RO" },

        { "p" }, { "pp" }, { "p:" },

        { "f" }, { "ff" }, { "f:" }, { "fy" }, { "fa" }, { "fM" }, };
  }

  @Test(dataProvider = "invalidPatterns", expectedExceptions = IllegalArgumentException.class, groups = { "tck" })
  public void test_appendPattern_invalid(String input) throws Exception {

    try {
      this.builder.appendPattern(input);
    } catch (IllegalArgumentException ex) {
      throw ex;
    }
  }

}
