package fdot.gradle.plugin.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

import fdot.gradle.plugin.utils.Constants;
import fdot.gradle.plugin.utils.Util;
import fdot.gradle.plugin.xmlparse.APIMethod;
import fdot.gradle.plugin.xmlparse.APIParam;
import fdot.gradle.plugin.xmlparse.FDClass;
import fdot.gradle.plugin.xmlparse.FDMethod;
import fdot.gradle.plugin.xmlparse.FDParam;

/**
 * Created by chenpengfei on 2017/7/13.
 */
public class FDMethodVisitor extends MethodVisitor implements Opcodes {

    /**
     *  Dot Class
     */
    private FDClass fdClass;

    /**
     *  Dot Method Params
     */
    private List<FDParam> fdParamList;

    /**
     * API Method
     */
    private APIMethod apiMethod;

    /**
     *  API Method Params
     */
    private List<APIParam> apiParamList;

    public FDMethodVisitor(int api) {
        super(api);
    }

    public FDMethodVisitor(int api, MethodVisitor mv, FDClass fdClass, FDMethod fdMethod, APIMethod apiMethod) {
        super(api, mv);

        this.fdClass = fdClass;
        this.fdParamList = fdMethod.getParamList();
        this.apiMethod = apiMethod;
        this.apiParamList = apiMethod.getParamList();
    }

    @Override
    public void visitCode() {
        super.visitCode();

        StringBuffer paramTypeStringBuffer = new StringBuffer();

        for (int i = 0, size = fdParamList.size(); i < size; i++) {
            FDParam fdParam = fdParamList.get(i);
            APIParam apiParam = apiParamList.get(i);

            String asmParamTypeText = Util.getASMParamTypeText(apiParam.getParamType());

            if (Util.paramTransferTypeField(apiParam.getParamTransferType())) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, fdClass.getASMClassName(), fdParam.getParamValue(), asmParamTypeText);
            }

            if (Util.paramTransferTypeConstant(apiParam.getParamTransferType())) {
                mv.visitLdcInsn(fdParam.getParamValue());
            }

            if (i == 0) {
                paramTypeStringBuffer.append(Constants.INTERCEPT_RIGHT_BRACKETS);
            }
            paramTypeStringBuffer.append(asmParamTypeText);
            if (i == size - 1) {
                paramTypeStringBuffer.append(Constants.INTERCEPT_LEFT_BRACKETS_V);
            }
        }
        mv.visitMethodInsn(INVOKESTATIC, apiMethod.getMethodPackageName(), apiMethod.getMethodName(), paramTypeStringBuffer.toString(), false);
    }
}
