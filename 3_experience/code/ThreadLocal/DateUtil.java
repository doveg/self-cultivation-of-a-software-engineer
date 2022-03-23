public class DateUtil {

    private static final String DEFAULT_FOMRT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 重写initialValue 方法
     * 当我们使用 ThreadLocal 的 get 方法的时候
     * ThreadLocal 会先查看我们 是否主动使用了  set方法设置了值
     * 如果没有，或者 set 进去的被 remove了，就使用 initialValue 返回的值
     * 如果不重写，initialValue 默认返回 null
     * 这里重写了 initialValue 我们就不需要手动 set 的方式去添加 SimpleDateFormat
     */
    private static final ThreadLocal<SimpleDateFormat> threadLocalSdf = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    public static void setPattern(String pattern) {
        threadLocalSdf.get().applyPattern(pattern);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_FOMRT);
    }

    public static String format(Date date, String pattern) {
        if (pattern == null || "".equals(pattern)) {
            throw new IllegalArgumentException("pattern is not allow null or '' ");
        }
        setPattern(pattern);
        return threadLocalSdf.get().format(date);
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_FOMRT);
    }

    public static Date parse(String dateStr, String pattern) {
        setPattern(pattern);
        Date date = null;
        try {
            date = threadLocalSdf.get().parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
