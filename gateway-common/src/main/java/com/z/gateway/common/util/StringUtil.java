
package com.z.gateway.common.util;



import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * @author Administrator
 *
 */
public class StringUtil {

    private static Log log = LogFactory.getLog("StringUtil");

    public static String gbkToAscii(String s) {
        if (s == null)
            return "";
        try {
            s = new String(s.getBytes("GBK"), "iso8859-1");
        } catch (Exception e) {
            log.error("gbkToAscii fail," + e.getMessage(), e);
        }
        return s;
    }

    public static String asciiToGbk(String s) {
        if (s == null)
            return null;
        try {
            s = new String(s.getBytes("iso8859-1"), "GBK");
        } catch (Exception e) {
            log.error("asciiToGbk fail," + e.getMessage(), e);
            return null;
        }
        return s;
    }

    public static String[] parseStringToArray(String s, String delim) {
        StringTokenizer st = new StringTokenizer(s, delim);
        List v = new ArrayList();
        while (st.hasMoreElements()) {
            String tmp = st.nextToken();
            v.add(tmp);
        }
        return (String[]) v.toArray(new String[v.size()]);
    }

    public static String[] parseStringToArray2(String s, String delim) {

        List v = new ArrayList();

        String tmp = s;
        int startIdx = 0;
        while (s != null && s.indexOf(delim) != -1) {
            int idx = s.indexOf(delim);
            v.add(s.substring(0, idx).trim());
            startIdx += idx + delim.length();
            s = s.substring(startIdx);
            startIdx = 0;
        }
        v.add((s != null) ? s.trim() : "");
        return (String[]) v.toArray(new String[v.size()]);

    }

    public static String replaceNonDigiCharToZero(String s) {
        if (s == null || s.equals("")) {
            return "0";
        }
        for (int ii = 0; ii < s.length(); ii++) {
            if (!Character.isDigit(s.charAt(ii))) {
                s = s.replace(s.charAt(ii), '0');
            }
        }
        return s;
    }

    /**
     * replace all the targets with patterns
     * 
     * @param s
     * @param target
     * @param pattern
     * @return
     */
    public static String replace(String s, String target, String pattern) {
        while (s.indexOf(target) != -1) {
            int i = s.indexOf(target);
            s = s.substring(0, i) + pattern + s.substring(i + target.length());
        }
        return s;
    }

