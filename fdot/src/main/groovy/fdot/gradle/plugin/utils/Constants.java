package fdot.gradle.plugin.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenpengfei on 2017/7/13.
 *  Constant Class
 */
public class Constants {

    /**
     *  System R File List
     */
    public static List<String> RFileClassList = new ArrayList<>();

    static {
        RFileClassList.add("R$array");
        RFileClassList.add("R$xml");
        RFileClassList.add("R$styleable");
        RFileClassList.add("R$style");
        RFileClassList.add("R$string");
        RFileClassList.add("R$raw");
        RFileClassList.add("R$menu");
        RFileClassList.add("R$layout");
        RFileClassList.add("R$integer");
        RFileClassList.add("R$id");
        RFileClassList.add("R$drawable");
        RFileClassList.add("R$dimen");
        RFileClassList.add("R$color");
        RFileClassList.add("R$bool");
        RFileClassList.add("R$mipmap");
        RFileClassList.add("BuildConfig");
        RFileClassList.add("R$attr");
        RFileClassList.add("R$anim");
        RFileClassList.add("R");
    }

    /**
     * "||"
     */
    public static final String INTERCEPT_DOUBLE_VERTICAL_LINE = "||";

    /**
     * Right"("
     */
    public static final String INTERCEPT_RIGHT_BRACKETS = "(";

    /**
     * Left ")V"
     */
    public static final String INTERCEPT_LEFT_BRACKETS_V = ")V";

    /**
     * "/"
     */
    public static final String REPLACE_LINE = "/";

    /**
     * "."
     */
    public static final String REPLACE_POINT = "\\.";

    /**
     *  Xml File
     */
    public static final String FDOT_XML = "fdot.xml";

    /**
     *  Param TransferType Field
     */
    public static final String PARAM_TRANSFER_TYPE_FIELD = "field";

    /**
     *  Param TransferType Constant
     */
    public static final String PARAM_TRANSFER_TYPE_CONSTANT = "constant";

}
