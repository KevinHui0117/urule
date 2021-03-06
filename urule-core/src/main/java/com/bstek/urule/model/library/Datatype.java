/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.model.library;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public enum Datatype {
    String, Integer, Char, Double, Long, Float, BigDecimal, Boolean, Date, List, Set, Map, Enum, Object, LocalDate, LocalDateTime, YearMonth;

    public String convertObjectToString(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return value.toString();
        }
        switch (this) {
            case Object:
                return value.toString();
            case Date:
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sd.format((Date) value);
            case List:
                List<?> list = (List<?>) value;
                String a = "";
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);
                    if (i > 0) {
                        a += ",";
                    }
                    a += obj;
                }
                return a;
            case Set:
                Set<?> set = (Set<?>) value;
                String b = "";
                int i = 0;
                for (Object obj : set) {
                    if (i > 0) {
                        b += ",";
                    }
                    b += obj;
                    i++;
                }
                return b;
            case BigDecimal:
                BigDecimal bb = Utils.toBigDecimal(value);
                return bb.floatValue() + "";
            case Double:
                Double d = Utils.toBigDecimal(value).doubleValue();
                return d.floatValue() + "";
            case LocalDate:
                return ((org.joda.time.LocalDate) value).toString("yyyy-MM-dd");
            case LocalDateTime:
                return ((org.joda.time.LocalDateTime) value).toString("yyyy-MM-dd HH:mm:ss");
            case YearMonth:
                return ((org.joda.time.YearMonth) value).toString("yyyy-MM");
            default:
                return value.toString();
        }
    }

    @SuppressWarnings("rawtypes")
    public Object convert(Object value) {
        switch (this) {
            case Object:
                return value;
            case String:
                if (value == null) {
                    return null;
                }
                return value.toString();
            case Integer:
                if (value == null || value.toString().equals("")) {
                    value = "0";
                }
                return Utils.toBigDecimal(value).intValue();
            case Char:
                if (value == null) {
                    return '\u0000';
                }
                if (value instanceof Character) {
                    return (Character) value;
                }
                String str = value.toString();
                if (str.length() == 1) {
                    return str.toCharArray()[0];
                } else {
                    int intValue = Utils.toBigDecimal(value).intValue();
                    return (char) intValue;
                }
            case Double:
                if (value == null) {
                    value = "0";
                }
                return Utils.toBigDecimal(value).doubleValue();
            case Long:
                if (value == null) {
                    value = "0";
                }
                return Utils.toBigDecimal(value).longValue();
            case Float:
                if (value == null) {
                    value = "0";
                }
                return Utils.toBigDecimal(value).floatValue();
            case BigDecimal:
                if (value == null) {
                    value = "0";
                }
                return Utils.toBigDecimal(value);
            case Boolean:
                if (value == null) {
                    value = "false";
                }
                return BooleanUtils.toBoolean(value.toString());
            case Date:
                if (value == null) {
                    return null;
                }
                try {
                    if (value instanceof Date) {
                        return (Date) value;
                    }
                    if (value.toString().equals("")) {
                        return null;
                    }
                    try {
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return sd.parse(value.toString());
                    } catch (Exception ex) {
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                        return sd.parse(value.toString());
                    }

                } catch (ParseException e) {
                    throw new RuleException(e);
                }
            case List:
                if (value == null) {
                    return null;
                }
                if (value instanceof List) {
                    return (List) value;
                }
                List<Object> list = new ArrayList<Object>();
                String[] values = value.toString().split(",");
                for (String v : values) {
                    list.add(v);
                }
                return list;
            case Set:
                if (value == null) {
                    return null;
                }
                if (value instanceof Set) {
                    return (Set) value;
                }
                Set<Object> set = new TreeSet<Object>();
                for (String v : value.toString().split(",")) {
                    set.add(v);
                }
                return set;
            case Map:
                if (value == null) {
                    return null;
                }
                if (value instanceof Map) {
                    return (Map) value;
                }
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Map<String, Object> map = mapper.readValue(value.toString(), new HashMapTypeReference());
                    return map;
                } catch (Exception e) {
                    throw new RuleException(e);
                }
            case Enum:
                return value;
            case LocalDateTime:
                if (value == null || value.toString().equals("")) {
                    return null;
                }
                if (value instanceof org.joda.time.LocalDateTime) {
                    return (org.joda.time.LocalDateTime) value;
                }
                try {
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sd.parse(value.toString());
                    return new LocalDateTime(date.getTime());
                } catch (ParseException e) {
                    throw new RuleException(e);
                }

            case LocalDate:
                if (value == null || value.toString().equals("")) {
                    return null;
                }
                if (value instanceof org.joda.time.LocalDate) {
                    return (org.joda.time.LocalDate) value;
                }
                try {
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sd.parse(value.toString());
                    return new org.joda.time.LocalDate(date.getTime());
                } catch (ParseException e) {
                    throw new RuleException(e);
                }
            case YearMonth:
                if (value == null || value.toString().equals("")) {
                    return null;
                }

                if (value instanceof org.joda.time.YearMonth) {
                    return (org.joda.time.YearMonth) value;
                }
                java.lang.String string = value.toString();
                java.lang.String[] array = string.split("-");
                int year = java.lang.Integer.parseInt(array[0]);
                int month = java.lang.Integer.parseInt(array[1]);
                return new org.joda.time.YearMonth(year, month);
            default:
                return null;
        }
    }
}

class HashMapTypeReference extends TypeReference<HashMap<String, Object>> {
}
