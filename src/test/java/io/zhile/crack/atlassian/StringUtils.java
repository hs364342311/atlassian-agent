package io.zhile.crack.atlassian;

import java.util.*;

/**
* @Description StringUtils 类（或接口）
* @Author huangshun
* @Date 2024/2/15
*/
public class StringUtils {

    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";

    public StringUtils() {
    }

    public static boolean hasLength(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasText(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static String trimLeadingWhitespace(String str) {
        if (str.length() == 0) {
            return str;
        } else {
            StringBuffer buf = new StringBuffer(str);

            while(buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
                buf.deleteCharAt(0);
            }

            return buf.toString();
        }
    }

    public static String trimTrailingWhitespace(String str) {
        if (str.length() == 0) {
            return str;
        } else {
            StringBuffer buf = new StringBuffer(str);

            while(buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }

    public static int countOccurrencesOf(String str, String sub) {
        if (str != null && sub != null && str.length() != 0 && sub.length() != 0) {
            int count = 0;
            int pos = 0;

            int idx;
            for(boolean var4 = false; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (inString == null) {
            return null;
        } else if (oldPattern != null && newPattern != null) {
            StringBuffer sbuf = new StringBuffer();
            int pos = 0;
            int index = inString.indexOf(oldPattern);

            for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sbuf.append(inString.substring(pos, index));
                sbuf.append(newPattern);
                pos = index + patLen;
            }

            sbuf.append(inString.substring(pos));
            return sbuf.toString();
        } else {
            return inString;
        }
    }

    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    public static String deleteAny(String inString, String chars) {
        if (inString != null && chars != null) {
            StringBuffer out = new StringBuffer();

            for(int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (chars.indexOf(c) == -1) {
                    out.append(c);
                }
            }

            return out.toString();
        } else {
            return inString;
        }
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringTokenizer st = new StringTokenizer(str, delimiters);
        ArrayList tokens = new ArrayList();

        while(true) {
            String token;
            do {
                if (!st.hasMoreTokens()) {
                    return (String[])((String[])tokens.toArray(new String[tokens.size()]));
                }

                token = st.nextToken();
                if (trimTokens) {
                    token = token.trim();
                }
            } while(ignoreEmptyTokens && token.length() <= 0);

            tokens.add(token);
        }
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        if (str == null) {
            return new String[0];
        } else if (delimiter == null) {
            return new String[]{str};
        } else {
            List result = new ArrayList();
            int pos = 0;

            int delPos;
            for(boolean var4 = false; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                result.add(str.substring(pos, delPos));
            }

            if (str.length() > 0 && pos <= str.length()) {
                result.add(str.substring(pos));
            }

            return (String[])((String[])result.toArray(new String[result.size()]));
        }
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    public static Set commaDelimitedListToSet(String str) {
        Set set = new TreeSet();
        String[] tokens = commaDelimitedListToStringArray(str);

        for(int i = 0; i < tokens.length; ++i) {
            set.add(tokens[i]);
        }

        return set;
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (arr == null) {
            return "null";
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < arr.length; ++i) {
                if (i > 0) {
                    sb.append(delim);
                }

                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection c, String delim, String prefix, String suffix) {
        if (c == null) {
            return "null";
        } else {
            StringBuffer sb = new StringBuffer();
            Iterator it = c.iterator();

            for(int var6 = 0; it.hasNext(); sb.append(prefix + it.next() + suffix)) {
                if (var6++ > 0) {
                    sb.append(delim);
                }
            }

            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    public static String collectionToCommaDelimitedString(Collection coll) {
        return collectionToDelimitedString(coll, ",");
    }

    public static String[] addStringToArray(String[] arr, String str) {
        String[] newArr = new String[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[arr.length] = str;
        return newArr;
    }

    public static String[] sortStringArray(String[] source) {
        if (source == null) {
            return new String[0];
        } else {
            Arrays.sort(source);
            return source;
        }
    }

    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(true, str);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(false, str);
    }

    private static String changeFirstCharacterCase(boolean capitalize, String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            StringBuffer buf = new StringBuffer(strLen);
            if (capitalize) {
                buf.append(Character.toUpperCase(str.charAt(0)));
            } else {
                buf.append(Character.toLowerCase(str.charAt(0)));
            }

            buf.append(str.substring(1));
            return buf.toString();
        } else {
            return str;
        }
    }

    public static String getFilename(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
    }

    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith("/")) {
                newPath = newPath + "/";
            }

            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    public static String cleanPath(String path) {
        String pathToUse = replace(path, "\\", "/");
        String[] pathArray = delimitedListToStringArray(pathToUse, "/");
        List pathElements = new LinkedList();
        int tops = 0;

        for(int i = pathArray.length - 1; i >= 0; --i) {
            if (!".".equals(pathArray[i])) {
                if ("..".equals(pathArray[i])) {
                    ++tops;
                } else if (tops > 0) {
                    --tops;
                } else {
                    pathElements.add(0, pathArray[i]);
                }
            }
        }

        return collectionToDelimitedString(pathElements, "/");
    }

    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = parts.length > 0 ? parts[0] : "";
        String country = parts.length > 1 ? parts[1] : "";
        String variant = parts.length > 2 ? parts[2] : "";
        return language.length() > 0 ? new Locale(language, country, variant) : null;
    }
}
