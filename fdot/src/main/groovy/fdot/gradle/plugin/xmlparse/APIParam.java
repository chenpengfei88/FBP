package fdot.gradle.plugin.xmlparse;

import fdot.gradle.plugin.utils.Constants;
import fdot.gradle.plugin.utils.Util;

/**
 * Created by chenpengfei on 2017/7/13.
 *  API Param
 */
public class APIParam {

    /**
     *  Param Type
     */
    private String paramType;

    /**
     *  ParamTransfer Type
     */
    private String paramTransferType;

    public APIParam(String paramType, String paramTransferType) {
        this.paramType = Util.replaceAllSpecifySymbol(paramType, Constants.REPLACE_POINT, Constants.REPLACE_LINE);
        this.paramTransferType = paramTransferType;
    }

    public String getParamTransferType() {
        return paramTransferType;
    }

    public String getParamType() {
        return paramType;
    }

}
