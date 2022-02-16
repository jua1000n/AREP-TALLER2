import javax.xml.ws.Service;
import java.io.IOException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ECISpringBoot {
    private Map<String, Member> services = new HashMap<>();
    private static ECISpringBoot _instance = new ECISpringBoot();

    private ECISpringBoot() {}

    public static ECISpringBoot getInstance() {
        return _instance;
    }
    public void startServer() {
        try {
            HttpServer httpServer = new HttpServer();
            httpServer.start();
        } catch (IOException e) {
            Logger.getLogger(ECISpringBoot.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void loadComponents() {
        String[] componentList = searchComponentList();
        for (String componentName: componentList) {
            loadServices(componentName);
        }
    }

    private void loadServices(String componentName) {
        try {
            Class c = Class.forName(componentName);
            Method[] declareMethods = c.getDeclaredMethods();
            for (Method m : declareMethods) {
                if(m.isAnnotationPresent(Service.class)) {
                    Service a = m.getAnnotation(Service.class);
                    String serviceName = a.value();
                    services.put(serviceName, m);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String invokeService(String serviceName) {
        Method serviceMethod = services.get(serviceName);
        return serviceMethod.invoke(null);
    }

    private String[] searchComponentList() {
        return new String[]{"statusService"};
    }
}
