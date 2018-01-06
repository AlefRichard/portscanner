/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portscanner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author alef
 */
public class PortScanner {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {

	final String ip = "127.0.0.1";
	final int timeout = 200;
	final ExecutorService executorService = Executors.newFixedThreadPool(20);
	final List<Future<Boolean>> futures = new ArrayList<>();

	for (int porta = 1; porta <= 65535; porta++) {
		futures.add(portaEstaAberta(executorService, ip, porta, timeout));
	}

	executorService.shutdown();
	int portasAbertas = 0;

	for (final Future<Boolean> future : futures) {
		if (future.get()) {
			portasAbertas++;
		}
	}
        
	System.out.println("Existem " + portasAbertas + " portas abertas nesse host " + ip);
    }

    public static Future<Boolean> portaEstaAberta(final ExecutorService es, final String ip, final int port,
		final int timeout) {
	return es.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(ip, port), timeout);
                    }
                    return true;
                    
                } catch (Exception ex) {
                    return false;
                }
            }
        });
    }
    
}

