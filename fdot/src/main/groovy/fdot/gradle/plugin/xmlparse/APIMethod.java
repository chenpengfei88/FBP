package fdot.gradle.plugin.xmlparse;

import java.util.List;

import fdot.gradle.plugin.utils.Constants;
import fdot.gradle.plugin.utils.Util;

/**
 * Created by chenpengfei on 2017/7/13.
 *  API Method
 */
public class APIMethod {

    /**
     *  Method PackageName
     */
    private String methodPackageName;

    /**
     *  MethodName
     */
    private String methodName;

    /**
     *  Param
     */
    private List<APIParam> paramList;

    public APIMethod(String packageMethodName, List<APIParam> paramList) {
        String dealPackageMethodName = Util.replaceAllSpecifySymbol(packageMethodName, Constants.REPLACE_POINT, Constants.REPLACE_LINE);
        int lineIndex = dealPackageMethodName.lastIndexOf(Constants.REPLACE_LINE);
        methodPackageName = dealPackageMethodName.substring(0, lineIndex);
        methodName = dealPackageMethodName.substring(lineIndex + 1);
        this.paramList = paramList;
    }

    public String getMethodPackageName() {
        return methodPackageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<APIParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<APIParam> paramList) {
        this.paramList = paramList;
    }

}
