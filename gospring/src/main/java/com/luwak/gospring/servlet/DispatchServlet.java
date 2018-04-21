package com.luwak.gospring.servlet;

import com.luwak.demo.action.DemoAction;
import com.luwak.gospring.annotation.Autowired;
import com.luwak.gospring.annotation.Controller;
import com.luwak.gospring.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanggang
 * @date 2018年4月19日 上午9:07:38
 * 
 */
public class DispatchServlet extends HttpServlet {
	
	private Properties contextConfig = new Properties();
	
	private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();
	
	private List<String> classNames = new ArrayList<String>();

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		this.doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		System.out.println("=============调用doPost=============");
	}

    @Override
	public void init(ServletConfig config) throws ServletException {
		
		//开始初始化的过程
		
		//定位
		doLoadConfig(config.getInitParameter("contextConfigLocation"));
		
		//加载
		doScanner(contextConfig.getProperty("scanPackage"));
		
		//注册
		doRegistry();

        //在Spring中是通过调用getBean方法才发出依赖注入的
        doAutowired();
		
		//自动依赖注入
        DemoAction demo = (DemoAction) beanMap.get("demoAction");
        demo.queryName(null, null, "luwak");

		
	}

	private void doAutowired() {
		if(beanMap.isEmpty()) {
		    return;
        }

        try {
            for(Map.Entry<String, Object> entry : beanMap.entrySet()) {

                Field[] fields = entry.getValue().getClass().getDeclaredFields();

                for(Field field : fields) {
                    if(field.isAnnotationPresent(Autowired.class)) {

                        Autowired autowired = field.getAnnotation(Autowired.class);

                        String beanName = autowired.value().trim();
                        if("".equals(beanName)) {
                            beanName = field.getType().getName();
                        }

                        field.setAccessible(true);

                        field.set(entry.getValue(), beanMap.get(beanName));

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	private void doRegistry() {
		if(classNames.isEmpty()) {
		    return;
        }

        for(String className : classNames) {

            try {
                Class<?> claxx = Class.forName(className);

                if(claxx.isAnnotationPresent(Controller.class)) {

                    String beanName = lowerFirstCase(claxx.getSimpleName());

                    Object instance = claxx.newInstance();

                    beanMap.put(beanName, instance);
                } else if(claxx.isAnnotationPresent(Service.class)) {

                    //是否存在指定类型的注解
                    Service service = claxx.getAnnotation(Service.class);

                    String beanName = service.value();
                    if("".equals(beanName)) {
                        beanName = lowerFirstCase(claxx.getSimpleName());
                    }

                    Object instance = claxx.newInstance();
                    beanMap.put(beanName, instance);

                    Class<?>[] interfaces = claxx.getInterfaces();
                    for(Class i : interfaces) {
                        beanMap.put(i.getName(), instance);
                    }

                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

	}

	private void doScanner(String packageName) {
		URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
		File classDir = new File(url.getPath());
		for(File file : classDir.listFiles()) {
			if(file.isDirectory()) {
				doScanner(packageName + "." + file.getName());
			} else {
				classNames.add(packageName + "." + file.getName().replace(".class", ""));
			}
		}
	}

	private void doLoadConfig(String location) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
		try {
			contextConfig.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null!=is)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private String lowerFirstCase(String str) {

	    char[] chars = str.toCharArray();
	    chars[0] += 32;

	    return String.valueOf(chars);
    }

}
