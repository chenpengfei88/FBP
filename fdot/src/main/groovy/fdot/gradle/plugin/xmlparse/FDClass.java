package fdot.gradle.plugin.xmlparse;

import java.util.List;

import fdot.gradle.plugin.utils.Constants;
import fdot.gradle.plugin.utils.Util;

/**
 * Created by chenpengfei on 2017/7/13.
 *  Dot Class
 */
public class FDClass {

    /**
     * Class Name
     */
    private String className;

    /**
     * ASM Class Name
     */
    private String ASMClassName;

    /**
     * Dot Method List
     */
    private List<FDMethod> methodList;

    public FDClass(String className, List<FDMethod> methodList) {
        this.className = className;
        this.ASMClassName = Util.replaceAllSpecifySymbol(className, Constants.REPLACE_POINT, Constants.REPLACE_LINE);
        this.methodList = methodList;
    }

    public List<FDMethod> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<FDMethod> methodList) {
        this.methodList = methodList;
    }

    public String getClassName() {
        return className;
    }

    public String getASMClassName() {
        return ASMClassName;
    }

}
