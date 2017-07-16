package fdot.gradle.plugin.utils;


/**
 * Created by chenpengfei on 2017/7/13.
 *  Util Class
 */
public class Util {

    /**
     * Is System R File Class
     */
    public static boolean isRFileClass(String className) {
        return Constants.RFileClassList.contains(className);
    }

    /**
     *  ReplaceAll
     */
    public static String replaceAllSpecifySymbol(String text, String regex, String replaceSymbol) {
        return text.replaceAll(regex, replaceSymbol);
    }

    /**
     *  Param TransferType Is Field
     */
    public static boolean paramTransferTypeField(String transferType) {
        return transferType.equals(Constants.PARAM_TRANSFER_TYPE_FIELD);
    }

    /**
     *  Param TransferType Is Constant
     */
    public static boolean paramTransferTypeConstant(String transferType) {
        return transferType.equals(Constants.PARAM_TRANSFER_TYPE_CONSTANT);
    }

    /**
     *  Get ASM Need Param Type Text
     */
    public static String getASMParamTypeText(String paramType) {
        return "L" + paramType + ";";
    }
}
