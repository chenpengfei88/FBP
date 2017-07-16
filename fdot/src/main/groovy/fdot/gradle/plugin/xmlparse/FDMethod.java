package fdot.gradle.plugin.xmlparse;

import java.util.List;

/**
 * Created by lenovo on 2017/7/13.
 * Dot Method
 */
public class FDMethod {

    /**
     * Method Name
     */
    private String methodName;

    /**
     *  API Method Index
     */
    private int apiMethodIndex;

    /**
     * Param List
     */
    private List<FDParam> paramList;

    public FDMethod(String methodName, String apiMethodIndex) {
        this.methodName = methodName;
        this.apiMethodIndex = Integer.valueOf(apiMethodIndex);
    }

    public List<FDParam> getParamList() {
        return paramList;
    }

    public int getApiMethodIndex() {
        return apiMethodIndex;
    }

    public void setParamList(List<FDParam> paramList) {
        this.paramList = paramList;
    }

    public String getMethodName() {
        return methodName;
    }


}
