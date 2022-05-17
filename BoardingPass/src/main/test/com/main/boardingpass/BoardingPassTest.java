package com.main.boardingpass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardingPassTest {

    @Test
    void isInvalidNameTest() { // inputs are trimmed before entering the function
        assertTrue(BoardingPass.isInvalidName("taco"));
        assertTrue(BoardingPass.isInvalidName("burrito"));
        assertTrue(BoardingPass.isInvalidName("chimichanga"));
        assertTrue(BoardingPass.isInvalidName(""));
        assertTrue(BoardingPass.isInvalidName("f"));
        assertTrue(BoardingPass.isInvalidName("w   bluecheese"));
        assertTrue(BoardingPass.isInvalidName(") ("));
        assertTrue(BoardingPass.isInvalidName("snoop  dogg"));
        assertTrue(BoardingPass.isInvalidName("snoop-dogg-e-dogg"));
        assertFalse(BoardingPass.isInvalidName("snoop dogg"));
        assertFalse(BoardingPass.isInvalidName("burrito taco chimichanga"));
        assertFalse(BoardingPass.isInvalidName("Rayvon Gregory"));
        assertFalse(BoardingPass.isInvalidName("Anthony Garcia"));
        assertFalse(BoardingPass.isInvalidName("Brian Glenn"));
    }

    @Test
    void isInvalidYearTest() {
        assertFalse(BoardingPass.isInvalidYear(2022, 2022));
        assertFalse(BoardingPass.isInvalidYear(2023, 2022));
        assertTrue(BoardingPass.isInvalidYear(2021, 2022));
        assertTrue(BoardingPass.isInvalidYear(2024, 2022));
        assertTrue(BoardingPass.isInvalidYear(2021, 2022));
        assertTrue(BoardingPass.isInvalidYear(2020, 2022));
        assertTrue(BoardingPass.isInvalidYear(69, 2022));
    }

    @Test
    void isInvalidMonthTest() { // the year is validated before this function executes so only enter valid years
        assertFalse(BoardingPass.isInvalidMonth(6,5,2022,2022));
        assertFalse(BoardingPass.isInvalidMonth(3,5,2022,2023));
        assertFalse(BoardingPass.isInvalidMonth(12,5,2022,2023));
        assertFalse(BoardingPass.isInvalidMonth(5,5,2022,2022));
        assertTrue(BoardingPass.isInvalidMonth(1, 5, 2022, 2022));
        assertTrue(BoardingPass.isInvalidMonth(4, 5, 2022, 2022));
        assertTrue(BoardingPass.isInvalidMonth(3, 5, 2022, 2022));
        assertTrue(BoardingPass.isInvalidMonth(2, 5, 2022, 2022));
        assertTrue(BoardingPass.isInvalidMonth(-2, 5, 2022, 2022));
        assertTrue(BoardingPass.isInvalidMonth(13, 5, 2022, 2022));
    }

    @Test
    void isInvalidDateTest() {
        // the month is validated before this function executes so only enter valid months
        // result of test will vary depending on number of days in the year so purposely NOT testing
        // February 29th. The code DOES account for leap years.
        assertFalse(BoardingPass.isInvalidDate(16, 16, 5, 5));
        assertFalse(BoardingPass.isInvalidDate(17, 16, 5, 5));
        assertFalse(BoardingPass.isInvalidDate(28, 12, 2, 2));
        assertFalse(BoardingPass.isInvalidDate(31, 16, 1, 1));
        assertTrue(BoardingPass.isInvalidDate(31, 30, 11, 4));
        assertTrue(BoardingPass.isInvalidDate(30, 12, 2, 2));
        assertTrue(BoardingPass.isInvalidDate(40, 16, 5, 5));
        assertTrue(BoardingPass.isInvalidDate(32, 16, 1, 1));
        assertTrue(BoardingPass.isInvalidDate(31, 30, 4, 4));
    }

    @Test
    void getDepartureDateTest() {
        assertEquals("2022-05-16", BoardingPass.getDepartureDate(2022, 5, 16));
        assertEquals("6969-69-69",  BoardingPass.getDepartureDate(6969, 69, 69));
        assertEquals("6969-06-09",  BoardingPass.getDepartureDate(6969, 6, 9));
        assertNotEquals("2222-1-1", BoardingPass.getDepartureDate(2222, 1,1));
        assertNotEquals("2121-1-14", BoardingPass.getDepartureDate(2222, 1,14));
        assertNotEquals("1111-1-1", BoardingPass.getDepartureDate(1111, 1,1));
    }

    @Test
    void isInvalidEmail() { // email is trimmed before entering function
        assertTrue(BoardingPass.isInvalidEmail(""));
        assertTrue(BoardingPass.isInvalidEmail("."));
        assertTrue(BoardingPass.isInvalidEmail("@"));
        assertTrue(BoardingPass.isInvalidEmail("@."));
        assertTrue(BoardingPass.isInvalidEmail(".com"));
        assertTrue(BoardingPass.isInvalidEmail("@.com"));
        assertTrue(BoardingPass.isInvalidEmail("-.com"));
        assertTrue(BoardingPass.isInvalidEmail("thisisinvalid.com"));
        assertTrue(BoardingPass.isInvalidEmail("3@_g.com"));
        assertFalse(BoardingPass.isInvalidEmail("thisisnotinvalid@gmail.com"));
        assertFalse(BoardingPass.isInvalidEmail("3@g.com"));
        assertFalse(BoardingPass.isInvalidEmail("3_@g.com"));
        assertFalse(BoardingPass.isInvalidEmail("3-@g.com"));
        assertFalse(BoardingPass.isInvalidEmail("69@mmm.wow"));
    }

    @Test
    void isInvalidPhoneNumber() {
        assertTrue(BoardingPass.isInvalidPhoneNumber("XXX-XXX-XXXX"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("XXXXXXXXXX"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("111-1111111"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("2222222222"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("333333-333"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("4-444-444-4444"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("XXXXXX-XXXX"));
        assertTrue(BoardingPass.isInvalidPhoneNumber("XXX-XXXXXXX"));
        assertFalse(BoardingPass.isInvalidPhoneNumber("860-111-1111"));
        assertFalse(BoardingPass.isInvalidPhoneNumber("336-222-2222"));
        assertFalse(BoardingPass.isInvalidPhoneNumber("919-333-3333"));
        assertFalse(BoardingPass.isInvalidPhoneNumber("696-969-6969"));
    }

    @Test
    void isUnrecognizedGender() { // strings are capitalized before entering the function
        assertTrue(BoardingPass.isUnrecognizedGender(""));
        assertTrue(BoardingPass.isUnrecognizedGender(" "));
        assertTrue(BoardingPass.isUnrecognizedGender("  "));
        assertTrue(BoardingPass.isUnrecognizedGender("0"));
        assertTrue(BoardingPass.isUnrecognizedGender("00"));
        assertTrue(BoardingPass.isUnrecognizedGender("000"));
        assertTrue(BoardingPass.isUnrecognizedGender("A"));
        assertTrue(BoardingPass.isUnrecognizedGender("B"));
        assertTrue(BoardingPass.isUnrecognizedGender("C"));
        assertTrue(BoardingPass.isUnrecognizedGender("D"));
        assertTrue(BoardingPass.isUnrecognizedGender("E"));
        assertTrue(BoardingPass.isUnrecognizedGender("F"));
        assertTrue(BoardingPass.isUnrecognizedGender("G"));
        assertFalse(BoardingPass.isUnrecognizedGender("M"));
        assertFalse(BoardingPass.isUnrecognizedGender("W"));
    }

    @Test
    void addDiscountIfApplicableTest() {
        assertEquals(100.00, BoardingPass.addDiscountIfApplicable(100, 30, 'M'));
        assertEquals(75.00, BoardingPass.addDiscountIfApplicable(100, 30, 'F'));
        assertEquals(40.00, BoardingPass.addDiscountIfApplicable(100, 60, 'M'));
        assertEquals(40.00, BoardingPass.addDiscountIfApplicable(100, 60, 'F'));
        assertEquals(50.00, BoardingPass.addDiscountIfApplicable(100, 10, 'M'));
        assertEquals(240.88, BoardingPass.addDiscountIfApplicable(321.18, 30, 'F'));
        assertEquals(128.47, BoardingPass.addDiscountIfApplicable(321.18, 70, 'F'));
    }
}