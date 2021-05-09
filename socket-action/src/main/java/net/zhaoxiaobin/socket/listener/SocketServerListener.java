package net.zhaoxiaobin.socket.listener;

import net.zhaoxiaobin.socket.common.IListenService;
import net.zhaoxiaobin.socket.config.SocketChannelConfigParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 当spring容器将所有的bean都初始化完成后执行该类
 *
 * @author zhaoxb
 * @date 2021-04-27 11:35 上午
 */
//@Component
public class SocketServerListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SocketChannelConfigParse socketChannelConfigParse;

    @Autowired
    private IListenService listenService;

    /**
     * 在web 项目中（spring mvc），系统会存在两个容器，一个是root application context，另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
     * 这种情况下，就会造成onApplicationEvent方法被执行两次。为了避免上面提到的问题，我们可以只在root application context初始化完成后调用逻辑代码，其他的容器的初始化完成，则不做任何处理
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 如果是子容器，则不执行该方法
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        // 开启监听
        listenService.startListen(socketChannelConfigParse.getSocketChannelConfigList());
    }
}