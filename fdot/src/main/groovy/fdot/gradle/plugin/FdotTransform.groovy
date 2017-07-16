package fdot.gradle.plugin

import com.android.SdkConstants
import com.android.build.api.transform.Context
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import fdot.gradle.plugin.asm.FDClassVisitor
import fdot.gradle.plugin.utils.Util
import fdot.gradle.plugin.xmlparse.APIMethod
import fdot.gradle.plugin.xmlparse.APIParam
import fdot.gradle.plugin.xmlparse.FDClass
import fdot.gradle.plugin.xmlparse.FDMethod
import fdot.gradle.plugin.utils.Constants
import fdot.gradle.plugin.xmlparse.FDParam
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarOutputStream
import java.util.regex.Matcher
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Created by chenpengfei on 17/7/13.
 *  插入打点代码
 */

public class FdotTransform extends Transform implements Plugin<Project> {

    /**
     * 项目
     */
    def project

    /**
     * android
     */
    def android

    /**
     * xml 文件
     */
    def fdot

    /**
     *  logger
     */
    Logger logger

    /**
     *  是否打开打点
     */
   boolean turnOnFdot

    /**
     * 打点的类集合
     */
    private List<FDClass> fdClassList;

    /**
     * API方法集合
     */
    private List<APIMethod> apiMethodList;

    @Override
    void apply(Project project) {
        initConfig(project);
    }

    /**
     *  初始化配置
     */
    void initConfig(Project project) {
        this.project = project
        this.logger = this.project.logger

        fdClassList = new ArrayList<>()
        apiMethodList = new ArrayList<>()

        parseXml()

        if (turnOnFdot) {
            android = this.project.extensions.getByType(AppExtension)
            android.registerTransform(this)
        }
    }

    /**
     *  解析xml配置文件
     */
    void parseXml() {
        def startTime = System.currentTimeMillis()

        fdot = new XmlSlurper().parse(new File("${project.projectDir}/${Constants.FDOT_XML}"))

        /**
         *  是否打开打点
         */
        if (null != fdot.switch.turnOnFdot && "false".equals(String.valueOf(fdot.switch.turnOnFdot.text()))) {
            turnOnFdot = false;
        } else {
            turnOnFdot = true;
        }
        logger.quiet '-----------------turnOnFdot--------------' + turnOnFdot
        if (!turnOnFdot) return

        parseXmlToFdClass()
        parseXmlToFdAPIClass()

        def cost = (System.currentTimeMillis() - startTime) / 1000
        logger.quiet "----------------XmlParse cost $cost second----------------"
    }

    /**
     *  从xml中解析出打点的类转换成class文件
     */
    void parseXmlToFdClass() {
        fdot.classname.each {
            def fdClassName = it.name.text()

            List<FDMethod> fdMethodList = new ArrayList<>();
            it.method.name.each {
                def methodParamAPIName = it.text();

                def lineIndex = methodParamAPIName.indexOf(Constants.INTERCEPT_DOUBLE_VERTICAL_LINE)
                def methodParamName = methodParamAPIName.substring(0, lineIndex);
                def apiIndexValue = methodParamAPIName.substring(lineIndex + 2);

                def rightBracketsIndex = methodParamName.indexOf(Constants.INTERCEPT_RIGHT_BRACKETS)
                def methodName = methodParamName.substring(0, rightBracketsIndex)
                def methodParams = methodParamName.substring(rightBracketsIndex + 1, methodParamName.length() - 1)
                def methodParamArray = methodParams.split(",");

                //打点的方法
                FDMethod fdMethod = new FDMethod(methodName, apiIndexValue)
                List<FDParam> fdParamList = new ArrayList<>()
                int plength = methodParamArray.length
                for (int p = 0; p < plength; p++) {
                    FDParam fdParam = new FDParam(methodParamArray[p]);
                    fdParamList.add(fdParam);
                }

                fdMethod.setParamList(fdParamList)
                fdMethodList.add(fdMethod)
            }

            FDClass fdClass = new FDClass(fdClassName, fdMethodList)
            fdClassList.add(fdClass)
        }
    }

