package com.example.firstdome.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 电话号码校验工具类
 * 支持11位手机号码的格式验证
 */
public class PhoneNumberValidator {

    // 中国大陆手机号码正则表达式
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    // 预编译正则表达式模式，提高性能
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * 私有构造函数，防止实例化
     */
    private PhoneNumberValidator() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }

    /**
     * 验证是否为有效的11位手机号码
     * @param phoneNumber 待验证的手机号码字符串
     * @return true-有效，false-无效
     */
    public static boolean isValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        String trimmedNumber = phoneNumber.trim();

        // 基本长度检查
        if (trimmedNumber.length() != 11) {
            return false;
        }

        // 正则表达式匹配
        Matcher matcher = PHONE_PATTERN.matcher(trimmedNumber);
        return matcher.matches();
    }

    /**
     * 验证手机号码并返回详细信息
     * @param phoneNumber 待验证的手机号码
     * @return 包含验证结果和运营商信息的验证结果对象
     */
    public static ValidationResult validateWithDetail(String phoneNumber) {
        ValidationResult result = new ValidationResult();

        if (phoneNumber == null) {
            result.setValid(false);
            result.setMessage("手机号码不能为null");
            return result;
        }

        String trimmedNumber = phoneNumber.trim();
        result.setPhoneNumber(trimmedNumber);

        // 检查是否为空
        if (trimmedNumber.isEmpty()) {
            result.setValid(false);
            result.setMessage("手机号码不能为空");
            return result;
        }

        // 检查长度
        if (trimmedNumber.length() != 11) {
            result.setValid(false);
            result.setMessage("手机号码必须是11位数字");
            return result;
        }

        // 检查是否全是数字
        if (!trimmedNumber.matches("\\d+")) {
            result.setValid(false);
            result.setMessage("手机号码必须全部由数字组成");
            return result;
        }

        // 正则表达式验证
        Matcher matcher = PHONE_PATTERN.matcher(trimmedNumber);
        if (!matcher.matches()) {
            result.setValid(false);
            result.setMessage("无效的手机号码格式");
            return result;
        }

        // 验证通过，识别运营商
        result.setValid(true);
        result.setMessage("有效的手机号码");
        result.setCarrier(identifyCarrier(trimmedNumber));

        return result;
    }

    /**
     * 根据手机号码识别运营商
     * @param phoneNumber 手机号码
     * @return 运营商名称
     */
    private static String identifyCarrier(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 3) {
            return "未知";
        }

        String prefix = phoneNumber.substring(0, 3);

        switch (prefix) {
            case "130": case "131": case "132": case "145": case "155": case "156": case "166": case "171": case "175": case "176": case "185": case "186":
                return "中国联通";
            case "133": case "149": case "153": case "173": case "177": case "180": case "181": case "189": case "199":
                return "中国电信";
            case "134": case "135": case "136": case "137": case "138": case "139": case "147": case "148": case "150": case "151": case "152":
            case "157": case "158": case "159": case "172": case "178": case "182": case "183": case "184": case "187": case "188": case "198":
                return "中国移动";
            case "190": case "191": case "192": case "193": case "194": case "195": case "196": case "197":
                return "中国广电";
            default:
                return "未知运营商";
        }
    }

    /**
     * 格式化手机号码（添加空格分隔）
     * @param phoneNumber 原始手机号码
     * @return 格式化后的手机号码，如：138 0013 8000
     */
    public static String formatPhoneNumber(String phoneNumber) {
        if (!isValid(phoneNumber)) {
            return phoneNumber; // 如果不是有效号码，返回原值
        }

        String trimmedNumber = phoneNumber.trim();
        return trimmedNumber.substring(0, 3) + " " +
                trimmedNumber.substring(3, 7) + " " +
                trimmedNumber.substring(7);
    }

    /**
     * 隐藏手机号码中间四位
     * @param phoneNumber 原始手机号码
     * @return 隐藏后的手机号码，如：138****8000
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (!isValid(phoneNumber)) {
            return phoneNumber; // 如果不是有效号码，返回原值
        }

        String trimmedNumber = phoneNumber.trim();
        return trimmedNumber.substring(0, 3) + "****" + trimmedNumber.substring(7);
    }

    /**
     * 验证结果类
     */
    public static class ValidationResult {
        private boolean isValid;
        private String message;
        private String phoneNumber;
        private String carrier;

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        @Override
        public String toString() {
            return "ValidationResult{" +
                    "isValid=" + isValid +
                    ", message='" + message + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", carrier='" + carrier + '\'' +
                    '}';
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        String[] testNumbers = {
                "13800138000",    // 有效
                "13912345678",    // 有效
                "18612345678",    // 有效
                "12345678901",    // 无效 - 开头不是1
                "11111111111",    // 无效 - 格式错误
                "1380013800",     // 无效 - 只有10位
                "138001380000",   // 无效 - 12位
                "138abc45678",    // 无效 - 包含字母
                "",               // 无效 - 空字符串
                null              // 无效 - null
        };

        System.out.println("手机号码验证测试：");
        for (String number : testNumbers) {
            boolean valid = isValid(number);
            ValidationResult result = validateWithDetail(number);
            System.out.println(number + " -> " + valid + " | " + result.getMessage() +
                    (result.isValid() ? " (" + result.getCarrier() + ")" : ""));
        }

        // 格式化测试
        System.out.println("\n格式化测试：");
        String phone = "13800138000";
        System.out.println("原始: " + phone);
        System.out.println("格式化: " + formatPhoneNumber(phone));
        System.out.println("隐藏: " + maskPhoneNumber(phone));
    }
}