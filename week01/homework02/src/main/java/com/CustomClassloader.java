package com;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class CustomClassloader extends ClassLoader{
    private String path ;
    public CustomClassloader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class log = null;
        // 获取该class文件字节码数组
        byte[] classData = getByteData();

        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            log = defineClass(name, classData, 0, classData.length);
        }
        return log;
    }
    private byte[] getByteData() {

        File file = new File(path);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = (byte) (255 - (buffer[i] & 0xff));
                    }
                    out.write(buffer, 0, size);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }else{
            return null;
        }


    }
    public static void main(String[] args) throws Exception{

        String classPath = "src\\main\\java\\com\\Hello.xlass";//class文件路径
        CustomClassloader myClassLoader = new CustomClassloader(classPath);
        String packageNamePath = "Hello";//类的全称
        //加载这个class文件
        Class<?> Log = myClassLoader.loadClass(packageNamePath);//loadClass里会调用findClass方法
        //利用反射获取方法
        Method method = Log.getDeclaredMethod("hello");//hello为方法名
        Object object = Log.newInstance();
        method.invoke(object);
    }
}