    public String isNull(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 
     * @param obj
     */
    public static boolean isNotEmpty(String obj) {
        return obj != null && obj.trim().length() > 0;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj) {
        return !isNotEmpty(obj);
    }

    private static final char zeroArray[] = "0000000000000000000000000000000000000000000000000000000000000000"
            .toCharArray();

    public static String[] splitStr(String sStr, String sSplitter) {
        if (sStr == null || sStr.length() <= 0 || sSplitter == null || sSplitter.length() <= 0)
            return null;
        String[] saRet = null;
        int iLen = sSplitter.length();
        int[] iIndex = new int[sStr.length()];
        iIndex[0] = sStr.indexOf(sSplitter, 0);
        if (iIndex[0] == -1) {
            saRet = new String[1];
            saRet[0] = sStr;
            return saRet;
        }
        int iIndexNum = 1;
        while (true) {
            iIndex[iIndexNum] = sStr.indexOf(sSplitter, iIndex[iIndexNum - 1] + iLen);
            if (iIndex[iIndexNum] == -1)
                break;
            iIndexNum++;
        }
        Vector vStore = new Vector();
        int i = 0;
        String sub = null;
        for (i = 0; i < iIndexNum + 1; i++) {
            if (i == 0)
                sub = sStr.substring(0, iIndex[0]);
            else if (i == iIndexNum)
                sub = sStr.substring(iIndex[i - 1] + iLen, sStr.length());
            else
                sub = sStr.substring(iIndex[i - 1] + iLen, iIndex[i]);
            if (sub != null && sub.length() > 0)
                vStore.add(sub);
        }
        if (vStore.size() <= 0)
            return null;
        saRet = new String[vStore.size()];
        Enumeration e = vStore.elements();
        for (i = 0; e.hasMoreElements(); i++)
            saRet[i] = (String) e.nextElement();
        return saRet;
    }

    public static boolean isNum(String str) {
        if (str == null || str.length() <= 0)
            return false;
        char[] ch = str.toCharArray();
        for (int i = 0; i < str.length(); i++)
            if (!Character.isDigit(ch[i]))
                return false;
        return true;
    }

    public static String symbol(String value) {
        String val = "";
        if (value != null) {
            val = value;
        }
        val = replaceStrEx(val, "'", "’");
        val = replaceStrEx(val, ",", "，");
        val = replaceStrEx(val, "\"", "“");
        // val = replaceStrEx(val,"\"","“");
        return val;
    }

    public static String replaceStrEx(String sReplace, String sOld, String sNew) {
        if (sReplace == null || sOld == null || sNew == null)
            return null;
        int iLen = sReplace.length();
        int iLenOldStr = sOld.length();
        int iLenNewStr = sNew.length();
        if (iLen <= 0 || iLenOldStr <= 0 || iLenNewStr < 0)
            return sReplace;
        int[] iIndex = new int[iLen];
        iIndex[0] = sReplace.indexOf(sOld, 0);
        if (iIndex[0] == -1)
            return sReplace;
        int iIndexNum = 1;
        while (true) {
            iIndex[iIndexNum] = sReplace.indexOf(sOld, iIndex[iIndexNum - 1] + 1);
            if (iIndex[iIndexNum] == -1)
                break;
            iIndexNum++;
        }
        Vector vStore = new Vector();
        String sub = sReplace.substring(0, iIndex[0]);
        if (sub != null)
            vStore.add(sub);
        int i = 1;
        for (i = 1; i < iIndexNum; i++) {
            vStore.add(sReplace.substring(iIndex[i - 1] + iLenOldStr, iIndex[i]));
        }
        vStore.add(sReplace.substring(iIndex[i - 1] + iLenOldStr, iLen));
        StringBuffer sbReplaced = new StringBuffer("");
        for (i = 0; i < iIndexNum; i++) {
            sbReplaced.append(vStore.get(i) + sNew);
        }
        sbReplaced.append(vStore.get(i));
        return sbReplaced.toString();
    }

    public static String replaceStr(String sReplace, String sOld, String sNew) {
        if (sReplace == null || sOld == null || sNew == null)
            return null;
        int iLen = sReplace.length();
        int iLenOldStr = sOld.length();
        int iLenNewStr = sNew.length();
        if (iLen <= 0 || iLenOldStr <= 0 || iLenNewStr < 0)
            return sReplace;
        int iIndex = -1;
        iIndex = sReplace.indexOf(sOld, 0);
        if (iIndex == -1)
            return sReplace;
        String sub = sReplace.substring(0, iIndex);
        StringBuffer sbReplaced = new StringBuffer("");
        sbReplaced.append(sub + sNew);
        sbReplaced.append(sReplace.substring(iIndex + iLenOldStr, iLen));
        return sbReplaced.toString();
    }

    public static String cvtTOgbk(String encoding, String string) {
        if (string == null) {
            return "";
        }
        try {
            return new String(string.getBytes(encoding), "GBK");
        } catch (UnsupportedEncodingException e) {
            log.error("cvtTOgbk fail," + e.getMessage(), e);
            return string;
        }
    }

    public static String cvtTOgbk(String str) {
        if (str == null)
            return "";
        try {
            return new String(str.getBytes("iso-8859-1"), "GBK");
        } catch (java.io.UnsupportedEncodingException un) {
            log.error("cvtTOgbk fail," + un.getMessage(), un);
            return str;
        }
    }

    public static String cvtTOiso(String str) {
        if (str == null)
            return "";
        try {
            return new String(str.getBytes("GBK"), "iso-8859-1");
        } catch (java.io.UnsupportedEncodingException un) {
            log.error("cvtTOiso fail," + un.getMessage(), un);
            return str;
        }
    }

    public static String cvtToUTF8(String str) {
        if (str == null)
            return "";
        try {
            return new String(str.getBytes("iso-8859-1"), "GBK");
        } catch (java.io.UnsupportedEncodingException un) {
            log.error("cvtToUTF8 fail," + un.getMessage(), un);
            return str;
        }
    }

    public static int toInteger(String integer) {
        return toInteger(integer, 0);
    }

    public static int toInteger(String integer, int def) {
        int ret = 0;
        try {
            ret = Integer.parseInt(integer);
        } catch (NumberFormatException nfx) {
            ret = def;
        }
        return ret;
    }

    public static long toLong(String longStr) {
        return toLong(longStr, 0);
    }

    public static long toLong(String longStr, long def) {
        long ret = 0;
        try {
            ret = Long.parseLong(longStr);
        } catch (NumberFormatException nfx) {
            ret = def;
        }
        return ret;
    }

    public static double toDouble(String doubleStr) {
        return toDouble(doubleStr, 0.0);
    }

    public static double toDouble(String doubleStr, double def) {
        double ret = 0.0;
        try {
            ret = Double.parseDouble(doubleStr);
        } catch (NumberFormatException nfx) {
            ret = def;
        }
        return ret;
    }

    public static String contactStr(String[] saStr, String sContacter) {
        if (saStr == null || saStr.length <= 0 || sContacter == null || sContacter.length() <= 0)
            return null;
        StringBuffer sRet = new StringBuffer("");
        for (int i = 0; i < saStr.length; i++) {
            if (i == saStr.length - 1)
                sRet.append(saStr[i]);
            else
                sRet.append(saStr[i] + sContacter);
        }
        return sRet.toString();
    }

    public static String contactStr(String[] saStr, String sContacter, String str) {
        if (saStr == null || saStr.length <= 0 || sContacter == null || sContacter.length() <= 0)
            return null;
        StringBuffer sRet = new StringBuffer("");
        for (int i = 0; i < saStr.length; i++) {
            if (i == saStr.length - 1)
                sRet.append(str + saStr[i] + str);
            else
                sRet.append(str + saStr[i] + str + sContacter);
        }
        return sRet.toString();
    }

    public static boolean validHomepage(String homepage) {
        if (homepage == null)
            return false;
        if (homepage.length() == 0)
            return false;
        if (homepage.length() > 7) {
            if (homepage.toLowerCase().indexOf("http://") == 0 && homepage.indexOf(".") > 0)
                return false;
            else
                return true;
        } else {
            if (homepage.length() == 7 && homepage.toLowerCase().indexOf("http://") == 0)
                return false;
            else
                return true;
        }
    }

    public static boolean isEmailUrl(String str) {
        if ((str == null) || (str.length() == 0))
            return false;
        if ((str.indexOf('@') > 0) && (str.indexOf('@') == str.lastIndexOf('@'))) {
            if ((str.indexOf('.') > 0) && (str.lastIndexOf('.') > str.indexOf('@'))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidEmail(String str) {
        return Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$").matcher(str).matches();
    }

    public static String cvtTOgb2312(String str) {
        if (str == null)
            return "";
        try {
            return new String(str.getBytes("iso-8859-1"), "gb2312");
        } catch (java.io.UnsupportedEncodingException un) {
            log.error("cvtTOgb2312 fail," + un.getMessage(), un);
            return str;
        }
    }

    public static String chkNull(String str) {
        if (str == null)
            return "未知";
        else
            return str;
    }

    public static String chkNullBlank(String str) {
        if (str == null)
            return "";
        else if (str.trim().equals("null"))
            return "";
        else
            return str.trim();
    }

    public static String chkNullDate(Date date) {
        if (date == null)
            return "";
        else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return df.format(date);
        }
    }

    public static String getDateToString(Date date) {
        if (date == null)
            return "";
        else {
            DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
            return df.format(date);
        }
    }

    public static String chkNullDateHH(Date date) {
        if (date == null)
            return "";
        else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(date);
        }
    }

    public static String checkNullDateHH(Timestamp timestamp) {
        if (timestamp == null)
            return "";
        else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(timestamp);
        }
    }

    public static String chkNullBlank(Object object) {
        if (object != null)
            return chkNullBlank(object.toString());
        else
            return "";
    }

    public static String[] checkBox(String[] source, String[] pos) {
        if (source == null || pos == null || source.length < pos.length)
            return null;
        String[] res = new String[pos.length];
        for (int i = 0; i < pos.length; i++) {
            res[i] = source[Integer.parseInt(pos[i])];
        }
        return res;
    }

    public static String randomString(int len) {
        String asctbl = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
        String str = "";
        for (int i = 0; i < len; i++) {
            int offset = (int) Math.round(Math.random() * (asctbl.length() - 1));
            str += asctbl.substring(offset, offset + 1);
        }
        return str;
    }

    public static String ParseMsg(String strMsg, String strValue, int intLen) {
        if (strMsg == null)
            strMsg = strValue;
        long lMsgLength = strMsg.getBytes().length;
        if (lMsgLength < intLen) {
            while (lMsgLength < intLen) {
                strMsg = strMsg + strValue;
                lMsgLength = strMsg.getBytes().length;
            }
        }
        return strMsg;
    }

    public static String getValue(Object str) {
        if (str == null || str.equals("null"))
            return "";
        else
            return str.toString();
    }

    public static Date formateDate(String dateString, String hourStr, String minStr) {
        Date result = null;
        Calendar calendar = Calendar.getInstance();
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8, 10));
        int hour = Integer.parseInt(hourStr);
        int min = Integer.parseInt(minStr);
        calendar.set(year, month - 1, day, hour, min, 0);
        return calendar.getTime();
    }

    public static String TimestampDateFormat(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        String timestampStr = null;
        timestampStr = "to_timestamp('" + timestamp.toString() + "','YYYY-MM-DD HH24:MI:SSXFF')";
        return timestampStr;
    }

    /**
     * 给出"YYYY-MM-DD","HH","MI"三个字符串,返回一个oracle的时间格式
     * 
     * @author Ltao 2004-12-12
     * @param dateString
     * @param hourStr
     * @param minStr
     * @return oracle的时间格式 形如"to_date(..." , 如果三个参数中缺少其中任意一个,则返回null;
     */
    public static String oracleDateFormat(String dateString, String hourStr, String minStr, String secStr) {
        if (dateString == null || hourStr == null || minStr == null || secStr == null || dateString.equals("")
                || hourStr.equals("") || minStr.equals("") || secStr.equals("")) {
            return null;
        }
        String oracleDateStr = null;
        String tempStr = dateString + " " + hourStr + ":" + minStr + ":" + secStr;
        // oracleDateStr = "to_date('" + tempStr + "','YYYY-MM-DD HH24:MI:SS')";
        oracleDateStr = "str_to_date('" + tempStr + "','%Y-%m-%d %H:%i:%s')";
        return oracleDateStr;
    }

    /**
     * 取timestamp的形如"YYYY-MM-DD"的日期字符串,
     * 
     * @author Ltao 2004-12-12
     * @param time
     *            时间
     * @return 形如"YYYY-MM-DD"的日期字符串, 如果time为null,则返回null;
     */
    public static String getDateStr(Date time) {
        if (time == null) {
            return "";
        }
        String result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
        result = df.format(time);
        return result;
    }

    /**
     * 取timestamp的形如"HH24:MI:SS"的时间字符串,
     * 
     * @author Ltao 2004-12-12
     * @param time
     *            时间
     * @return 形如"HH24:MI:SS"的时间字符串, 如果time为null,则返回null;
     */
    public static String getHourMinuteSecondStr(Timestamp time) {
        if (time == null) {
            return "";
        }
        String result = null;
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        result = df.format(time);
        return result;
    }

    public static String getHourMinuteSecondStr(Date time) {
        if (time == null) {
            return "";
        }
        String result = null;
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        result = df.format(time);
        return result;
    }

    /**
     * 取timestamp的形如"HH24:MI"的时间字符串,
     * 
     * @author Ltao 2004-12-12
     * @param time
     *            时间
     * @return 形如"HH24:MI"的时间字符串, 如果time为null,则返回null;
     */
    public static String getHourMinuteStr(Timestamp time) {
        if (time == null) {
            return "";
        }
        String result = null;
        DateFormat df = new SimpleDateFormat("hh:mm");
        result = df.format(time);
        return result;
    }

    public static String log2String(HashMap hashMap) {
        String result = "";
        Object[] keyList = hashMap.keySet().toArray();
        for (int i = 0; i < keyList.length; i++) {
            String keyName = keyList[i] == null ? "" : keyList[i].toString();
            String keyValue = hashMap.get(keyName) == null ? "" : hashMap.get(keyName).toString();
            result += keyName + ":" + hashMap.get(keyList[i].toString()) + "\n";
        }
        return result;
    }

    public static String log2ContentName(HashMap hashMap) {
        String result = "";
        if (hashMap.get("Content Name") != null) {
            result = (String) hashMap.get("Content Name");
        }
        return result;
    }

    public static String removeQueryString(String queryStr, String paramName) {
        int i = queryStr.indexOf(paramName);
        if (i >= 0) {
            int j = queryStr.indexOf("&", i);
            if (j > 0)
                queryStr = queryStr.substring(0, i) + queryStr.substring(j + 1);
            else if (i == 0)
                queryStr = "";
            else
                queryStr = queryStr.substring(0, i - 1);
        }
        return queryStr;
    }

    /**
     * 把时间类型传换成String，空的话换成""
     * 
     * @return
     */
    public static String getDateWeb(Date newDate) {
        return newDate == null ? "" : newDate.toString();
    }

    /**
     * 把时间类型传换成String，空的话换成""
     * 
     * @return
     */
    public static String getStrWeb(String str) {
        return str == null ? "" : str.toString();
    }

    /**
     * 把时间类型传换成String，空的话换成""
     * 
     * @return
     */
    public static String getLongWeb(Long newLong) {
        return newLong == null ? "" : newLong.toString();
    }

    /**
     * 分割以指定符号的字符串
     * 
     * @param splitStr
     * @return
     */
    public static String[] getSplitString(String splitStr, String splitType) {
        StringTokenizer str = new StringTokenizer(splitStr, splitType);
        int strNum = str.countTokens();
        String strTemp[] = new String[strNum];
        int i = 0;
        while (str.hasMoreElements()) {
            strTemp[i] = (String) str.nextElement();
            i++;
        }
        return strTemp;
    }

    public static String percentage(double d) {
        // System.out.println("d:"+d);
        Double double1 = new Double(d);
        int i = double1.intValue();
        return i + "%";
    }

    // 把字符串中大写的字母都变成小写
    public static String changeSmall(String str) {
        String smallStr = "";
        char indexChar;
        for (int i = 0; str != null && i < str.length(); i++) {
            indexChar = str.charAt(i);
            if (indexChar == 'A') {
                indexChar = 'a';
            } else if (indexChar == 'B') {
                indexChar = 'b';
            } else if (indexChar == 'C') {
                indexChar = 'c';
            } else if (indexChar == 'D') {
                indexChar = 'd';
            } else if (indexChar == 'E') {
                indexChar = 'e';
            } else if (indexChar == 'F') {
                indexChar = 'f';
            } else if (indexChar == 'G') {
                indexChar = 'g';
            } else if (indexChar == 'H') {
                indexChar = 'h';
            } else if (indexChar == 'I') {
                indexChar = 'i';
            } else if (indexChar == 'J') {
                indexChar = 'j';
            } else if (indexChar == 'K') {
                indexChar = 'k';
            } else if (indexChar == 'L') {
                indexChar = 'l';
            } else if (indexChar == 'M') {
                indexChar = 'm';
            } else if (indexChar == 'N') {
                indexChar = 'n';
            } else if (indexChar == 'O') {
                indexChar = 'o';
            } else if (indexChar == 'P') {
                indexChar = 'p';
            } else if (indexChar == 'Q') {
                indexChar = 'q';
            } else if (indexChar == 'R') {
                indexChar = 'r';
            } else if (indexChar == 'S') {
                indexChar = 's';
            } else if (indexChar == 'T') {
                indexChar = 't';
            } else if (indexChar == 'U') {
                indexChar = 'u';
            } else if (indexChar == 'V') {
                indexChar = 'v';
            } else if (indexChar == 'W') {
                indexChar = 'w';
            } else if (indexChar == 'X') {
                indexChar = 'x';
            } else if (indexChar == 'Y') {
                indexChar = 'y';
            } else if (indexChar == 'Z') {
                indexChar = 'z';
            }
            smallStr += indexChar;
        }

        return smallStr;
    }

    public static String percentage(String s1, String s2) {
        return percentage(toInteger(s1) * 100 / toInteger(s2));
    }

    /**
     * change window type path to unix
     * 
     * @param originalPath
     * @return String
     */
    public static String makeAbsolutePath(String originalPath) {
        originalPath = originalPath.replace('\\', '/');

        if ('/' == originalPath.charAt(0)) {
            return originalPath;
        }

        /* Check for a drive specification for windows-type path */
        if (originalPath.substring(1, 2).equals(":")) {
            return originalPath;
        }

        /* and put the two together */
        return originalPath;
    }

    public static String str2booleanValue(String value) {
        if (value != null && value.trim().equals("1"))
            return "1";
        else
            return "0";
    }

    public static String concateFormArray(List values) {
        return concateFormArray(values, ",");
    }

    public static String concateFormArray(List values, String delimiter) {
        if (values == null || values.size() == 0)
            return "";
        String temp[] = new String[values.size()];
        values.toArray(temp);
        return contactStr(temp, delimiter);
        // // StringBuffer sb=new StringBuffer();
        // // for (int i=0;i<values.size();i++)
        // // sb.append(values.get(i).toString()+delimiter);
        // // String temp=sb.toString();
        // // // return temp.substring(0,2);
        // // return temp;
    }

    public static final String zeroPadString(String string, int length) {
        if (string == null || string.length() > length) {
            return string;
        } else {
            StringBuffer buf = new StringBuffer(length);
            buf.append(zeroArray, 0, length - string.length()).append(string);
            return buf.toString();
        }
    }

    public static final String moneyFormat(String param) {
        String ret = "";
        try {

            int index = param.indexOf(".");
            if (index > 0) {
                param = param + "00";
            } else {
                param = param + ".00";
            }
            index = param.indexOf(".");

            ret = param.substring(0, index + 3);

        } catch (Exception e) {
            ret = "0.00";
        }
        return ret;
    }

    public static final String achivFormat(String param) {
        String ret = "";
        try {
            double parValue = Double.parseDouble(param);
            ret = new DecimalFormat("0").format(parValue);
        } catch (Exception e) {
            ret = "0";
        }
        return ret;
    }

    /**
     * transform null to ""
     * 
     * @param param
     * @return String
     */
    public static final String tranNull(String param) {
        String strPra = "";
        if (null == param) {
            strPra = "";
        } else {
            strPra = param;
        }
        return strPra;
    }

    /**
     * yyyyMMdd To yyyy-MM-dd
     */
    public static final String dateFormat(String param) {
        String strPra = "";
        if (null == param) {
            strPra = "";
        } else if (param.length() == 8) {
            strPra = param.substring(0, 4) + "-" + param.substring(4, 6) + "-" + param.substring(6, 8);
        } else if (param.length() >= 10) {
            strPra = param.substring(0, 10);
        } else {
            strPra = "";
        }
        return strPra;
    }

    /**
     * To yyyy-MM-dd hh:mm:ss
     */
    public static final String timeFormat(String param) {
        String strPra = "";
        if (null == param) {
            strPra = "";
        } else if (param.length() >= 19) {
            strPra = param.substring(0, 19);
        } else {
            strPra = "";
        }
        return strPra;
    }

    /**
     * 按字节截取字符串前n个字节，不分开全角字符
     */
    public static String strSplit(String s, int n) {
        int d = n;
        int i = 0;
        while (i < s.length() && (d > 1 || (d == 1 && s.charAt(i) < 256))) {
            if (s.charAt(i) < 256) {
                d--;
            } else {
                d -= 2;
            }
            ++i;
        }
        return s.substring(0, i);
    }

    /**
     * 
     * @param paraString
     * @param paraParmsList
     * @return
     */
    private String GetFullConstants(String paraString, String[] paraParmsList) {
        String result = paraString;

        for (int i = 0; i < paraParmsList.length; i++) {
            result = result.replaceAll("{" + i + "}", paraParmsList[i]);
        }

        return result;
    }

    /**
     * convert IP addr (such as 192.168.10.21) to long (192168010021) Some time,
     * CDN return IP as [202.23.45.134,211.45.23.101], which the first is cust's
     * IP and the second is CDN's supplier's proxy server. Then in case, we
     * parse the first one only.
     */
    public static long convertIP2Long(String ipAddr) {

        if (ipAddr == null || ipAddr.length() < 7)
            return -1;

        StringBuffer result = new StringBuffer();

        if (ipAddr.indexOf(",") > 6)
            ipAddr = ipAddr.substring(0, ipAddr.indexOf(","));

        StringTokenizer st = new StringTokenizer(ipAddr.trim(), ".");
        while (st.hasMoreElements()) {
            String pt = (String) st.nextElement();
            if (pt.length() == 1)
                result.append("00" + pt);
            else if (pt.length() == 2)
                result.append("0" + pt);
            else
                result.append(pt);
        }
        return Long.parseLong(result.toString());
    }

    /**
     * 对给定字符串以默认"UTF-8"编码方式解码一次
     * 
     * @param input
     *            待解码字符串
     * @return 如果给定字符串input不为空，则返回以"UTF-8"方式解码一次后的字符串；否则原样返回
     * @author 11082829
     */
    public static String decodeString(String input) {
        return decodeString(input, "UTF-8");
    }

    /**
     * 对给定字符串以指定编码方式解码一次
     */
    public static String decodeString(String input, String encode) {
        String result = input;
        if (isNotEmpty(input)) {
            if (isEmpty(encode)) {
                encode = "UTF-8";
            }
            try {
                result = URLDecoder.decode(input, encode);
            } catch (UnsupportedEncodingException e) {
                result = input;
            }
        }
        return result;
    }

    /**
     * 对给定字符串以指定编码方式、指定次数解码
     */
    public static String decodeString(String input, String encode, int times) {
        if (times < 0) {
            times = 1;
        }

        String result = input;
        if (isNotEmpty(input)) {
            while (times-- > 0) {
                result = decodeString(input, encode);
            }
        }

        return result;
    }

    /**
     * 获取两个时间相隔的天数
     * 
     * @author 12060945
     * @param start
     *            开始的天数
     * @param end
     *            结束的天
     * @return 返回的天数
     */
    public static long getDay(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 得到毫秒数
        long timeStart;
        long timeEnd;
        long dayCount = 0;
        try {
            timeStart = sdf.parse(start).getTime();
            timeEnd = sdf.parse(end).getTime();
            // 两个日期想减得到天数
            dayCount = (timeEnd - timeStart) / (24 * 3600 * 1000);
        } catch (ParseException e) {
            log.error("getDay fail," + e.getMessage(), e);
        }
        return dayCount;
    }

    /**
     * 判断两个日期的大小
     * 
     * @author 12060945
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            log.error("compareDate fail," + exception.getMessage(), exception);
        }
        return 0;
    }

    /**
     * get main domain by ServerName
     * 
     * @param serverName
     * @return
     */
    public static String getServerMainDomain(String serverName) {
        String svrMainDomain = serverName;
        String domainParts[] = svrMainDomain.split("\\.");
        int svrPartCount = domainParts.length;
        // if the is 3 parts in the server name, will need to compare the
        // 2nd,3rd part only, for others, will compare all
        if (svrPartCount >= 3) {
            svrMainDomain = domainParts[svrPartCount - 2] + "." + domainParts[svrPartCount - 1];
        }
        return svrMainDomain;
    }

    /**
     * get the domain as String with url
     * 
     * @param url
     * @return
     */
    public static String getURLMainDomain(String url) {
        String svr = url;
        boolean isFullURL = false;
        String METHODNAME = "getMainDomainString";
        try {
            // decode url, if the url is encoded
            svr = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            svr = url;
            log.error("getURLMainDomain fail," + e.getMessage(), e);
        }
        // remove the protocal part, http,https, etc
     
        if (svr.contains("http://") && svr.contains(".com")) {
            svr = svr.substring(svr.indexOf("http://"), svr.indexOf(".com") + 4);
            isFullURL = true;
        } else if (svr.contains("http://") && svr.contains(".cn")) {
            svr = svr.substring(svr.indexOf("http://"), svr.indexOf(".cn") + 3);
            isFullURL = true;
        } else if (svr.contains("https://") && svr.contains(".com")) {
            svr = svr.substring(svr.indexOf("https://"), svr.indexOf(".com") + 4);
            isFullURL = true;
        } else if (svr.contains("https://") && svr.contains(".cn")) {
            svr = svr.substring(svr.indexOf("https://"), svr.indexOf(".cn") + 3);
            isFullURL = true;
        }
        

        svr = StringUtils.stripStart(svr, "https://");
        svr = StringUtils.stripStart(svr, "http://");
        // get the first part as server name
        svr = svr.split("/")[0];
        String svrMainDomain = svr;
        // if is full url then start getting main domain, otherwise, return
        // empty
        // if all the svr name removes. is numbers, then the server is using IP,
        // otherwise, only need to compare the last 2 part
        if (isFullURL) {
            if (!StringUtils.isNumeric(svr.replace(".", ""))) {
                String domainParts[] = svr.split("\\.");
                int svrPartCount = domainParts.length;
                // if the is 3 parts in the server name, will need to compare
                // the 2nd,3rd part only, for others, will compare all
                if (svrPartCount >= 3) {
                    svrMainDomain = domainParts[svrPartCount - 2] + "." + domainParts[svrPartCount - 1];
                }
            }
        } else {
            svrMainDomain = "";
        }

        return svrMainDomain;
    }

    /**
     * if url is allowed
     * 
     * @param urlString
     * @param allowDomain
     *            (*.suning.com,*.cnsuning.com)
     * @return
     */
    public static boolean urlAccessAllowed(String domainString, String[] allowDomains) {
        boolean allow = false;
        for (int i = 0; i < allowDomains.length; i++) {
            String s = allowDomains[i];
            // generate regex
            s = s.replace('.', '#');
            s = s.replaceAll("#", "\\.");
            s = s.replace('*', '#');
            s = s.replaceAll("#", ".*");
            s = s.replace('?', '#');
            s = s.replaceAll("#", ".?");
            s = "^" + s + "$";
            ;
            Pattern p = Pattern.compile(s);
            Matcher m = p.matcher(domainString);
            boolean b = m.matches();
            if (b) {
                allow = true;
                break;
            }
        }

        return allow;

    }

    /**
     * 
     * @param obj
     */
    public static boolean isNotEmptyWithoutNull(String obj) {
        return obj != null && obj.trim().length() > 0 && !obj.contains("null");
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static boolean isEmptyWithoutNull(String obj) {
        return !isNotEmptyWithoutNull(obj);
    }

    /**
     * timestamp 类型转换成date
     * 
     * @param timestamp
     * @return
     */
    public static Date convertTimeStampToDate(Timestamp timestamp) {
        String methodName = "convertStringToTimeStamp";
        if (timestamp == null) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = df.format(timestamp);
        Date date = null;
        try {
            date = df.parse(times);
        } catch (ParseException e) {
        }
        return date;

    }

    public static String getPhoneVerification(int limit) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 重要个人信息（手机号码、身份证号码、邮箱）以星号代替
     * 
     * @param num
     * @return
     */
    public static String secreteSensitiveNum(String num) {
        if (isEmpty(num)) {
            return num;
        }

        if (num.contains("@")) { // 邮箱email
            int iAt = num.indexOf("@");
            int oriLen = 3;
            if (iAt <= 3) {
                oriLen = 1;
            }
            int starArrayLen = num.substring(0, iAt - oriLen).length();
            char[] starArray = new char[starArrayLen];
            for (int i = 0; i < starArray.length; i++) {
                starArray[i] = '*';
            }
            String starStr = String.valueOf(starArray);
            StringBuilder sb = new StringBuilder();
            sb.append(starStr).append(num.substring(iAt - oriLen, iAt)).append(num.substring(iAt));

            return sb.toString();
        } else if (num.length() == 11) { // 手机号码
            StringBuilder sb = new StringBuilder();
            sb.append(num.substring(0, 3)).append("****").append(num.substring(7));

            return sb.toString();
        } else if (num.length() == 15 || num.length() == 18) { // 身份证号码
            StringBuilder sb = new StringBuilder();
            sb.append(num.substring(0, 3)).append(num.length() == 15 ? "********" : "***********")
                    .append(num.substring(num.length() - 4));

            return sb.toString();
        }

        return num;
    }

}