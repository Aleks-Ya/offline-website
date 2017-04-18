package ru.yaal.offlinewebsite.api.http;

/**
 * @author Aleksey Yablokov
 */
public interface HttpInfo {
    int getResponseCode();

    long getContentLength();

    long getLastModified();

    String getContentType();

    interface ContentTypes {
        String HTML = "text/html";
    }
}
