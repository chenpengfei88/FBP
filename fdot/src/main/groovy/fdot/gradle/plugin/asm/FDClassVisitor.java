package fdot.gradle.plugin.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

import fdot.gradle.plugin.xmlparse.APIMethod;
import fdot.gradle.plugin.xmlparse.FDClass;
import fdot.gradle.plugin.xmlparse.FDMethod;

/**
 * Created by chenepngfei on 2017/7/13.
 */
public class FDClassVisitor extends ClassVisitor {

    /**
     *  Dot Class
     */
    private FDClass fdClass;

    /**
     * API Method List
     */
    private List<APIMethod> apiMethodList;

    public FDClassVisitor(int api) {
        super(api);
    }

    public FDClassVisitor(int api, ClassVisitor cv, FDClass fdClass, List<APIMethod> apiMethodList) {
        super(api, cv);

        this.fdClass = fdClass;
        this.apiMethodList = apiMethodList;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (cv != null) {
            cv.visit(version, access, name, signature, superName, interfaces);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        FDMethod fdMethod = findFdMethod(name);
        if (fdMethod != null) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return new FDMethodVisitor(Opcodes.ASM5, mv, fdClass, fdMethod, apiMethodList.get(fdMethod.getApiMethodIndex()));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    /**
     *  Find Dot Method
     * @param methodName
     * @return
     */
    private FDMethod findFdMethod(String methodName) {
        FDMethod findFdMethod = null;
        List<FDMethod> fdMethodList = fdClass.getMethodList();
        for (int m = 0, size = fdMethodList.size(); m < size; m++) {
            FDMethod fdMethod = fdMethodList.get(m);
            if (fdMethod.getMethodName().equals(methodName)) {
                findFdMethod = fdMethod;
                break;
            }
        }
        return findFdMethod;
    }
}
