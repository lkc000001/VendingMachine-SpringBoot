package com.demo.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ValidateUtil {

    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } 
        return false;
    }

    public boolean isNotEmpty(Object obj) {
        if (obj != null) {
            return true;
        }
        return false;
    }

    public boolean isNotEmptyAndSize(List<?> list) {
        if (list != null && list.size() > 0) {
            return true;
        } 
        return false;
    }

    public boolean isBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public boolean isNotBlank(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        } 
        return false;
    }

    public boolean isIntegerNull(Integer num) {
        if (num == null) {
            return true;
        } 
        return false;
    }

    public boolean isNotNumNone(Integer num) {
        if (num != null && num != 0) {
            return true;
        } 
        return false;
    }

    public boolean isNotNumNone(BigDecimal num) {
        if (num != null && BigDecimal.ZERO.compareTo(num) != 0) {
            return true;
        } 
        return false;
    }

    /**
     * 民國年日期之檢查
     * Input String.format(ckdate)
     * @param ckdate
     * @return
     */
    public boolean checkCDate(String ckdate) {

        // 判斷長度是否為7碼
        if (ckdate.length() != 7)
            return false;

        String yy = String.format("%04d", Integer.parseInt(ckdate.substring(0, 3)) + 1911);
        String dateStr = String.format("%s-%s-%s", yy, ckdate.substring(3, 5), ckdate.substring(5, 7));

        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /*public String checkBetweenNumber(Integer start, Integer end) {
		
	}*/

}
