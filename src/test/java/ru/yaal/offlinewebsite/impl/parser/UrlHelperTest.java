package ru.yaal.offlinewebsite.impl.parser;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.yaal.offlinewebsite.impl.parser.UrlHelper.toAbsoluteUrlStr;

/**
 * @author Aleksey Yablokov
 */
public class UrlHelperTest {
    @Test
    public void testToAbsoluteUrlStr() {
        assertThat(toAbsoluteUrlStr("http://ya.ru/site/index.html", "contacts.html"),
                equalTo("http://ya.ru/site/contacts.html"));

        assertThat(toAbsoluteUrlStr("http://ya.ru/site/", "contacts.html"),
                equalTo("http://ya.ru/site/contacts.html"));

        assertThat(toAbsoluteUrlStr("http://ya.ru/site/index.html", "/contacts.html"),
                equalTo("http://ya.ru/contacts.html"));

        assertThat(toAbsoluteUrlStr("http://ya.ru/site/index.html", "http://ya.ru/site/contacts.html"),
                equalTo("http://ya.ru/site/contacts.html"));

        assertThat(toAbsoluteUrlStr("https://logback.qos.ch/manual/index.html", "https://logback.qos.ch/manual/filters.html"),
                equalTo("https://logback.qos.ch/manual/filters.html"));
    }

}