package ru.alterland.java.launcher;

import javafx.application.Platform;
import ru.alterland.Main;
import ru.alterland.controllers.pages.MainServers;
import ru.alterland.java.ServerData;
import ru.alterland.java.api.Servers;
import ru.alterland.java.api.exceptions.ApiExceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public final class ScheduledTasks {
    private static Logger log = Logger.getLogger(ScheduledTasks.class.getName());

    public static final class UpdateServerList extends TimerTask {
        private MainServers mainServers;
        @Override
        public void run() {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                log.info("Update server data");
                try {
                    List<String> servers = new ArrayList<>();
                    mainServers.getServersData().forEach(serverData -> servers.add(serverData.getName()));
                    if (Servers.getNewServersList(Main.getUserData(), servers)){
                        List<ServerData> serverData = Servers.getServerList(Main.getUserData());
                        Platform.runLater(() -> mainServers.renderCardList(serverData));
                    }
                } catch (ApiExceptions apiExceptions) {
                    switch (apiExceptions.getType()){
                        case ConnectionError:
                            Main.fatalError("Не удается подключиться, проверьте соединение и перезапустите лаунчер", mainServers.getMainWrapper(), apiExceptions);
                            break;
                        case InvalidAccessToken:
                            Main.fatalError("Кто то зашел через ваш аккаунт. Попросите его выйти и перезапустите лаунчер", mainServers.getMainWrapper(), apiExceptions);
                            break;
                        default:
                            Main.fatalError(mainServers.getMainWrapper(), apiExceptions);
                    }
                }
            });
        }

        public UpdateServerList(MainServers mainServers){
            this.mainServers = mainServers;
        }
    }

}