    /**
     *  Parse To APIClass From XML File
     */
    void parseXmlToFdAPIClass() {
        fdot.apimethod.each {
            def apiMethodName = it.name.text()

            List<APIParam> apiParamList = new ArrayList<>()
            it.param.name.each {
                def apiParamName = it.text()
                def lineIndex = apiParamName.indexOf(Constants.INTERCEPT_DOUBLE_VERTICAL_LINE)

                APIParam apiParam = new APIParam(apiParamName.substring(0, lineIndex), apiParamName.substring(lineIndex + 2))
                apiParamList.add(apiParam)
            }
            APIMethod apiMethod = new APIMethod(apiMethodName, apiParamList)
            apiMethodList.add(apiMethod)
        }
    }

    @Override
    String getName() {
        return "Fdot"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    /**
     *  找到打点的class类
     * @param packageClassName
     * @return
     */
    FDClass findFdClass(String packageClassName) {
        FDClass findFdClass = null;
        for (int m = 0; m < fdClassList.size(); m++) {
            FDClass fdClass = fdClassList.get(m);
            if (fdClass.getClassName().equals(packageClassName)) {
                findFdClass = fdClass;
                break;
            }
        }
        return findFdClass;
    }

    /**
     * class文件写入jar
     * @param classBytesArray
     * @param zos
     * @param entryName
     */
     void zipFile(byte[] classBytesArray, ZipOutputStream zos, String entryName){
        try {
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            zos.write(classBytesArray, 0, classBytesArray.length);
            zos.closeEntry();
            zos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  遍历解析项目文件
     */
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        logger.quiet '-----------------fdot start--------------'

        def fdot = outputProvider.getContentLocation("fdot", getOutputTypes(), getScopes(), Format.JAR);
        if (!fdot.getParentFile().exists()) {
            fdot.getParentFile().mkdirs();
        }
        if (fdot.exists()) {
            fdot.delete();
        }

        ZipOutputStream outStream = new JarOutputStream(new FileOutputStream(fdot));

        inputs.each {
            it.directoryInputs.each {
                def dirPath = it.file.absolutePath
                org.apache.commons.io.FileUtils.listFiles(it.file, null, true).each {
                    if (it.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
                        /**
                         *   packageClassNamePath == android\support\v7\appcompat\R.class
                         */
                        def packageClassNamePath = it.absolutePath.substring(dirPath.length() + 1, it.absolutePath.length())

                        /**
                         *   packageClassName ==  android.support.v7.appcompat.R
                         */
                        def packageClassName = packageClassNamePath.substring(0, packageClassNamePath.length() - SdkConstants.DOT_CLASS.length()).replaceAll(Matcher.quoteReplacement(File.separator), '.')

                        /**
                         * className == R
                         */
                        def className = packageClassName.substring(packageClassName.lastIndexOf(".") + 1)

                        FDClass findFdClass = findFdClass(packageClassName)
                        /**
                         *  不是系统R文件并且在xml中有配置
                         */
                        if (!Util.isRFileClass(className) && findFdClass != null) {
                            ClassReader cr = new ClassReader(it.bytes);
                            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                            ClassVisitor cv = new FDClassVisitor(Opcodes.ASM5, cw, findFdClass, apiMethodList);

                            cr.accept(cv, Opcodes.ASM5);
                            zipFile(cw.toByteArray(), outStream, packageClassNamePath)
                        } else {
                            zipFile(it.bytes, outStream, packageClassNamePath)
                        }
                   }
               }
            }

            it.jarInputs.each {
                def jarName = it.name
                def dest = outputProvider.getContentLocation(jarName, it.contentTypes, it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, dest)
            }
       }
        outStream.close();

        logger.quiet '-----------------fdot end--------------'
    }
}