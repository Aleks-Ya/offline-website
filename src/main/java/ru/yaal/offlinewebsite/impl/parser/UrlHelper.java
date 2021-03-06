package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
public class UrlHelper {
    private UrlHelper() {
    }

    @SneakyThrows
    public static String toAbsoluteUrlStr(String resUrlStr, String relativeUrlStr) {
        URL rootUrl = new URL(resUrlStr);
        URL url = new URL(rootUrl, relativeUrlStr);
        String newPath = removeDotSegments(url.getPath());
        return replacePath(url, newPath);
    }

    private static String removeDotSegments(String url) {
        String input = url;
        List<String> output = new ArrayList<>();
        while (!input.isEmpty()) {
            if (input.startsWith("../") || input.startsWith("./")) {
                input = input.substring(input.indexOf("/") + 1);
                continue;
            }
            if (input.startsWith("/./")) {
                input = "/" + input.substring(3);
                continue;
            }
            if (input.startsWith("/../")) {
                input = "/" + input.substring(4);
                if (!output.isEmpty()) {
                    output.remove(output.size() - 1);
                }
                continue;
            }
            if (input.startsWith("/..")) {
                input = "/" + input.substring(3);
                output.remove(output.size() - 1);
                continue;
            }
            if (input.startsWith("/.")) {
                input = input.substring(2);
                continue;
            }
            if (input.equals("..") || input.equals(".")) {
                break;
            }

            int slashInd = input.indexOf("/");
            if (slashInd == 0) {
                slashInd = input.indexOf("/", 1);
            }
            if (slashInd == -1) {
                slashInd = input.length();
            }
            output.add(input.substring(0, slashInd));
            input = input.substring(slashInd);
        }
        return output.stream().collect(Collectors.joining());
    }

    @SneakyThrows
    private static String replacePath(URL oldUrl, String newPath) {
        String urlStr = oldUrl.toString();
        String oldPath = oldUrl.getPath();
        return urlStr.replace(oldPath, newPath);
    }

    public static String removeLastSegmentFromPath(String path) {
        if (path.isEmpty()) {
            return "";
        }
        int slashInd = path.lastIndexOf("/");
        if (slashInd == path.length() - 1) {
            slashInd = path.lastIndexOf("/", slashInd - 1);
        }
        if (slashInd == -1) {
            return "/";
        }
        String newPath = path.substring(0, slashInd);
        if (!newPath.endsWith("/")) {
            newPath += "/";
        }
        return newPath;
    }
}
